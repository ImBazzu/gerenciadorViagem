package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.query;

import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryBaseInfoDto;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query.MotoristaBancoQueryPorta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MotoristaBancoQueryImpl implements MotoristaBancoQueryPorta {

    private MotoristaBancoQueryRepository banco;

    @Override
    public Optional<MotoristaQueryBaseInfoDto> buscarMotorista(UUID id) {
        return banco.buscarPorId(id);
    }

    @Override
    public Page<MotoristaQueryBaseInfoDto> buscarPorNome(String nome, Pageable pageable) {
        return banco.buscarPorNome(nome, pageable);
    }
}
