package br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.UUID;


public record PassageiroRequestDto(
        @NotNull
        UUID idPessoa,
        @NotNull
        UUID idDestino,
        @Min(0)
        int acompanhante,
        @NotNull
        LocalTime horaChegada,
        boolean buscar

) {

}
