package br.com.imbazzu.secretariaTransporte.compartilhados.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter
public class DurationToMinutesConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duracao) {
        return duracao == null ? null : duracao.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long minutos) {
        return minutos == null ? null : Duration.ofMinutes(minutos);
    }
}
