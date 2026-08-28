package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto.CidadeResponseDto;
import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto.CidadeRequestDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.dto.CidadeInfoOutputDto;

public class CidadeControllerMapper {

    public static CidadeResponseDto paraResponse(
            CidadeInfoOutputDto result){
        return  new CidadeResponseDto(
                result.id(),
                result.nome(),
                result.estado(),
                result.tempoViagem()
        );
    }

    public static CidadeSalvarInputDto paraSalvarInput(CidadeRequestDto dto){

        return new CidadeSalvarInputDto(
                dto.nome(),
                dto.estado(),
                dto.horas(),
                dto.minutos()
        );
    }

}
