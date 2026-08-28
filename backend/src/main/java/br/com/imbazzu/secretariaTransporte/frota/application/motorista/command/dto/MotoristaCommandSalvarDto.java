package br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.dto;

import java.util.List;
import java.util.UUID;

public record MotoristaCommandSalvarDto(String nome, String cpf, String telefone,
                                        List<UUID> idVeiculos) {
}
