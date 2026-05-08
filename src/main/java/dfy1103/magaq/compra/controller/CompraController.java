package dfy1103.magaq.compra.controller;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.sevice.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecaam/compra")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<Optional<List<CompraResponseDTO>>> obtenerTodos(){
        return ResponseEntity.ok(compraService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Long id){
        return compraService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> guardar(@Valid @RequestBody CompraRequestDTO doto){
        return ResponseEntity.status(201).body(compraService.guardar(doto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO doto){
        return compraService.actualizar(id, doto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        if (compraService.obtenerPorId(id).isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            compraService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
    }
}
