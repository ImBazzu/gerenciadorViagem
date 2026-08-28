package br.com.imbazzu.frontSpring.dto.viagem;

import java.util.List;

import br.com.imbazzu.frontSpring.dto.passageiro.PassageiroResponseDto;

public record ViagemResponseDto(Long id, List<PassageiroResponseDto> passageiros, String destino, String data,
                String hora, String motorista, String motoristaTelefone) {

}
