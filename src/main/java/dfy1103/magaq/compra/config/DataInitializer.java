package dfy1103.magaq.compra.config;

import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CompraRepository compraRepository;

    @Override
    public void run(String... args){
        if (compraRepository.count()>0){
            log.info(">>> DataInitializer: la BD ya tiene datos, se omite la carga inicial.");
            return;
        }

        log.info(">>> DataInitializer: BD vacía detectada, insertando datos de prueba...");

        compraRepository.save(new Compra(null,352356L, LocalDate.of(2010,7,10),100000,119000,"El cliente olia raro.",1L));
        compraRepository.save(new Compra(null,654331L, LocalDate.of(2009,5,9),200000,238000,"El cliente era de 4chan.",2L));

    }
}
