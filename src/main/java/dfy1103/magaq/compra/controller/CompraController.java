package dfy1103.magaq.compra.controller;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.sevice.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Compras", description = "Operaciones asociadas a compras.")
public class CompraController {
    private final CompraService compraService;

    @GetMapping
    @Operation(summary = "Obtener todas las compras", description = "Obtiene una lista de todas las compras.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    public ResponseEntity<List<CompraResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(compraService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener compras por id", description = "Obtiene compras por id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    public ResponseEntity<CompraResponseDTO> buscarPorId(@PathVariable Long id){
        return compraService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/porusuario/{idEmpleado}")
    @Operation(summary = "Obtener compras por id de empleado", description = "Obtiene compras por id de empleado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operacion exitosa"),
            @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    public ResponseEntity<List<CompraResponseDTO>> listarPorIdUsuario(@PathVariable Long idEmpleado){
        return ResponseEntity.ok(compraService.listarPorIdEmpleado(idEmpleado));
    }

    @PostMapping
    @Operation(summary = "Guardar una compra", description = "Guarda una compra acorde a lo ingresado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa."),
            @ApiResponse(responseCode = "400", description = "Error al ingresar parametros. Revise si ingreso todos los parametros solicitados."),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para hacer el cambio.")
    })
    public ResponseEntity<CompraResponseDTO> guardar(@Valid @RequestBody CompraRequestDTO doto, @RequestHeader("Authorization") String token){
        return ResponseEntity.status(201).body(compraService.guardar(doto, token));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar compra", description = "Actualiza una compra acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compra actualizada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Compra.class))),
            @ApiResponse(responseCode = "404", description = "El id de la compra no existe.")
    })
    public ResponseEntity<CompraResponseDTO> actualizar(@PathVariable Long id, @Valid @RequestBody CompraRequestDTO doto, @RequestHeader("Authorization") String token){
        return compraService.actualizar(id, doto, token)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar compra", description = "Elimina una compra acorde a una id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "¡Compra eliminada con exito!"),
            @ApiResponse(responseCode = "404",description = "ERROR: ¡El id de la compra ingresada no existe!")
    })
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
