package dfy1103.magaq.compra;

import dfy1103.magaq.compra.controller.CompraController;
import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.sevice.CompraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompraController.class)
@ActiveProfiles("test")
@DisplayName("Tests Unitarios - CompraController")
class CompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private CompraService compraService;

    private final String mockToken = "Bearer token-de-prueba";

    @Test
    @DisplayName("GIVEN: Existen compras WHEN: GET /api/bibliotecaam/compra THEN: Retorna 200 OK y la lista de compras")
    void shouldReturnTodasLasCompras() throws Exception {
        CompraResponseDTO c1 = new CompraResponseDTO(1L, 10001L, LocalDate.now(), 5000, 5950, "Detalle 1", 10L);
        CompraResponseDTO c2 = new CompraResponseDTO(2L, 10002L, LocalDate.now(), 8000, 9520, "Detalle 2", 11L);
        List<CompraResponseDTO> lista = Arrays.asList(c1, c2);

        Mockito.when(compraService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/bibliotecaam/compra")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idCompra").value(1L))
                .andExpect(jsonPath("$[0].nro_factura").value(10001L))
                .andExpect(jsonPath("$[1].idEmpleado").value(11L));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: GET /api/bibliotecaam/compra/{id} THEN: Retorna 200 OK y la compra")
    void shouldReturnCompraById() throws Exception {
        Long id = 1L;
        CompraResponseDTO mockResponse = new CompraResponseDTO(id, 10001L, LocalDate.now(), 5000, 5950, "Detalle 1", 10L);

        Mockito.when(compraService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(get("/api/bibliotecaam/compra/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCompra").value(id))
                .andExpect(jsonPath("$.detalles").value("Detalle 1"));
    }

    @Test
    @DisplayName("GIVEN: ID inexistente WHEN: GET /api/bibliotecaam/compra/{id} THEN: Retorna 404 Not Found")
    void shouldReturnNotFoundWhenCompraDoesNotExist() throws Exception {
        Long id = 99L;
        Mockito.when(compraService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bibliotecaam/compra/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN: ID de empleado WHEN: GET /api/bibliotecaam/compra/porusuario/{idEmpleado} THEN: Retorna lista de compras")
    void shouldReturnComprasByIdEmpleado() throws Exception {
        Long idEmpleado = 10L;
        CompraResponseDTO c1 = new CompraResponseDTO(1L, 10001L, LocalDate.now(), 5000, 5950, "Detalle 1", idEmpleado);
        Mockito.when(compraService.listarPorIdEmpleado(idEmpleado)).thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/api/bibliotecaam/compra/porusuario/{idEmpleado}", idEmpleado)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idEmpleado").value(idEmpleado));
    }

    @Test
    @DisplayName("GIVEN: Request y Token válidos WHEN: POST /api/bibliotecaam/compra THEN: Retorna 21 Created")
    void shouldCreateCompra() throws Exception {
        CompraRequestDTO request = new CompraRequestDTO(10001L, LocalDate.now(), 5000, 5950, "Detalle 1", 10L);
        CompraResponseDTO mockResponse = new CompraResponseDTO(1L, 10001L, LocalDate.now(), 5000, 5950, "Detalle 1", 10L);

        Mockito.when(compraService.guardar(any(CompraRequestDTO.class), eq(mockToken))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/bibliotecaam/compra")
                        .header(HttpHeaders.AUTHORIZATION, mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCompra").value(1L))
                .andExpect(jsonPath("$.totalConIva").value(5950));
    }

    @Test
    @DisplayName("GIVEN: ID, Request y Token válidos WHEN: PUT /api/bibliotecaam/compra/{id} THEN: Retorna 200 OK")
    void shouldUpdateCompra() throws Exception {
        Long id = 1L;
        CompraRequestDTO request = new CompraRequestDTO(10001L, LocalDate.now(), 6000, 7140, "Detalle Modificado", 10L);
        CompraResponseDTO mockResponse = new CompraResponseDTO(id, 10001L, LocalDate.now(), 6000, 7140, "Detalle Modificado", 10L);

        Mockito.when(compraService.actualizar(eq(id), any(CompraRequestDTO.class), eq(mockToken))).thenReturn(Optional.of(mockResponse));

        mockMvc.perform(put("/api/bibliotecaam/compra/{id}", id)
                        .header(HttpHeaders.AUTHORIZATION, mockToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.detalles").value("Detalle Modificado"))
                .andExpect(jsonPath("$.totaSinIva").value(6000));
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: DELETE /api/bibliotecaam/compra/{id} THEN: Retorna 204 No Content con mensaje de éxito")
    void shouldDeleteCompraSuccessfully() throws Exception {
        Long id = 1L;
        CompraResponseDTO mockResponse = new CompraResponseDTO(id, 10001L, LocalDate.now(), 5000, 5950, "Detalle 1", 10L);

        Mockito.when(compraService.obtenerPorId(id)).thenReturn(Optional.of(mockResponse));
        Mockito.doNothing().when(compraService).eliminar(id);

        mockMvc.perform(delete("/api/bibliotecaam/compra/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.['¡EXITO! ']").value("¡La compra fue eliminada con exito!"));
    }

    @Test
    @DisplayName("GIVEN: ID inexistente WHEN: DELETE /api/bibliotecaam/compra/{id} THEN: Retorna 204 No Content con mensaje de error")
    void shouldReturnNoContentWithErrorWhenDeletingNonExistentCompra() throws Exception {
        Long id = 99L;
        Mockito.when(compraService.obtenerPorId(id)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bibliotecaam/compra/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.['¡ERROR! ']").value("¡La compra con id 99 no fue encontrada!"));
    }
}