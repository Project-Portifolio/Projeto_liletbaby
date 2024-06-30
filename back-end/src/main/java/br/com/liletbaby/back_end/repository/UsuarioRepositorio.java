package br.com.liletbaby.back_end.repository;

import br.com.liletbaby.back_end.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * UsuarioRepositorio class.
 *
 * @author Wender Couto
 * @author João Abreu
 * @since 0.0.1-SNAPSHOT
 */

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    Usuario findByUserId (Integer userId);

    Usuario findByName(String username);

    Usuario findByEmail(String email);
}