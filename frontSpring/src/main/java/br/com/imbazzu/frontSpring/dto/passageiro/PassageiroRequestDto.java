package br.com.imbazzu.frontSpring.dto.passageiro;

import java.time.LocalTime;

public record PassageiroRequestDto(Long idPaciente, Long idDestino, int acompanhante, LocalTime horaChegada,
        boolean buscar) {

}
