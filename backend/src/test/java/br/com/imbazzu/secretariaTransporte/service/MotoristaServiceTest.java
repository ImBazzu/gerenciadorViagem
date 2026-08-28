package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.compartilhados.dadosPessoais.dto.DadosPessoaisRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura.BancoDeDadosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.frota.core.domain.motorista.MotoristaTipoEnum;
import br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto.MotoristaRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestContainersConfig.class)
public class MotoristaServiceTest {

    @Autowired
    private MotoristaApplicationImpl motoristaApplicationImpl;

    // =========================================================
    //  buscarPorNome
    // =========================================================

    @Test
    void buscarPorNomeExistente() {

        var resultado = motoristaApplicationImpl.buscarPorNome(
                "José Santos",
                null,
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .hasSize(1);

        assertThat(resultado.content()
                .getFirst()
                .dadosPessoais().nome())
                .isEqualTo("JOSÉ SANTOS");
    }

    @Test
    void buscarPorNomeInexistente() {

        var resultado = motoristaApplicationImpl.buscarPorNome(
                "MOTORISTA_INEXISTENTE",
                null,
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .isEmpty();
    }

    @Test
    void buscarPorNomeComFiltroTipoEfetivo() {

        var resultado = motoristaApplicationImpl.buscarPorNome(
                "",
                MotoristaTipoEnum.FIXO_CARRO,
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .isNotEmpty();

        assertThat(resultado.content())
                .allMatch(m -> m.tipoMotorista() == MotoristaTipoEnum.FIXO_CARRO);
    }

    @Test
    void buscarPorNomeComFiltroTipoTerceirizado() {

        var resultado = motoristaApplicationImpl.buscarPorNome(
                "",
                MotoristaTipoEnum.TERCEIRIZADO,
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .isNotEmpty();

        assertThat(resultado.content())
                .allMatch(m -> m.tipoMotorista() == MotoristaTipoEnum.TERCEIRIZADO);
    }

    // =========================================================
    //  buscarPorId
    // =========================================================

    @Test
    void buscarPorIdExistente() {

        var motorista = motoristaApplicationImpl.buscarPorNome(
                        "José Santos",
                        null,
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var motoristaBuscado = motoristaApplicationImpl.buscarPorId(motorista.dadosPessoais().id());

        assertThat(motoristaBuscado).isNotNull();
        assertThat(motoristaBuscado.dadosPessoais().nome()).isEqualTo("JOSÉ SANTOS");
    }

    @Test
    void buscarPorIdInexistente() {

        assertThatThrownBy(() ->
                motoristaApplicationImpl.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Motorista não encontrado");
    }

    // =========================================================
    //  salvar
    // =========================================================

    @Test
    void salvarMotoristaComSucesso() {

        var dto = new MotoristaRequestDto(
                new DadosPessoaisRequestDto(
                        "Fernando Souza",
                        "33060919011",
                        "(79)98888-7777"
                ),
                MotoristaTipoEnum.FIXO_CARRO
        );

        var motoristaSalvo = motoristaApplicationImpl.salvar(dto);

        assertThat(motoristaSalvo).isNotNull();
        assertThat(motoristaSalvo.dadosPessoais().id()).isNotNull();
        assertThat(motoristaSalvo.dadosPessoais().nome()).isEqualTo("FERNANDO SOUZA");
    }

    @Test
    void salvarMotoristaCpfDuplicado() {

        // CPF '11111111111' pertence a "José Santos" no seed
        var dto = new MotoristaRequestDto(
                new DadosPessoaisRequestDto(
                        "Outro Nome",
                        "24067388098",
                        "79911112222"
                ),
                MotoristaTipoEnum.TERCEIRIZADO
        );

        assertThatThrownBy(() ->
                motoristaApplicationImpl.salvar(dto))
                .isInstanceOf(BancoDeDadosException.class)
                .hasMessageContaining("Erro ao salvar o Motorista");
    }

    // =========================================================
    //  editar
    // =========================================================

    @Test
    void editarMotoristaComSucesso() {

        var motorista = motoristaApplicationImpl.buscarPorNome(
                        "Carlos Oliveira",
                        null,
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new MotoristaRequestDto(
                new DadosPessoaisRequestDto(
                        "Carlos Oliveira Editado",
                        "180.345.050-99",
                        "79922223333"
                ),
                MotoristaTipoEnum.TERCEIRIZADO
        );

        var motoristaEditado = motoristaApplicationImpl.editar(motorista.dadosPessoais().id(), dto);

        assertThat(motoristaEditado.dadosPessoais().id()).isEqualTo(motorista.dadosPessoais().id());
        assertThat(motoristaEditado.dadosPessoais().nome()).isEqualTo("CARLOS OLIVEIRA EDITADO");
    }

    @Test
    void editarMotoristaInexistente() {

        var dto = new MotoristaRequestDto(
                new DadosPessoaisRequestDto(
                        "Qualquer Nome",
                        "00000000000",
                        "79900000000"
                ),
                MotoristaTipoEnum.TERCEIRIZADO
        );

        assertThatThrownBy(() ->
                motoristaApplicationImpl.editar(UUID.randomUUID(), dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Motorista não encontrado");
    }

    @Test
    void editarMotoristaCpfDuplicado() {

        // Tenta editar Carlos Oliveira com o CPF de José Santos
        var motorista = motoristaApplicationImpl.buscarPorNome(
                        "Carlos Oliveira",
                        null,
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new MotoristaRequestDto(
                new DadosPessoaisRequestDto(
                        "Carlos Oliveira",
                        "24067388098", // CPF do José Santos
                        "79922222222"
                ),
                MotoristaTipoEnum.TERCEIRIZADO
        );

        assertThatThrownBy(() ->
                motoristaApplicationImpl.editar(motorista.dadosPessoais().id(), dto))
                .isInstanceOf(BancoDeDadosException.class)
                .hasMessageContaining("Erro ao editar o motorista");
    }

    // =========================================================
    //  deletar
    // =========================================================

    @Test
    void deletarMotoristaComSucesso() {

        // Carlos Oliveira não tem viagem associada no seed
        var motorista = motoristaApplicationImpl.buscarPorNome(
                        "CARLOS OLIVEIRA",
                        null,
                        PageRequest.of(0, 10))
                .content()
                .getFirst();



        motoristaApplicationImpl.deletar(motorista.dadosPessoais().id());

        assertThatThrownBy(() ->
                motoristaApplicationImpl.buscarPorId(motorista.dadosPessoais().id()))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }



    @Test
    void deletarMotoristaInexistente() {

        assertThatThrownBy(() ->
                motoristaApplicationImpl.deletar(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Motorista não encontrado");
    }
}