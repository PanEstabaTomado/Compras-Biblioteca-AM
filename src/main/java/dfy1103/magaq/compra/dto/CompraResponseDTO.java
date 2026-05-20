package dfy1103.magaq.compra.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraResponseDTO {

    private Long idCompra;

    private Long nro_factura;

    private LocalDate fechaComp;

    private Integer totaSinIva;

    private Integer totalConIva;

    private String detalles;

    private Long idEmpleado;
}
