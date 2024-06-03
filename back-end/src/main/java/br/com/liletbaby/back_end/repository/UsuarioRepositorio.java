package br.com.liletbaby.back_end.repository;

import br.com.liletbaby.back_end.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    List<Usuario> findAll(); //Apenas provisorio.
}
