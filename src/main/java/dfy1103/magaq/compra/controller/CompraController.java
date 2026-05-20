package dfy1103.magaq.compra.controller;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.sevice.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bibliotecaam/compra")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<List<CompraResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(compraService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Long id){
        return compraService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/porusuario/{idEmpleado}")
    public ResponseEntity<List<CompraResponseDTO>> listarPorIdUsuario(@PathVariable Long idEmpleado){
        return ResponseEntity.ok(compraService.listarPorIdEmpleado(idEmpleado));
    }

    @PostMapping
    public ResponseEntity<CompraResponseDTO> guardar(@Valid @RequestBody CompraRequestDTO doto, @RequestHeader("Authorization") String token){
        return ResponseEntity.status(201).body(compraService.guardar(doto, token));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO doto, @RequestHeader("Authorization") String token){
        return compraService.actualizar(id, doto, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> eliminar(@PathVariable Long id){
        if (compraService.obtenerPorId(id).isEmpty()){
            Map<String, String> borrado = new LinkedHashMap<>();
            borrado.put("¡ERROR! ", "¡La compra con id "+id+" no fue encontrada!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(borrado);
        }else {
            compraService.eliminar(id);
            Map<String, String> borrado = new LinkedHashMap<>();
            borrado.put("¡EXITO! ", "¡La compra fue eliminada con exito!");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(borrado);
        }
    }
}
