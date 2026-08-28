package br.com.imbazzu.secretariaTransporte.operacao.viagem.dto;

import br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto.PassageiroResponseDto;

import java.util.List;
import java.util.UUID;

public record ViagemResponseDto(UUID id, List<PassageiroResponseDto> passageiros, String destino,
                                String hora, String motorista, String motoristaTelefone) {

}
