package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.command;

import br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.MotoristaBanco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Responsável interagir com o banco de dados
 */
public interface MotoristaBancoCommandRepository extends JpaRepository<MotoristaBanco, UUID> {


    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdIgnoreCase(String cpf, UUID id);

}
