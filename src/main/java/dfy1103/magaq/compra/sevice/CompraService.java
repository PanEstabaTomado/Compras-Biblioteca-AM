package dfy1103.magaq.compra.sevice;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {
    private final CompraRepository compraRepository;
    private final WebClient webClient;

    private CompraResponseDTO mapToDOTO(Compra compra){
        return new CompraResponseDTO(
                compra.getIdComp(),
                compra.getNro_factura(),
                compra.getFechaComp(),
                compra.getTotaSinIva(),
                compra.getTotalConIva(),
                compra.getDetalles(),
                compra.getIdEmpleado()
        );
    }

    /*
    -------------------------VALIDANDO EL ID EMPLEADO -----------------------
     */
    private void validarEmpleado(Long empleadoId, String token) {
        try {
            webClient.get()
                    .uri("/api/bibliotecaam/empleados/{id}", empleadoId)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info(">>> Empleado {} validado correctamente (WebClient) id:", empleadoId);

        } catch (WebClientResponseException.NotFound e) {
            throw new RuntimeException(
                    "El empleado/empleada con id " + empleadoId + " no existe en la Base de Datos de Empleado.");
        } catch (Exception e) {
            throw new RuntimeException(
                    "No se puede conectar con Empleado: " + e.getMessage());
        }
    }

    /*
    ------------------------------- C R U D ------------------------------------
     */

    public Optional<List<CompraResponseDTO>> listarTodos(){
        return Optional.of(compraRepository.findAll()
                .stream()
                .map(this::mapToDOTO)
                .collect(Collectors.toList()));
    }

    public Optional<CompraResponseDTO> obtenerPorId(Long id){
        return compraRepository.findById(id)
                .map(this::mapToDOTO);

    }

    public CompraResponseDTO guardar(CompraRequestDTO doto, String token){
        validarEmpleado(doto.getIdEmpleado(), token);
        Compra compra = new Compra(
                null,
                doto.getNro_factura(),
                doto.getFechaComp(),
                doto.getTotaSinIva(),
                doto.getTotalConIva(),
                doto.getDetalles(),
                doto.getIdEmpleado()
        );
        return mapToDOTO(compraRepository.save(compra));
    }

    public Optional<CompraResponseDTO> actualizar(Long id, CompraRequestDTO doto, String token){
        return compraRepository.findById(id).map(existente ->
        {
            validarEmpleado(doto.getIdEmpleado(), token);
            existente.setNro_factura(doto.getNro_factura());
            existente.setFechaComp(doto.getFechaComp());
            existente.setTotaSinIva(doto.getTotaSinIva());
            existente.setTotalConIva(doto.getTotalConIva());
            existente.setDetalles(doto.getDetalles());
            existente.setIdEmpleado(doto.getIdEmpleado());
            return mapToDOTO(compraRepository.save(existente));
        });
    }

    public void eliminar(Long id){
        compraRepository.deleteById(id);
    }

    /*
    -------- FUNCIONES EXTRAS ------------
     */

    public List<CompraResponseDTO> listarPorIdUsuario(Long idUsuario){
        return compraRepository.obtenerPorUsuario(idUsuario)
                .stream()
                .map(this::mapToDOTO)
                .collect(Collectors.toList());
    }


}
