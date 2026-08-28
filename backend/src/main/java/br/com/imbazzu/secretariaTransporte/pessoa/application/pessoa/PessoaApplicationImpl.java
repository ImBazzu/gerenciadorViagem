package br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Cpf;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Telefone;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.domain.Pessoa;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.excecoes.PessoaDuplicadaException;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.excecoes.PessoaNaoEncontradaException;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.portas.PessoaApplicationPorta;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.portas.PessoaBancoPorta;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Endereco;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.valueObjects.Observacao;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Classe responsável pela lógica envolvendo com Paciente
 */
@Service
@Transactional
public class PessoaApplicationImpl implements PessoaApplicationPorta {

    private final PessoaBancoPorta banco;


    public PessoaApplicationImpl(PessoaBancoPorta banco) {
        this.banco = banco;
    }


    @Override
    public PessoaInfoOutputDto buscarPorId(UUID id) {
        return PessoaApplicationMapper.paraPessoaInfo(buscarEntidade(id));
    }
    /**
     * Salva no banco de dados um novo paciente
     * As informações são obtidas pelo o {@link PessoaRequestDto}
     *
     * @return O novo Paciente salvo no banco de dados
     */
    @Override
    public PessoaInfoOutputDto salvar(PessoaSalvarInputDto dto) {
        // Transforma o DTO em entidade
        var pessoa = Pessoa.criar(new Nome(dto.nome()),new Cpf(dto.cpf()),
                new Telefone(dto.telefone()),dto.idCondicao(),
                new Endereco(dto.endereco()),new Observacao(dto.observacao()));

        if (banco.verificarExistenciaCpf(pessoa.getCpf().valor())) {
            throw new PessoaDuplicadaException(pessoa.getCpf().valor());
        }
        var pessoaSalva =  banco.salvar(pessoa);
        return PessoaApplicationMapper.paraPessoaInfo(pessoaSalva);
    }

    /**
     * Edita um paciente no banco de dados.
     * <p>
     * Buscar um paciente pelo ‘Id’ usando o metodo @{buscarPorId}
     *
     * @return O paciente editado
     */
    @Override
    public PessoaInfoOutputDto editar(UUID id, PessoaSalvarInputDto dto) {
        var pessoa = buscarEntidade(id);
        if(banco.verificarExistenciaCpfIgnorandoId(pessoa.getCpf().valor(),id)){
            throw new PessoaDuplicadaException(pessoa.getCpf().valor());
        }
        pessoa.editar(new Nome(dto.nome()),new Cpf(dto.cpf()),new Telefone(dto.telefone()),
                dto.idCondicao(),new Endereco(dto.endereco()),new Observacao(dto.observacao()));

        banco.salvar(pessoa);
        return PessoaApplicationMapper.paraPessoaInfo(pessoa);
    }

    @Override
    public void arquivar(UUID idPessoa) {
        var pessoa = buscarEntidade(idPessoa);
        pessoa.arquivar();
        banco.salvar(pessoa);
    }

    @Override
    public void desarquivar(UUID idPessoa) {
        var  pessoa = buscarEntidade(idPessoa);
        pessoa.desarquivar();
        banco.salvar(pessoa);
    }

    @Transactional(readOnly = true)
    @Override
    public ResultadoPaginado<PessoaInfoOutputDto> listarPorNome(String nome, int pagina, int tamanho) {
        // Retorna a lista de todos os pacientes que possuem alguma compatibilidade com
        // o nome passado
        var listaRetorno = banco.buscarPorNome(nome, pagina, tamanho);
        return listaRetorno.map(PessoaApplicationMapper::paraPessoaInfo);
    }

    /**
     * Busca um paciente pelo seu identificador único "ID".
     * <p>
     * Caso nenhum paciente seja encontrado com o "ID" informado,
     * é lançada uma exceção {@link EntidadeNaoEncontradoException}.
     *
     * @param idPessoa o identificador único da pessoa a ser buscado.
     * @return o paciente correspondente ao "ID" informado.
     * @throws EntidadeNaoEncontradoException se nenhum paciente for encontrado com
     *                                        o "ID" fornecido.
     */
    @Transactional(readOnly = true)
    protected Pessoa buscarEntidade(UUID idPessoa) {
        // Busca o paciente através do "id"
        return banco.buscarPorId(idPessoa).orElseThrow(
                // Caso o "id" não seja encontrado retorne o erro
                () -> new PessoaNaoEncontradaException(idPessoa));
    }

}
