package br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoaCondicao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PessoaCondicaoRepository extends JpaRepository<PessoaCondicaoBanco, UUID> {

    @Query("Select p FROM PessoaCondicaoBanco p WHERE p.nome LIKE CONCAT('%',:nome,'%')")
    Page<PessoaCondicaoBanco> buscarPorNome(String nome, Pageable pageable);

    boolean existsByNome(String nome);


    boolean existsByNomeAndIdIgnoreCase(String nome, UUID id);
}
