package br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command;

import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;

import java.util.Optional;
import java.util.UUID;

public interface MotoristaBancoCommandPorta {

    UUID salvar(Motorista motorista);

    boolean verificarExistenciaCpf(String cpf);

    boolean verificarExistenciaCpfIgnorandoId(String cpf, UUID id);

    Optional<Motorista> buscarPorId(UUID id);
}
