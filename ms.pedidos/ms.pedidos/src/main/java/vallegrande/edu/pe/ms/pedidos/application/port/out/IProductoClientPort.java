package vallegrande.edu.pe.ms.pedidos.application.port.out;

import vallegrande.edu.pe.ms.pedidos.domain.model.Producto;
import reactor.core.publisher.Mono;

public interface IProductoClientPort {

    Mono<Producto> findById(Long id);
    Mono<Producto> decreaseStock(Long id, Integer quantity);
}