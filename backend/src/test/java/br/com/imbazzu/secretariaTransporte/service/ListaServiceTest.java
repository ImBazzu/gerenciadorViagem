package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.DadosInvalidosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeEmUsoException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.operacao.lista.ListaService;
import br.com.imbazzu.secretariaTransporte.operacao.lista.dto.ListaRequestDto;
import br.com.imbazzu.secretariaTransporte.operacao.passageiro.dto.PassageiroRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.PessoaApplicationImpl;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.ViagemService;
import br.com.imbazzu.secretariaTransporte.operacao.viagem.dto.ViagemRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestContainersConfig.class)
public class ListaServiceTest {

    @Autowired
    private ListaService listaService;

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private PessoaApplicationImpl pessoaApplicationImpl;

    @Autowired
    private ViagemService viagemService;

    // =========================================================
    //  Helpers
    // =========================================================

    private UUID idListaCarro() {
        return listaService.buscarListaPorData(LocalDate.of(2026, 6, 15))
                .stream()
                .filter(l -> l.tipo().equals("CARRO"))
                .findFirst()
                .orElseThrow()
                .id();
    }

    private UUID idListaVan() {
        return listaService.buscarListaPorData(LocalDate.of(2026, 6, 16))
                .stream()
                .filter(l -> l.tipo().equals("VAN"))
                .findFirst()
                .orElseThrow()
                .id();
    }

    private PassageiroRequestDto passageiroDto() {
        var pessoa = pessoaApplicationImpl.buscarListaPorNome("JULIA ALVES", PessoaTipoEnum.CARRO_OU_VAN, PageRequest.of(0, 10))
                .content().getFirst();
        var destino = destinoService.buscarPorNome("Hospital Regional", "", PageRequest.of(0, 10))
                .content().getFirst();
        return new PassageiroRequestDto(
                pessoa.dadosPessoais().id(),
                destino.id(),
                0,
                LocalTime.of(8, 0),
                true
        );
    }

    // =========================================================
    //  salvar
    // =========================================================

    @Test
    void salvarListaComSucesso() {

        var dto = new ListaRequestDto(
                "ListaDoDia Teste",
                "Descrição teste",
                LocalDate.of(2026, 6, 17),
                ListaTipoEnum.ONIBUS
        );

        var listaSalva = listaService.salvar(dto);

        assertThat(listaSalva).isNotNull();
        assertThat(listaSalva.id()).isNotNull();
        assertThat(listaSalva.titulo()).isEqualTo("LISTA TESTE");
        assertThat(listaSalva.tipo()).isEqualTo("ONIBUS"); // CORRIGIDO: era .equals("ONIBUS") sem assertThat real
    }

    // =========================================================
    //  buscarListaPorData
    // =========================================================

    @Test
    void buscarListaPorDataExistente() {

        var listas = listaService.buscarListaPorData(LocalDate.of(2026, 6, 15));

        assertThat(listas).isNotEmpty();
        assertThat(listas).allMatch(l -> l.data().equals(LocalDate.of(2026, 6, 15)));
    }

    @Test
    void buscarListaPorDataInexistente() {

        assertThatThrownBy(() ->
                listaService.buscarListaPorData(LocalDate.of(2000, 1, 1)))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Nenhuma lista encontrada");
    }

    // =========================================================
    //  editar
    // =========================================================

    @Test
    void editarListaComSucesso() {

        var idLista = idListaCarro();

        var dto = new ListaRequestDto(
                "ListaDoDia Editada",
                "Nova descrição",
                LocalDate.of(2026, 6, 15),
                ListaTipoEnum.CARRO
        );

        var listaEditada = listaService.editar(idLista, dto);

        assertThat(listaEditada.id()).isEqualTo(idLista);
        assertThat(listaEditada.titulo()).isEqualTo("LISTA EDITADA");
    }

    @Test
    void editarListaInexistente() {

        var dto = new ListaRequestDto(
                "ListaDoDia Qualquer",
                null,
                LocalDate.of(2026, 6, 15),
                ListaTipoEnum.CARRO
        );

        assertThatThrownBy(() ->
                listaService.editar(UUID.randomUUID(), dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("ListaDoDia nao encontrada");
    }

    // =========================================================
    //  deletarLista
    // =========================================================

    @Test
    void deletarListaComSucesso() {

        var dto = new ListaRequestDto(
                "ListaDoDia Para Deletar",
                null,
                LocalDate.of(2026, 6, 20),
                ListaTipoEnum.VAN
        );
        var lista = listaService.salvar(dto);

        listaService.deletarLista(lista.id());

        assertThatThrownBy(() ->
                listaService.buscarListaPorData(LocalDate.of(2026, 6, 20)))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deletarListaInexistente() {

        assertThatThrownBy(() ->
                listaService.deletarLista(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("ListaDoDia nao encontrada");
    }

    @Test
    void deletarListaComPassageiros() {

        var idLista = idListaCarro();
        var passageiroRequest = passageiroDto();
        var lista = listaService.adicionarPassageiro(idLista, passageiroRequest);
        var passageiro = lista.passageiros().getLast();
        var destino = destinoService.buscarEntidadePorId(passageiroRequest.idDestino());
        viagemService.salvar(new ViagemRequestDto(
                lista.data(),
                destino.calcularHoraSaida(passageiroRequest.horaChegada()),
                null,
                List.of(passageiro.id())));

        assertThatThrownBy(() ->
                listaService.deletarLista(idLista))
                .isInstanceOf(EntidadeEmUsoException.class);
    }

    // =========================================================
    //  adicionarPassageiro
    // =========================================================

    @Test
    void adicionarPassageiroComSucesso() {

        var idLista = idListaCarro();
        var destino = destinoService.buscarPorNome("", "ESTANCIA", PageRequest.of(0, 10))
                .content().getFirst();
        var pessoa = pessoaApplicationImpl
                .buscarListaPorNome("pedro", null, PageRequest.of(0, 20))
                .content().getFirst();
        var dto = new PassageiroRequestDto(
                pessoa.dadosPessoais().id(),
                destino.id(),
                1,
                LocalTime.of(9, 30),
                true
        );

        var lista = listaService.adicionarPassageiro(idLista, dto);

        // CORRIGIDO: lambda com sintaxe correta (sem return explícito em expressão lambda)
        assertThat(
                lista.passageiros().stream().anyMatch(p ->
                        p.nome().equals(pessoa.dadosPessoais().nome())
                                && p.telefone().equals(pessoa.dadosPessoais().telefone()))
        ).isTrue();
    }

    @Test
    void adicionarPassageiroEmListaInexistente() {

        assertThatThrownBy(() ->
                listaService.adicionarPassageiro(UUID.randomUUID(), passageiroDto()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("ListaDoDia nao encontrada");
    }

    @Test
    void adicionarPassageiroComPessoaInexistente() {

        var idLista = idListaCarro();
        var destino = destinoService.buscarPorNome("Hospital Regional", "LAGARTO", PageRequest.of(0, 10))
                .content().getFirst();

        var dto = new PassageiroRequestDto(
                UUID.randomUUID(),
                destino.id(),
                0,
                LocalTime.of(8, 0),
                true
        );

        assertThatThrownBy(() ->
                listaService.adicionarPassageiro(idLista, dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Pessoa nao encontrada");
    }

    @Test
    void adicionarPassageiroComDestinoInexistente() {

        var idLista = idListaCarro();
        var pessoa = pessoaApplicationImpl.buscarListaPorNome("Maria Silva", PessoaTipoEnum.COMUM, PageRequest.of(0, 10))
                .content().getFirst();

        var dto = new PassageiroRequestDto(
                pessoa.dadosPessoais().id(),
                UUID.randomUUID(),
                0,
                LocalTime.of(8, 0),
                true
        );

        assertThatThrownBy(() ->
                listaService.adicionarPassageiro(idLista, dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Destino nao encontrada");
    }

    // =========================================================
    //  buscarTodosPassageiros
    // =========================================================

    @Test
    void buscarTodosPassageirosDeListaVazia() {

        // CORRIGIDO: nome em maiúsculo para consistência com o padrão do sistema
        var lista = listaService.buscarPorNome("VIAGENS QUARTA");
        var passageiros = listaService.buscarTodosPassageiros(lista.id());

        assertThat(passageiros).isEmpty();
    }

    @Test
    void buscarTodosPassageirosComPassageiros() {

        var idLista = listaService.buscarPorNome("VIAGENS SEGUNDA").id();

        var passageiros = listaService.buscarTodosPassageiros(idLista);

        assertThat(passageiros).hasSize(3);
    }

    @Test
    void buscarTodosPassageirosDeListaInexistente() {

        assertThatThrownBy(() ->
                listaService.buscarTodosPassageiros(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("ListaDoDia nao encontrada");
    }

    // =========================================================
    //  buscarTodosPassageirosSemCarro
    // =========================================================

    @Test
    void buscarPassageirosSemCarroComSucesso() {

        var idLista = idListaCarro();
        listaService.adicionarPassageiro(idLista, passageiroDto());

        var passageiros = listaService.buscarTodosPassageirosSemCarro(idLista);

        assertThat(passageiros).isNotEmpty();
    }

    @Test
    void buscarPassageirosSemCarroEmListaNaoCarro() {

        var idListaVan = idListaVan();

        assertThatThrownBy(() ->
                listaService.buscarTodosPassageirosSemCarro(idListaVan))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("tipo Carro");
    }

    // =========================================================
    //  deletarPassageiro
    // =========================================================

    @Test
    void deletarPassageiroComSucesso() {

        // CORRIGIDO: usa lista limpa (sem passageiros do seed) para o isEmpty() ser válido
        var dto = new ListaRequestDto(
                "ListaDoDia Carro Limpa",
                null,
                LocalDate.of(2026, 6, 20),
                ListaTipoEnum.CARRO
        );
        var listaLimpa = listaService.salvar(dto);
        var listaComPassageiro = listaService.adicionarPassageiro(listaLimpa.id(), passageiroDto());
        var passageiro = listaComPassageiro.passageiros().getLast();

        listaService.deletarPassageiro(listaLimpa.id(), passageiro.id());

        var passageiros = listaService.buscarTodosPassageiros(listaLimpa.id());
        assertThat(passageiros).isEmpty();
    }

    @Test
    void deletarPassageiroInexistente() {

        var idLista = idListaCarro();

        assertThatThrownBy(() ->
                listaService.deletarPassageiro(idLista, UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Passageiro não encontrado");
    }

    // =========================================================
    //  moverPassageiro
    // =========================================================

    @Test
    void moverPassageiroComSucesso() {

        var idListaCarro = idListaCarro();
        var lista = listaService.adicionarPassageiro(idListaCarro, passageiroDto());
        var quantidadeAntes = lista.passageiros().size();
        var passageiro = lista.passageiros().getLast();

        var dto = new ListaRequestDto(
                "ListaDoDia Carro 2",
                null,
                LocalDate.of(2026, 6, 15),
                ListaTipoEnum.CARRO
        );
        var listaDestino = listaService.salvar(dto);

        var listaAtualizada = listaService.moverPassageiro(passageiro.id(), listaDestino.id());

        assertThat(listaAtualizada.id()).isEqualTo(listaDestino.id());

        // CORRIGIDO: compara tamanho da lista de origem (era assertThat(quantidadeAtual).isEmpty())
        var passageirosOrigem = listaService.buscarTodosPassageiros(idListaCarro);
        assertThat(passageirosOrigem).hasSize(quantidadeAntes - 1);
    }

    @Test
    void moverPassageiroParaListaDeDataDiferente() {

        var idListaCarro = idListaCarro();
        var lista = listaService.adicionarPassageiro(idListaCarro, passageiroDto());
        var passageiro = lista.passageiros().getLast();
        var idListaVan = idListaVan();

        assertThatThrownBy(() ->
                listaService.moverPassageiro(passageiro.id(), idListaVan))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("mesma data");
    }

    @Test
    void moverPassageiroParaListaInexistente() {

        var idListaCarro = idListaCarro();
        var lista = listaService.adicionarPassageiro(idListaCarro, passageiroDto());
        var novoPassageiro = lista.passageiros().getLast();

        assertThatThrownBy(() ->
                listaService.moverPassageiro(novoPassageiro.id(), UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("ListaDoDia nao encontrada");
    }
}