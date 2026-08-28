package br.com.imbazzu.secretariaTransporte.operacao.lista.dto;

import br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto.PassageiroResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ListaResponseDto(
                UUID id,

                String titulo,

                String descricao,

                LocalDate data,

                String tipo,

                List<String> cidades,
                List<PassageiroResponseDto> passageiros) {
}
