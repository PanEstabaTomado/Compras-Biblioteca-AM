package dfy1103.magaq.compra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
---------------------- AVISO DE UTILIDAD ----------------------

* * * * INICIALIZAR UNICAMENTE CUANDO EMPLEADO YA ESTE CORRIENDO EN LA BASE DE DATOS.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CompraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompraApplication.class, args);
	}

}
