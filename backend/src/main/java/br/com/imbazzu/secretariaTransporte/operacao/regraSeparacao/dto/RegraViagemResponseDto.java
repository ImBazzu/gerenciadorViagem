package br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto;

import java.util.List;

// -------------------------------------------------------------------------
// RegraViagem
// -------------------------------------------------------------------------
    /**
     * Resposta com as configurações atuais.
     */
    public record RegraViagemResponseDto(
            int capacidadeMaxima,
            String tempoTolerancia,
            List<ConjuntoResponseDto> conjuntos
    ) {}

