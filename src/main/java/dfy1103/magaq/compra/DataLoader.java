package dfy1103.magaq.compra;

import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.repository.CompraRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private CompraRepository compraRepository;

    @Override
    public void run (String... args) throws Exception{
        Faker faker = new Faker();

        for (int i = 0; i < 6; i++) {
            Compra compra = new Compra();
            compra.setNro_factura((long) faker.number().numberBetween(1,9999999));
            compra.setFechaComp(faker.timeAndDate().birthday());
            compra.setTotaSinIva(faker.number().numberBetween(1,999999));
            compra.setTotalConIva(faker.number().numberBetween(1,999999));
            compra.setDetalles(faker.lorem().sentence());
            compra.setIdEmpleado((long)faker.number().numberBetween(1,3));

            compraRepository.save(compra);
        }
    }
}
