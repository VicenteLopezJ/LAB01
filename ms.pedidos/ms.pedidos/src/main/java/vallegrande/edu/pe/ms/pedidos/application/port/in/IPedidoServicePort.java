package vallegrande.edu.pe.ms.pedidos.application.port.in;

import vallegrande.edu.pe.ms.pedidos.domain.model.Pedido;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPedidoServicePort {

    Flux<Pedido> findAll();
    Mono<Pedido> findById(Long id);
    Mono<Pedido> create(Pedido order);
    Mono<Pedido> cancel(Long id);
}