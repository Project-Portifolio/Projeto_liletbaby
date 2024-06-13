package br.com.liletbaby.back_end.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Carrinho class.
 *
 * @author Wender Couto
 * @author Joao Abreu
 * @since 0.0.1-SNAPSHOT
 */

@Data
@Entity
@Table(name = "Carrinhos")
public class Carrinho {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    @ManyToOne
    private Produto productId;

    @OneToOne
    private Usuario userId;
}
