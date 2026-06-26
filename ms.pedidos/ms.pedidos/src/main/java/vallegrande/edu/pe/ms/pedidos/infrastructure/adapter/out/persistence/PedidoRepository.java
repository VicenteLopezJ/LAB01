package vallegrande.edu.pe.ms.pedidos.infrastructure.adapter.out.persistence;

import vallegrande.edu.pe.ms.pedidos.domain.model.Pedido;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PedidoRepository extends ReactiveCrudRepository<Pedido, Long> {
}