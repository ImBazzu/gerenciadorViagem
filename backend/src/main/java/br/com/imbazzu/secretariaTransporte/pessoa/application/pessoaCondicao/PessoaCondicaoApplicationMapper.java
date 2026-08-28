package br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoaCondicao.domain.PessoaCondicao;

public class PessoaCondicaoApplicationMapper {

    public static PessoaCondicaoInfoOutputDto paraInfoDto(PessoaCondicao pessoaCondicao) {
        return new PessoaCondicaoInfoOutputDto(pessoaCondicao.getId(),pessoaCondicao.getNome().valor());
    }

}
