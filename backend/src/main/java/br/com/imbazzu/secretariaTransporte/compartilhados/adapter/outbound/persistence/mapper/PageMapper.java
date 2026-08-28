package br.com.imbazzu.secretariaTransporte.compartilhados.adapter.outbound.persistence.mapper;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import org.springframework.data.domain.Page;

public final class PageMapper {

    private PageMapper() {}

    public static <T> ResultadoPaginado<T> toDomain(
            Page<T> page) {
        return new ResultadoPaginado<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}