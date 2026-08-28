package br.com.imbazzu.frontSpring.dto.passageiro;

import java.time.LocalTime;

public record PassageiroResponseDto(Long id, String nome, String telefone, int acompanhante, LocalTime horaChegada,
                String destino, String endereco) {

}
