package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.handler;

import java.time.LocalDateTime;

public record ErroDto(LocalDateTime dataHora,
                      int status, String erro, String mensagem, String caminho) {
}

