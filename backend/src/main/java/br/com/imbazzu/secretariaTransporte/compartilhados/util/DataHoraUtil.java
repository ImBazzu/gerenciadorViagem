package br.com.imbazzu.secretariaTransporte.compartilhados.util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DataHoraUtil {

    private static final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

    public static String dataParaTexto(LocalDate data) {
        return formatoData.format(data);
    }


    public static String horaParaTexto(LocalTime hora) {
        return hora.format(formatoHora);
    }

    public static Duration horaEMinutoParaDuration(Integer hora, Integer minuto) {
        return Duration.ofHours(hora).plusMinutes(minuto);
    }

    public static String durationParaTexto(Duration duration) {
        long hora = duration.toHours();
        long minuto = duration.toMinutesPart();
        return  String.format("%02d:%02d", hora, minuto);
    }
}
