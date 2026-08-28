package br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.domain.PessoaCondicao;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.excecoes.PessoaCondicaoDuplicadaException;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.excecoes.PessoaCondicaoNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.portas.PessoaCondicaoApplicationPorta;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.portas.PessoaCondicaoBancoPorta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PessoaCondicaoApplication implements PessoaCondicaoApplicationPorta {

    private final PessoaCondicaoBancoPorta banco;

    public PessoaCondicaoApplication(PessoaCondicaoBancoPorta banco) {
        this.banco = banco;
    }



    @Transactional(readOnly = true)
    @Override
    public PessoaCondicaoInfoOutputDto buscarPorId(UUID id){
        return PessoaCondicaoApplicationMapper.paraInfoDto(buscarEntidade(id));
    }

    @Override
    public PessoaCondicaoInfoOutputDto salvar(PessoaCondicaoSalvarInputDto dto) {
        var pessoaCondicao = PessoaCondicao.criar(new Nome(dto.nome()));

        if (banco.existePorNome(pessoaCondicao.getNome().valor())) {
            throw new PessoaCondicaoDuplicadaException(pessoaCondicao.getNome().valor());
        }
        var condicaoSalva = banco.salvar(pessoaCondicao);
        return PessoaCondicaoApplicationMapper.paraInfoDto(condicaoSalva);
    }


    @Override
    public PessoaCondicaoInfoOutputDto editar(UUID id, PessoaCondicaoSalvarInputDto dto) {
        var pessoaCondicao = buscarEntidade(id);

        pessoaCondicao.editar(new Nome(dto.nome()));

        if(banco.existePorNomeIgnorandoId(pessoaCondicao.getNome().valor(), pessoaCondicao.getId())) {
            throw new PessoaCondicaoDuplicadaException(pessoaCondicao.getNome().valor());
        }
        banco.salvar(pessoaCondicao);
        return PessoaCondicaoApplicationMapper.paraInfoDto(pessoaCondicao);
    }

    @Override
    public void arquivar(UUID idPessoa) {
        var entidade =  buscarEntidade(idPessoa);
        entidade.arquivar();
        banco.salvar(entidade);
    }

    @Override
    public void desarquivar(UUID idPessoa) {
        var entidade =   buscarEntidade(idPessoa);
        entidade.desarquivar();
        banco.salvar(entidade);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoPaginado<PessoaCondicaoInfoOutputDto>
    buscarPorNome(String nome, int pagina, int tamanho) {

        var result = banco.buscarPorNome(nome,pagina,tamanho);
        return result.map(PessoaCondicaoApplicationMapper::paraInfoDto);
    }

    @Transactional(readOnly = true)
    public PessoaCondicao buscarEntidade(UUID id){
        return banco.buscarPorId(id).orElseThrow(
                ()->new PessoaCondicaoNaoEncontradoException(id)
        );
    }

}
