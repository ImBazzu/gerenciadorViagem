package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.destino;

import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface DestinoBancoRepository extends JpaRepository<DestinoBanco, UUID> {
    @Query("""
            SELECT d FROM DestinoBanco d WHERE d.nome like CONCAT('%',:nome,'%')
                        AND d.cidadeId = :idCidade""")
    Page<Destino> listarPorCidade(UUID idCidade, String nome, Pageable pageable);

    @Query("SELECT d FROM DestinoBanco d WHERE d.nome like CONCAT('%',:nome,'%')")
    Page<Destino> listar(String nome,Pageable pageable);

    boolean existsByCidadeIdAndNome(UUID idCidade, String nome);

    boolean existsByCidadeIdAndNomeAndId(UUID cidadeId, String nome, UUID id);
}
