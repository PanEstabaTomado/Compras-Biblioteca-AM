package dfy1103.magaq.compra;

import dfy1103.magaq.compra.dto.CompraRequestDTO;
import dfy1103.magaq.compra.dto.CompraResponseDTO;
import dfy1103.magaq.compra.model.Compra;
import dfy1103.magaq.compra.repository.CompraRepository;
import dfy1103.magaq.compra.sevice.CompraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(classes = CompraService.class)
@ActiveProfiles("test")
@DisplayName("Tests Unitarios - CompraService")
class CompraServiceTest {

    @Autowired
    private CompraService compraService;

    @MockitoBean
    private CompraRepository compraRepository;

    @MockitoBean
    private WebClient webClient;

    // Mocks para la interfaz fluida de WebClient
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    private WebClient.ResponseSpec responseSpecMock;

    private final String mockToken = "Bearer token-valido";

    @BeforeEach
    void setUp() {
        requestHeadersUriSpecMock = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpecMock = Mockito.mock(WebClient.RequestHeadersSpec.class);
        responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientSuccess(WebClient webClientMock, String uri, Object id, String token) {
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri(eq(uri), eq(id))).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.header("Authorization", token)).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("OK"));
    }

    @SuppressWarnings("unchecked")
    private void mockWebClientException(WebClient webClientMock, String uri, Object id, String token, Throwable exception) {
        Mockito.when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        Mockito.when(requestHeadersUriSpecMock.uri(eq(uri), eq(id))).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.header("Authorization", token)).thenReturn(requestHeadersSpecMock);
        Mockito.when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        Mockito.when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.error(exception));
    }

    @Test
    @DisplayName("GIVEN: Existen compras WHEN: listarTodos THEN: Retorna la lista completa mapeada a DTO")
    void shouldReturnAllCompras() {
        List<Compra> mockList = Arrays.asList(
                new Compra(1L, 1001L, LocalDate.now(), 1000, 1190, "Utiles", 5L),
                new Compra(2L, 1002L, LocalDate.now(), 2000, 2380, "Libros", 6L)
        );
        Mockito.when(compraRepository.findAll()).thenReturn(mockList);

        List<CompraResponseDTO> resultado = compraService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getIdCompra());
        assertEquals("Utiles", resultado.get(0).getDetalles());
    }

    @Test
    @DisplayName("GIVEN: Existe compra WHEN: obtenerPorId THEN: Retorna el DTO correspondiente")
    void shouldReturnCompraById() {
        Long id = 1L;
        Compra compra = new Compra(id, 1001L, LocalDate.now(), 1000, 1190, "Utiles", 5L);
        Mockito.when(compraRepository.findById(id)).thenReturn(Optional.of(compra));

        Optional<CompraResponseDTO> resultado = compraService.obtenerPorId(id);

        assertTrue(resultado.isPresent());
        assertEquals(id, resultado.get().getIdCompra());
        assertEquals(1001L, resultado.get().getNro_factura());
    }

    @Test
    @DisplayName("GIVEN: Request y Token válidos WHEN: guardar THEN: Valida al empleado remotamente y guarda la compra")
    void shouldSaveCompraSuccessfully() {
        CompraRequestDTO request = new CompraRequestDTO(1001L, LocalDate.now(), 1000, 1190, "Utiles", 5L);
        Compra compraGuardada = new Compra(100L, 1001L, LocalDate.now(), 1000, 1190, "Utiles", 5L);

        mockWebClientSuccess(webClient, "/api/bibliotecaam/empleado/{id}", 5L, mockToken);
        Mockito.when(compraRepository.save(any(Compra.class))).thenReturn(compraGuardada);

        CompraResponseDTO resultado = compraService.guardar(request, mockToken);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getIdCompra());
        assertEquals(1190, resultado.getTotalConIva());
    }

    @Test
    @DisplayName("GIVEN: Empleado inexistente WHEN: guardar THEN: Lanza RuntimeException")
    void shouldThrowExceptionWhenEmpleadoNotFound() {
        CompraRequestDTO request = new CompraRequestDTO(1001L, LocalDate.now(), 1000, 1190, "Utiles", 99L);

        WebClientResponseException notFoundException = Mockito.mock(WebClientResponseException.NotFound.class);
        mockWebClientException(webClient, "/api/bibliotecaam/empleado/{id}", 99L, mockToken, notFoundException);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> compraService.guardar(request, mockToken));
        assertTrue(exception.getMessage().contains("El empleado/empleada con id 99 no existe"));
        Mockito.verify(compraRepository, Mockito.never()).save(any(Compra.class));
    }

    @Test
    @DisplayName("GIVEN: ID, Request y Token válidos WHEN: actualizar THEN: Modifica la compra existente")
    void shouldUpdateCompraSuccessfully() {
        Long id = 1L;
        Compra existente = new Compra(id, 1001L, LocalDate.now(), 1000, 1190, "Utiles Viejos", 5L);
        CompraRequestDTO request = new CompraRequestDTO(1001L, LocalDate.now(), 2000, 2380, "Utiles Nuevos", 5L);
        Compra modificado = new Compra(id, 1001L, LocalDate.now(), 2000, 2380, "Utiles Nuevos", 5L);

        Mockito.when(compraRepository.findById(id)).thenReturn(Optional.of(existente));
        mockWebClientSuccess(webClient, "/api/bibliotecaam/empleado/{id}", 5L, mockToken);
        Mockito.when(compraRepository.save(any(Compra.class))).thenReturn(modificado);

        Optional<CompraResponseDTO> resultado = compraService.actualizar(id, request, mockToken);

        assertTrue(resultado.isPresent());
        assertEquals("Utiles Nuevos", resultado.get().getDetalles());
        assertEquals(2380, resultado.get().getTotalConIva());
    }

    @Test
    @DisplayName("GIVEN: ID válido WHEN: eliminar THEN: Borra el registro en el repositorio")
    void shouldDeleteCompra() {
        Long id = 1L;
        Mockito.doNothing().when(compraRepository).deleteById(id);

        assertDoesNotThrow(() -> compraService.eliminar(id));
        Mockito.verify(compraRepository, Mockito.times(1)).deleteById(id);
    }

    @Test
    @DisplayName("GIVEN: ID de empleado WHEN: listarPorIdEmpleado THEN: Retorna las compras asociadas")
    void shouldReturnComprasByIdEmpleado() {
        Long idEmpleado = 5L;
        List<Compra> mockList = Arrays.asList(new Compra(1L, 1001L, LocalDate.now(), 1000, 1190, "Mobiliario", idEmpleado));
        Mockito.when(compraRepository.obtenerPorEmpleado(idEmpleado)).thenReturn(mockList);

        List<CompraResponseDTO> resultado = compraService.listarPorIdEmpleado(idEmpleado);

        assertEquals(1, resultado.size());
        assertEquals(idEmpleado, resultado.get(0).getIdEmpleado());
        assertEquals("Mobiliario", resultado.get(0).getDetalles());
    }
}