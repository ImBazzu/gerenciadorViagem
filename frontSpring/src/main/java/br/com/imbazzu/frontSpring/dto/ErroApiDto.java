package br.com.imbazzu.frontSpring.dto;

public record ErroApiDto(String dataHora,
                         int status,
                         String erro,
                         String mensagem,
                         String caminho)  {
}
