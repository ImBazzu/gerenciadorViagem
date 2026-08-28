package br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

public class ErroMapper {

    public static ErroDto toErroDto(HttpStatus http, Exception e, HttpServletRequest request) {
        return new ErroDto(LocalDateTime.now(), http.value(),http.getReasonPhrase(),
                e.getMessage(), HtmlUtils.htmlEscape(request.getServletPath()));
    }
}
