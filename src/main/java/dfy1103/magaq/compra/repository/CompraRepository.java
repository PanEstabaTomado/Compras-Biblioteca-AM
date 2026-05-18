package dfy1103.magaq.compra.repository;

import dfy1103.magaq.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query("SELECT c FROM Compra c WHERE c.idUsuario = :idEntregado ORDER BY c.idEntregado")
    List<Compra> obtenerPorUsuario(@Param("idEntregado") Long idEntregado);

}
