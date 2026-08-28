package br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.command;

import br.com.imbazzu.secretariaTransporte.frota.adapters.out.motorista.MotoristaBancoMapper;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command.MotoristaBancoCommandPorta;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class MotoristaBancoCommandRepositoryImpl implements MotoristaBancoCommandPorta {

    private MotoristaBancoCommandRepository banco;

    public MotoristaBancoCommandRepositoryImpl(MotoristaBancoCommandRepository banco) {
        this.banco = banco;
    }

    @Override
    public UUID salvar(Motorista motorista) {
        var motoristaJpa = MotoristaBancoMapper.paraMotoristaBanco(motorista);
        var motoristaSalvo = banco.save(motoristaJpa);
        return motoristaSalvo.getId();
    }

    @Override
    public boolean verificarExistenciaCpf(String cpf) {
        return banco.existsByCpf(cpf);
    }

    @Override
    public boolean verificarExistenciaCpfIgnorandoId(String cpf, UUID id) {
        return banco.existsByCpfAndIdIgnoreCase(cpf,id);
    }

    @Override
    public Optional<Motorista> buscarPorId(UUID id) {
        return banco.findById(id).map(MotoristaBancoMapper::paraMotorista);
    }
}
