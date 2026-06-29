package pe.edu.vallegrande.mspedidos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido {
    @Id
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double total;
    
    @org.springframework.data.annotation.Transient
    private Double price;
    private String status;
    
    @org.springframework.data.annotation.Transient
    private LocalDateTime fecha;


}
