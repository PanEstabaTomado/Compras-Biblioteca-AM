package dfy1103.magaq.compra.sevice;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.repository.CompraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {
    private final CompraRepository compraRepository;

    private CompraResponseDTO mapToDOTO(Compra compra){
        return new CompraResponseDTO(
                compra.getIdComp(),
                compra.getNro_factura(),
                compra.getFechaComp(),
                compra.getTotaSinIva(),
                compra.getTotalConIva(),
                compra.getDetalles()
        );
    }

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

    public CompraResponseDTO guardar(CompraRequestDTO doto){
        Compra compra = new Compra(
                null,
                doto.getNro_factura(),
                doto.getFechaComp(),
                doto.getTotaSinIva(),
                doto.getTotalConIva(),
                doto.getDetalles()
        );
        return mapToDOTO(compraRepository.save(compra));
    }

    public Optional<CompraResponseDTO> actualizar(Long id, CompraRequestDTO doto){
        return compraRepository.findById(id).map(existente ->
        {
            existente.setNro_factura(doto.getNro_factura());
            existente.setFechaComp(doto.getFechaComp());
            existente.setTotaSinIva(doto.getTotaSinIva());
            existente.setTotalConIva(doto.getTotalConIva());
            existente.setDetalles(doto.getDetalles());
            return mapToDOTO(compraRepository.save(existente));
        });
    }

    public void eliminar(Long id){
        compraRepository.deleteById(id);
    }
}
