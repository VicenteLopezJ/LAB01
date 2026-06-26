package vallegrande.edu.pe.ms.pedidos.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Component;
import vallegrande.edu.pe.ms.pedidos.application.port.out.IPedidoRepositoryPort;
import vallegrande.edu.pe.ms.pedidos.domain.model.Pedido;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PedidoRepositoryAdapter implements IPedidoRepositoryPort {

    private final PedidoRepository repository;

    @Override
    public Flux<Pedido> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Pedido> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Pedido> save(Pedido order) {
        return repository.save(order);
    }
}