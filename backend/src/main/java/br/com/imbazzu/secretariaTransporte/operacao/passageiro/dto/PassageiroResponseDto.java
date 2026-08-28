package br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto;

import java.time.LocalTime;
import java.util.UUID;

public record PassageiroResponseDto(UUID id, String nome, String telefone,
                                    int acompanhante, LocalTime horaChegada, String destino, String endereco) {
}
