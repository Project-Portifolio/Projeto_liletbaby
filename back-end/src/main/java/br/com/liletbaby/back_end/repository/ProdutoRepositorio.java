package br.com.liletbaby.back_end.repository;

import br.com.liletbaby.back_end.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ProdutoRepositorio class.
 *
 * @author Wender Couto
 * @author João Abreu
 * @since 0.0.1-SNAPSHOT
 */

public interface ProdutoRepositorio extends JpaRepository<Produto, String> {
    List<Produto> findAll();
}