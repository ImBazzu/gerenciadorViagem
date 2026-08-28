package br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto;

import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.MotoristaTipoEnum;
import br.com.imbazzu.secretariaTransporte.compartilhados.dadosPessoais.dto.DadosPessoaisRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
public record MotoristaRequestDto(

        String nome,
        String cpf,
        String telefone,
        @NotBlank(message = "Tipo do motorista é obrigatório")
        @NotBlank String tipoMotorista) {
}
