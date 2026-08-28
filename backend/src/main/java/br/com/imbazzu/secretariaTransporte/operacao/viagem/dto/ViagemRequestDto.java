package br.com.imbazzu.secretariaTransporte.operacao.viagem.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ViagemRequestDto(

        @NotNull(message = "Data de saída é obrigatório")
        LocalDate data,

        @NotNull(message = "Hora de saída é obrigatória")
        LocalTime horaSaida,

        UUID idMotorista,

        @NotNull(message = "Informe ao menos um passageiro")
        List<UUID> idsPassageiros
) {}