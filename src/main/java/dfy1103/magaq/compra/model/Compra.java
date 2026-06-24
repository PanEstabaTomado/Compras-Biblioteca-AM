package dfy1103.magaq.compra.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCompra;

    @Column(nullable = false,scale = 10)
    private Long nro_factura;

    @Column(nullable = false)
    private LocalDate fechaComp;

    @Column(nullable = false, scale = 7)
    private Integer totaSinIva;

    @Column(nullable = false, scale = 7)
    private Integer totalConIva;

    @Column(nullable = false, length = 200)
    private String detalles;

    @Column(nullable = false)
    private Long idEmpleado;
}
