package br.com.imbazzu.secretariaTransporte.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<UserDetails> findByNome(String usuario);

    boolean existsByNome(String usuario);
}