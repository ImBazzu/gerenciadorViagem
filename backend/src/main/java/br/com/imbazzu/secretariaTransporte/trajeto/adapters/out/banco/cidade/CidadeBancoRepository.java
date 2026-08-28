package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.CidadeEstadoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

/**
 * Integração com o banco de dados
 */
public interface CidadeBancoRepository extends JpaRepository<CidadeBanco, UUID> {

    /**
     * Procurado no banco de dados pelo nome
     * @param nome nome procurado
     * @param pageable config da pagina
     * @return retorna a lista da correspondente da cidade
     */
    @Query("""
        SELECT c FROM CidadeBanco c WHERE c.nome LIKE CONCAT('%',:nome,'%')""")
    Page<CidadeBanco> findByNome(@Param("nome") String nome, Pageable pageable);


    boolean existsByNomeAndEstadoAndIdNot(String nome, CidadeEstadoEnum estado, UUID id);

    boolean existsByNomeAndEstado(String nome, CidadeEstadoEnum estado);
}
