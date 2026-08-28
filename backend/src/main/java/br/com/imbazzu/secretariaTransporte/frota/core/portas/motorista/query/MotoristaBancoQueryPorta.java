package br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query;

import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MotoristaBancoQueryPorta {

    Optional<MotoristaQueryBaseInfoDto> buscarMotorista(UUID id);

    Page<MotoristaQueryBaseInfoDto> buscarPorNome(String nome, Pageable pageable);
}
