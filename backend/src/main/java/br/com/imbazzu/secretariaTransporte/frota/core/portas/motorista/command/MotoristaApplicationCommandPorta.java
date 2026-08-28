package br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command;

import br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.dto.MotoristaCommandSalvarDto;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;

import java.util.Optional;
import java.util.UUID;

public interface MotoristaApplicationCommandPorta {

    UUID salvar(MotoristaCommandSalvarDto dto);

    void editar(UUID id, MotoristaCommandSalvarDto dto);

    void arquivar(UUID uuid);

    void desarquivar(UUID uuid);

    Motorista buscarPorId(UUID id);
}
