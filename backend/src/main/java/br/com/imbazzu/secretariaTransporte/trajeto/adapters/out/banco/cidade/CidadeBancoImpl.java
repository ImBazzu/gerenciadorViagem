package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.CidadeEstadoEnum;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas.CidadeBancoPort;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura.BancoDeDadosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.adapter.outbound.persistence.mapper.PageMapper;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


//Adaptador da porta de persistência
@Repository
public class CidadeBancoImpl implements CidadeBancoPort {


    //Spring Jpa
    private final CidadeBancoRepository repo;

    //Construtor
    public CidadeBancoImpl(CidadeBancoRepository cidadeBancoRepository) {
        //inserindo o Jpa
        this.repo= cidadeBancoRepository;
    }

    /**
     * Salva a cidade no banco de dados
     * @param cidade cidade a ser salva
     * @return Cidade Salva
     */
    @Override
    public Cidade salvar(Cidade cidade) {
        //Converte o Model para o JpaEntity
        var cidadeJpa = CidadeBancoMapper.paraEntityCidade(cidade);
        try{
            //Salva e verifica se o banco lança algum erro
            cidadeJpa= repo.saveAndFlush(cidadeJpa);
            //Converte a JpaEntity para o Model
            return CidadeBancoMapper.paraDomainCidade(cidadeJpa);
        }catch(DataIntegrityViolationException ex){

            //Exceção para caso a exceção do Spring seja qualquer outra
            throw new BancoDeDadosException("Não foi possível salvar a cidade no banco de dados.",ex);
            }
        }



    /**
     * Busca a cidade pelo identificador
     * @param id identificador
     * @return cidade encontrada
     */
    @Override
    public Optional<Cidade> buscarPorId(UUID id) {
        //procura no banco a cidade
        return repo.findById(id).map(CidadeBancoMapper::paraDomainCidade);
    }

    /**
     * Realiza a procura paginada por nome
     * @param nome nome da cidade
     * @param pagina pagina a ser procurada
     * @param tamanho quantidade de itens em cada página
     * @return lista paginada de cidade compatível com a busca
     */
    @Override
    public ResultadoPaginado<Cidade> listar(String nome, int pagina, int tamanho) {
        //Cria o objeto de paginação
        var pageable = PageRequest.of(pagina, tamanho);

        var pageCidade =
                //procura a cidade com o objeto paginado
                repo.findByNome(nome, pageable)
                        //Mapeia o JpaEntity para o Model
                        .map(CidadeBancoMapper::paraDomainCidade);
        //retorna a lista da cidade paginada
        return PageMapper.toDomain(pageCidade);
    }



    @Override
    public boolean verificarCidadeCadastrada(String nome, CidadeEstadoEnum estado) {
        return repo.existsByNomeAndEstado(nome,estado);
    }

    @Override
    public boolean verificarCidadeCadastradaIgnorandoId(UUID idCidade, String nome, CidadeEstadoEnum estado) {
        return repo.existsByNomeAndEstadoAndIdNot(nome,estado,idCidade);
    }
}
