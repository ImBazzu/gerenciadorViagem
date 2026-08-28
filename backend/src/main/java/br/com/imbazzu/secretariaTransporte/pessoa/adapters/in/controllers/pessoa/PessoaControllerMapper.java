package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa;

import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaInfoOutputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaResponseDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaSalvarInputDto;

public class PessoaControllerMapper {


    public static PessoaResponseDto toResponse(PessoaInfoOutputDto dto) {

        return new PessoaResponseDto(
                dto.id(),
                dto.nome(),
                dto.cpf(),
                dto.idCondicao(),
                dto.telefone(),
                dto.endereco(),
                dto.observacao());
    }


    public static PessoaSalvarInputDto toInputDto(PessoaRequestDto dto) {
        return new PessoaSalvarInputDto(
                dto.nome(),dto.cpf(),dto.idCondicao(),dto.telefone(),dto.endereco(),dto.observacao()
        );
    }

}
