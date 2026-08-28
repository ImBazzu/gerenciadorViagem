package br.com.imbazzu.secretariaTransporte.frota.application.motorista.query;

import br.com.imbazzu.secretariaTransporte.compartilhados.adapter.outbound.persistence.mapper.PageMapper;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.dto.MotoristaQueryDetalhadoInfoDto;
import br.com.imbazzu.secretariaTransporte.frota.core.excecoes.MotoristaNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.gateways.VeiculoGatewayPorta;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query.MotoristaApplicationQueryPorta;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query.MotoristaBancoQueryPorta;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly=true)
public class MotoristaApplicationQueryImpl implements MotoristaApplicationQueryPorta {

    private MotoristaBancoQueryPorta banco;

    private VeiculoGatewayPorta veiculoGateway;

    public MotoristaApplicationQueryImpl(MotoristaBancoQueryPorta banco) {
        this.banco = banco;
    }

    @Override
    public MotoristaQueryDetalhadoInfoDto buscarPorId(UUID id) {
        var motoristaDtoBase= banco.buscarMotorista(id).orElseThrow(
                ()->new MotoristaNaoEncontradoException(id)
        );
        var listaVeiculoDto = veiculoGateway.buscarPorIdMotorista(id);
        return new MotoristaQueryDetalhadoInfoDto(
                motoristaDtoBase.id(),motoristaDtoBase.nome(),motoristaDtoBase.telefone(),
                motoristaDtoBase.cpf(),
                listaVeiculoDto
        );
    }

    @Override
    public ResultadoPaginado<MotoristaQueryDetalhadoInfoDto> buscarPorNome(String nome, int  pagina, int tamanho) {
        var pageable =  PageRequest.of(pagina,tamanho);
        var resultPaginadoMotorista = PageMapper.toDomain(banco.buscarPorNome(nome,pageable));
        return resultPaginadoMotorista.map(motoristaBase->{
            var listaVeiculo = veiculoGateway.buscarPorIdMotorista(motoristaBase.id());
            return new MotoristaQueryDetalhadoInfoDto(
                    motoristaBase.id(),
                    motoristaBase.nome(),
                    motoristaBase.telefone(),
                    motoristaBase.cpf(),
                    listaVeiculo
            );
        });
    }
}
