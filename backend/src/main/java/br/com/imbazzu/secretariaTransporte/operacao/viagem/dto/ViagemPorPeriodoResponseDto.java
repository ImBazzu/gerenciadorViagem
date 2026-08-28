package br.com.imbazzu.secretariaTransporte.operacao.viagem.dto;

import java.util.List;

public record ViagemPorPeriodoResponseDto(List<ViagemResponseDto> antesDas0530, List<ViagemResponseDto> depoisDas0530) {
}
