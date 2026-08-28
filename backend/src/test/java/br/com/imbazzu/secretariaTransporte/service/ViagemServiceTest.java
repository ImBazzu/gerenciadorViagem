package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.operacao.viagem.ViagemService;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.dto.ViagemRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.operacao.passageiro.PassageiroRepository;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.ViagemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestContainersConfig.class)
public class ViagemServiceTest {

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private ViagemRepository viagemRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    // ── helpers ──────────────────────────────────────────────────────────────

    private UUID primeiraViagem() {
        return viagemRepository.findAll().iterator().next().getId();
    }

    private UUID segundaViagem() {
        return viagemRepository.findAll().iterator().next().getId();
    }

    private UUID primeiroPassageiro() {
        var passageiros =passageiroRepository.findAll();
        return passageiros.getFirst().getId();
    }

    private UUID segundoPassageiro() {
        return passageiroRepository.findAll().get(1).getId();
    }

    // ── testes ───────────────────────────────────────────────────────────────

    @Test
    void deveSalvarViagemComPassageirosQuandoDadosForemValidos() {

        UUID passageiro1 = primeiroPassageiro();
        var lista= passageiroRepository.findById(passageiro1).get().getListaDoDia();
        UUID passageiro2 = lista.getPassageiros().get(1).getId();

        var dto = new ViagemRequestDto(
                lista.getData(),
                LocalTime.of(8, 0),
                null,
                List.of(passageiro1, passageiro2)
        );

        var viagem = viagemService.salvar(dto);

        assertThat(viagem).isNotNull();
        assertThat(viagem.id()).isNotNull();
        assertThat(viagem.passageiros()).hasSize(2);
    }

    @Test
    void deveBuscarViagemPorId() {

        UUID idViagem = primeiraViagem();

        var viagem = viagemService.buscarPorId(idViagem);

        assertThat(viagem).isNotNull();
        assertThat(viagem.id()).isEqualTo(idViagem);
    }

    @Test
    void deveRemoverPassageiroDaViagem() {

        UUID viagemId = primeiraViagem();
        UUID passageiroId = primeiroPassageiro();

        var viagem = viagemService.removerPassageiro(viagemId, passageiroId);

        assertThat(viagem.passageiros())
                .noneMatch(p -> p.id().equals(passageiroId));
    }

    @Test
    void deveMoverPassageiroEntreViagens() {

        UUID viagemDestino = segundaViagem();
        UUID passageiro = primeiroPassageiro();

        var resultado = viagemService.moverPassageiro(viagemDestino, passageiro);

        assertThat(resultado.passageiros())
                .anyMatch(p -> p.id().equals(passageiro));
    }

    @Test
    void deveDeletarViagemComSucesso() {

        UUID viagemId = primeiraViagem();

        viagemService.deletarViagem(viagemId);

        assertThatThrownBy(() -> viagemService.buscarPorId(viagemId))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverViagensNoDia() {

        LocalDate dataSemViagens = LocalDate.of(2099, 1, 1);

        var resultado = viagemService.buscarViagensDoDia(dataSemViagens);

        assertThat(resultado)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void deveRetornarDtoComListasVaziasQuandoNaoHouverViagensNoDia() {

        LocalDate dataSemViagens = LocalDate.of(2099, 1, 1);

        var resultado = viagemService.buscarViagemSeparadoPorHorario(dataSemViagens);

        assertThat(resultado).isNotNull();
        assertThat(resultado.antesDas0530()).isEmpty();
        assertThat(resultado.depoisDas0530()).isEmpty();
    }

    @Test
    void deveEditarViagemComSucesso() {

        UUID viagemId = primeiraViagem();
        UUID passageiroNovo = primeiroPassageiro();

        var dto = new ViagemRequestDto(
                LocalDate.now(),
                LocalTime.of(10, 0),
                null,
                List.of(passageiroNovo)
        );

        var viagem = viagemService.editar(viagemId, dto);

        assertThat(viagem.hora()).isEqualTo("10:00");
        assertThat(viagem.passageiros()).hasSize(1);
    }
}