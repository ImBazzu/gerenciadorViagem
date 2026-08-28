package br.com.imbazzu.frontSpring.dto.viagem;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import br.com.imbazzu.frontSpring.dto.passageiro.PassageiroRequestDto;

public record ViagemRequestDto(List<PassageiroRequestDto> passageiros, LocalTime horaSaida, Long idMotorista,
        LocalDate dataSaida) {

}
