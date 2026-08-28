package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.query;

import br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.MotoristaBanco;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;
import java.util.UUID;

/**
 * Responsável interagir com o banco de dados
 */
public interface MotoristaBancoQueryRepository extends JpaRepository<MotoristaBanco, UUID> {

    @Query("""
    select new br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto(
        m.id, m.nome,m.telefone,m.cpf) from MotoristaBanco m where m.id =: id
""")
    Optional<MotoristaQueryBaseInfoDto> buscarPorId(@Param("id") UUID id);


    @Query("""
    select new br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto(
        m.id, m.nome,m.telefone,m.cpf) from MotoristaBanco m where m.nome like concat('%',:nome,'%')
""")
    Page<MotoristaQueryBaseInfoDto> buscarPorNome(@Param("nome") String nome, Pageable pageable);
}
