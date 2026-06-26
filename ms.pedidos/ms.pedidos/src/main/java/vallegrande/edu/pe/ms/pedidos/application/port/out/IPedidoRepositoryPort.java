package vallegrande.edu.pe.ms.pedidos.application.port.out;

import vallegrande.edu.pe.ms.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPedidoRepositoryPort {

    Flux<Pedido> findAll();
    Mono<Pedido> findById(Long id);
    Mono<Pedido> save(Pedido order);
}