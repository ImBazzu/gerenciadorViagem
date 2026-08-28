package br.com.imbazzu.frontSpring.dto.viagem;

import java.util.List;

public record ViagemPorPeriodoResponseDto(List<ViagemResponseDto> antesDas0530, List<ViagemResponseDto> depoisDas0530) {

}
