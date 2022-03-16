package com.wf.springboot.reactor.app;

import com.wf.springboot.reactor.app.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Locale;

@SpringBootApplication
public class SpringBootReactorApplication  implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Flux<Usuario> nombres = Flux.just("Andres wiese", "Pedro aquino", "Manuel medran","Harry coa", "Yandira cia", "Bruce Lee", "Bruce Willis") // flux es un publisher (Observable)
				//  .doOnNext( element -> System.out.println(element) );
				// utilizando el map para convertirlo de string a Objeto usuario
				.map( nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
				.filter( usuario ->  usuario.getNombre().toLowerCase().equals("bruce")  )
				.doOnNext( usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Vacios");
					} else {
						System.out.println(usuario.getNombre().concat("-").concat(usuario.getApellido()));
					}
				})
				.map(usuario -> {
					String nombre = usuario.getNombre().toUpperCase();
					usuario.setNombre(nombre);
					return usuario;
				});

		// nombres.subscribe(log::info);
		nombres.subscribe(e -> log.info(e.toString()),
				error -> log.error(error.getMessage()),
				new Runnable() {
					@Override
					public void run() {
						log.info("Ha finalizado el observable");
					}
				}
				// transformado a lambda => () -> log.info("Ha finalizado el observable")

		);

	}
}
