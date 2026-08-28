package br.com.imbazzu.secretariaTransporte.trajeto.application.destino;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto.DestinoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.excecoes.DestinoDuplicadoException;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.excecoes.DestinoNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas.DestinoApplicationPort;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas.DestinoBancoPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DestinoApplicationImpl implements DestinoApplicationPort {


    private final DestinoBancoPort banco;

    public DestinoApplicationImpl(DestinoBancoPort banco) {
        this.banco = banco;
    }

    @Override
    public void arquivar(UUID idDestino){
        var destino =buscarEntidade(idDestino);
        destino.arquivar();
        banco.salvar(destino);
    }


    @Transactional(readOnly=true)
    @Override
    public DestinoInfoOutputDto buscarPorId(UUID idDestino) {
        return DestinoApplicationMapper.paraDestinoInfo(buscarEntidade(idDestino));
    }


    @Transactional(readOnly=true)
    @Override
    public ResultadoPaginado<DestinoInfoOutputDto> buscarPorNome(String destinoNome, int numPagina, int tamanhoPagina) {
        return banco.listar(destinoNome, numPagina, tamanhoPagina).map(DestinoApplicationMapper::paraDestinoInfo);
    }


    @Transactional(readOnly=true)
    @Override
    public ResultadoPaginado<DestinoInfoOutputDto> buscarPorNomeECidade(UUID idCidade, String destinoNome, int numPagina, int tamanhoPagina) {
        return banco.listarPorCidade(idCidade, destinoNome, numPagina, tamanhoPagina).map(DestinoApplicationMapper::paraDestinoInfo);
    }

    @Override
    public DestinoInfoOutputDto editar(UUID idDestino, String nomeDestino) {
        var destino = buscarEntidade(idDestino);
        var existeDestinoComNomeJaCadastrado = banco.verificarNomeEmDuplicidadeIgnorandoProprioDestino(destino.getIdCidade(),nomeDestino,destino.getId());
        if(existeDestinoComNomeJaCadastrado){
            throw new DestinoDuplicadoException(destino.getIdCidade(), nomeDestino);
        }
        destino.alterarNome(new Nome(nomeDestino));
        return DestinoApplicationMapper.paraDestinoInfo(banco.salvar(destino));
    }

    @Override
    public void desarquivar(UUID idDestino) {
        var destino = buscarEntidade(idDestino);
        destino.desarquivar();
        banco.salvar(destino);
    }

    @Override
    public DestinoInfoOutputDto salvar(UUID idCidade, String nomeDestino) {
        var existeDestinoComNomeJaCadastrado = banco.verificarNomeEmDuplicidade(idCidade,nomeDestino);
        if(existeDestinoComNomeJaCadastrado){
            throw new DestinoDuplicadoException(idCidade, nomeDestino);
        }
        var destino = Destino.criar(idCidade,new Nome(nomeDestino));
        return DestinoApplicationMapper.paraDestinoInfo(banco.salvar(destino));
    }

    @Transactional(readOnly=true)
    protected Destino buscarEntidade(UUID id) {
        return banco.buscarPorId(id).orElseThrow(
                ()->new DestinoNaoEncontradoException(id)
        );
    }
}
