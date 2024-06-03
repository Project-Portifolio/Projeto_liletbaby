package br.com.liletbaby.back_end.repository;

import br.com.liletbaby.back_end.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepositorio extends JpaRepository<Produto, String> {
    List<Produto> findAll();
}
