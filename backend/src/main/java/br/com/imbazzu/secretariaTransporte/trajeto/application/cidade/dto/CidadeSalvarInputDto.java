package br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.CidadeEstadoEnum;

public record CidadeSalvarInputDto(
            String nome,

            CidadeEstadoEnum estado,
            long horas,

            long minutos){
}
