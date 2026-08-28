package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.destino;

import br.com.imbazzu.secretariaTransporte.compartilhados.adapter.outbound.persistence.mapper.PageMapper;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura.BancoDeDadosException;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas.DestinoBancoPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public class DestinoBancoImpl implements DestinoBancoPort {

    private final DestinoBancoRepository destinoBancoRepository;

    public DestinoBancoImpl(DestinoBancoRepository destinoBancoRepository) {
        this.destinoBancoRepository = destinoBancoRepository;
    }

    @Override
    public Destino salvar(Destino destino) {
        var destinoJpa = DestinoBancoMapper.paraEntity(destino);
        try{
            var destinoSalvo = destinoBancoRepository.saveAndFlush(destinoJpa);
            return DestinoBancoMapper.paraDomain(destinoSalvo);
        }catch(Exception e){
            throw new BancoDeDadosException("Não foi possível salvar o destino no banco de dados", e);
        }
    }


    @Override
    public Optional<Destino> buscarPorId(UUID idDestino) {
        return destinoBancoRepository.findById(idDestino).map(DestinoBancoMapper::paraDomain);

    }

    @Override
    public ResultadoPaginado<Destino> listar(String nome,  int pagina, int tamanho) {
        var pageable = PageRequest.of(pagina, tamanho);
        var paginaDestino = destinoBancoRepository.listar(nome, pageable);
        return PageMapper.toDomain(paginaDestino);
    }

    @Override
    public boolean verificarNomeEmDuplicidade(UUID idCidade, String nome) {
        return destinoBancoRepository.existsByCidadeIdAndNome(idCidade,nome);
    }

    @Override
    public boolean verificarNomeEmDuplicidadeIgnorandoProprioDestino(UUID idCidade, String nome, UUID idDestino) {
        return destinoBancoRepository.existsByCidadeIdAndNomeAndId(idCidade,nome,idDestino);
    }

    @Override
    public ResultadoPaginado<Destino> listarPorCidade(UUID idCidade, String nome, int pagina, int tamanho) {
        var  pageable = PageRequest.of(pagina, tamanho);
        var paginaDestino = destinoBancoRepository.listarPorCidade(idCidade,nome,pageable);
        return PageMapper.toDomain(paginaDestino);
    }

}
