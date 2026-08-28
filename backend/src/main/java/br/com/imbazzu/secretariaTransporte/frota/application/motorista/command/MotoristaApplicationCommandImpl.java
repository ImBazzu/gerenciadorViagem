package br.com.imbazzu.secretariaTransporte.frota.application.motorista.command;

import br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.dto.MotoristaCommandSalvarDto;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.Motorista;
import br.com.imbazzu.secretariaTransporte.frota.core.excecoes.MotoristaDuplicadoException;
import br.com.imbazzu.secretariaTransporte.frota.core.excecoes.MotoristaNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command.MotoristaApplicationCommandPorta;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command.MotoristaBancoCommandPorta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class MotoristaApplicationCommandImpl implements MotoristaApplicationCommandPorta {

    private MotoristaBancoCommandPorta banco;



    @Override
    public UUID salvar(MotoristaCommandSalvarDto dto) {
        var motorista = MotoristaCommandMapper.paraDomain(dto);
        if(banco.verificarExistenciaCpf(motorista.getCpf().valor())){
            throw new MotoristaDuplicadoException(motorista.getCpf().valor());
        }
        return banco.salvar(motorista);
    }

    @Override
    public void editar(UUID id, MotoristaCommandSalvarDto dto) {
        var motorista = buscarPorId(id);
        MotoristaCommandMapper.editarMotorista(motorista, dto);
        if(banco.verificarExistenciaCpfIgnorandoId(motorista.getCpf().valor(),id)){
            throw new MotoristaDuplicadoException(motorista.getCpf().valor());
        }
        banco.salvar(motorista);
    }

    @Override
    public void arquivar(UUID uuid) {
        var  motorista = buscarPorId(uuid);
        motorista.arquivar();
        banco.salvar(motorista);
    }

    @Override
    public void desarquivar(UUID uuid) {
        var motorista = buscarPorId(uuid);
        motorista.desarquivar();
        banco.salvar(motorista);
    }

    @Override
    public Motorista buscarPorId(UUID id) {
        return banco.buscarPorId(id).orElseThrow(
                ()->new MotoristaNaoEncontradoException(id)
        );
    }
}
