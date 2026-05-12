package dfy1103.magaq.compra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
---------------------- AVISO DE UTILIDAD ----------------------

* * * * INICIALIZAR UNICAMENTE CUANDO EMPLEADO YA ESTE CORRIENDO EN LA BASE DE DATOS.
 */
@SpringBootApplication
public class CompraApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompraApplication.class, args);
	}

}
