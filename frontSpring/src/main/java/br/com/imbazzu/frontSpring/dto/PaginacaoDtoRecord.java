package br.com.imbazzu.frontSpring.dto;

import java.util.List;

public record PaginacaoDtoRecord<T> (List<T> content,
                                     int page,
                                     int size,
                                     long totalElements,
                                     int totalPages) {
}
