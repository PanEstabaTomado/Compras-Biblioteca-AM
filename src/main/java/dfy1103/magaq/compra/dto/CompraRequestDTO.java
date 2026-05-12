package dfy1103.magaq.compra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraRequestDTO {

    @NotNull(message = "El numero de factura no debe estar vacio.")
    private Long nro_factura;

    @NotNull(message = "La fecha de compra no puede estar vacia.")
    private LocalDate fechaComp;

    @NotNull(message = "El total sin iva debe mostrarse obligatoriamente.")
    private Integer totaSinIva;

    @NotNull(message = "El total con IVA debe mostrarse obligatoriamente.")
    private Integer totalConIva;

    @NotBlank(message = "Los detalles de compra no pueden estar vacios.")
    private String detalles;

    @NotNull(message = "ERROR: El idEmpleado no puede estar vacio.")
    private Long idEmpleado;
}
