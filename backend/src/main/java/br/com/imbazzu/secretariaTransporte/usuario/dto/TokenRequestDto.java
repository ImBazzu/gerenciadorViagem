package br.com.imbazzu.secretariaTransporte.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequestDto(@NotBlank String refreshToken) {
}
