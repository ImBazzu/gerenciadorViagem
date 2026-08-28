package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto.PessoaCondicaoRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto.PessoaCondicaoResponseDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.dto.PessoaCondicaoSalvarInputDto;
import jakarta.validation.Valid;

public class PessoaCondicaoMapper {

    public static PessoaCondicaoResponseDto converterDto(PessoaCondicaoInfoOutputDto dto){
        return new PessoaCondicaoResponseDto(dto.id(),dto.nome());
    }

    public static PessoaCondicaoSalvarInputDto paraInputSalvar(PessoaCondicaoRequestDto dto) {
            return new PessoaCondicaoSalvarInputDto(dto.nome());
        }
}
