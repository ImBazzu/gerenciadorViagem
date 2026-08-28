package br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PessoaBancoRepository extends JpaRepository<PessoaBanco, UUID> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdIgnoreCase(String cpf, UUID idPessoa);

    @Query("SELECT p FROM PessoaBanco p WHERE p.nome like CONCAT('%',:nome,'%')")
    Page<PessoaBanco> findByNome(String nome, Pageable pageable);
}
