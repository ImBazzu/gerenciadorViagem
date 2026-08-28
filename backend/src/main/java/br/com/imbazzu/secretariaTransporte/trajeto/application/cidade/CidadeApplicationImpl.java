package br.com.imbazzu.secretariaTransporte.trajeto.application.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.valueObject.TempoViagem;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.excecoes.CidadeDuplicadaException;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.excecoes.CidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas.CidadeApplicationPort;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas.CidadeBancoPort;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Responsável pela toda a lógica envolvendo a cidades
 */
@Transactional
@Service
public class CidadeApplicationImpl implements CidadeApplicationPort {


    //Responsável pelo metodo de integração com o banco de dados
    private final CidadeBancoPort banco;

    public CidadeApplicationImpl(CidadeBancoPort repo) {
        this.banco = repo;
    }

    /**
     * Apaga do banco de dados a cidade cadastrada
     * @param id identificador da cidade
     */

    @Override
    public void arquivar(UUID id){
        //Busca a cidade
        var cidade = buscarEntidade(id);
        cidade.arquivar();
        banco.salvar(cidade);
    }


    /**
     * Busca a cidade através do nome
     * @param nome nome procurado
     * @param pagina numero da pagina
     * @return lista das cidades com o nome correspondente
     */
    @Transactional(readOnly = true)
    @Override
    public ResultadoPaginado<CidadeInfoOutputDto>buscarPorNome(String nome, int pagina, int tamanho) {
        //Procura a cidade através do nome
        var result= banco.listar(nome, pagina, tamanho);
        return result.map(CidadeApplicationMapper::paraCidadeInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public CidadeInfoOutputDto buscarPorId(UUID id) {
        var cidade = buscarEntidade(id);
        return CidadeApplicationMapper.paraCidadeInfo(cidade);
    }

    /**
     * Edita o acesso à cidade
     * @param id identificador
     */
    @Override
    public void desarquivar(UUID id) {
        //Busca a entidade através do id
        var cidade = buscarEntidade(id);
        cidade.desarquivar();
        banco.salvar(cidade);
    }

    /**
     * Atualizar informações da cidade cadastrada
     * @param dto novos dados
     * @return cidade Atualizada
     */
    @Override
    public CidadeInfoOutputDto editar(UUID idCidade, CidadeSalvarInputDto dto){
        //Pre-checagem para a evitar duplicidade
        if(banco.verificarCidadeCadastradaIgnorandoId(idCidade,dto.nome(),dto.estado())){
            //lança erro de duplicidade
            throw new CidadeDuplicadaException(dto.nome(), dto.estado().name());
        }
        //Busca a entidade através do 'Id'
        var cidade = buscarEntidade(idCidade);
        //Atualiza a entidade com as novas informações
        cidade.editar(new Nome(dto.nome()),dto.estado(),new TempoViagem(dto.horas(),dto.minutos()));
        //Salva a entidade no banco de dados
        var cidadeSalva = banco.salvar(cidade);
        return CidadeApplicationMapper.paraCidadeInfo(cidadeSalva);
    }


    /**
     * Cadastra uma nova cidade no banco de dados
     * @param dto dados da nova cidade
     * @return dto correspondente da nova cidade salvo
     */
    @Transactional
    @Override
    public CidadeInfoOutputDto salvar(CidadeSalvarInputDto dto){
        //Pre-checagem para a evitar duplicidade
        if(banco.verificarCidadeCadastrada(dto.nome(),dto.estado())){
            //lança erro de duplicidade
            throw new CidadeDuplicadaException(dto.nome(), dto.estado().name());
        }
        //converte o dto em entidade
        var cidade = CidadeApplicationMapper.paraDominio(dto);
        //Salva a entidade no banco de dados e retorna a entidade atualizada
        var cidadeSalva = banco.salvar(cidade);

        return CidadeApplicationMapper.paraCidadeInfo(cidadeSalva);
    }

//====================
//Métodos privados
// ====================
    /**
     * Busca cidade pelo identificador
     * @param id identificador
     * @return Cidade encontrada no banco de dados
     */
    @Transactional(readOnly = true)
    protected Cidade buscarEntidade(UUID id){
        return banco.buscarPorId(id).orElseThrow(
                ()->new CidadeNaoEncontradoException(id)
        );
    }
}
