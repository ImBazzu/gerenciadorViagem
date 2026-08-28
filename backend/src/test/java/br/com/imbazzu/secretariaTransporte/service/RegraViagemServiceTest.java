package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.CidadeApplicationImpl;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.DadosInvalidosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.RegraViagemService;
import br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto.ConjuntoRequestDto;
import br.com.imbazzu.secretariaTransporte.operacao.regraSeparacao.dto.RegraViagemRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestContainersConfig.class)
public class RegraViagemServiceTest {

    @Autowired
    private RegraViagemService regraViagemService;

    @Autowired
    private CidadeApplicationImpl cidadeServiceImpl;

    // =========================================================
    //  inicializar
    // =========================================================

    @Test
    void inicializarQuandoJaExiste() {

        // O seed já insere a regra — tentar inicializar de novo deve lançar exceção
        var dto = new RegraViagemRequestDto(4, 0, 30);

        assertThatThrownBy(() ->
                regraViagemService.inicializar(dto))
                .isInstanceOf(DadosInvalidosException.class)
                .hasMessageContaining("já foi inicializada");
    }

    // =========================================================
    //  buscarPorId / buscarEntidade
    // =========================================================

    @Test
    void buscarRegraExistente() {

        var regra = regraViagemService.buscarPorId();

        assertThat(regra).isNotNull();
        assertThat(regra.capacidadeMaxima()).isEqualTo(4);
    }

    // =========================================================
    //  atualizar
    // =========================================================

    @Test
    void atualizarRegraComSucesso() {

        var dto = new RegraViagemRequestDto(6, 0, 15);

        var regraAtualizada = regraViagemService.atualizar(dto);

        assertThat(regraAtualizada).isNotNull();
        assertThat(regraAtualizada.capacidadeMaxima()).isEqualTo(6);
    }

    @Test
    void atualizarRegraAlteraTolerancia() {

        var dto = new RegraViagemRequestDto(4, 1, 30);

        var regraAtualizada = regraViagemService.atualizar(dto);

        // tolerância total = 1h30min = 90 minutos
        assertThat(regraAtualizada.tempoTolerancia()).isEqualTo("01:30");
    }

    // =========================================================
    //  adicionarConjunto
    // =========================================================

    @Test
    void adicionarConjuntoComSucesso() {

        // Estancia e Lagarto existem no seed
        var estancia = cidadeServiceImpl.buscarPorNome("Estancia", PageRequest.of(0, 10))
                .content().getFirst();
        var lagarto = cidadeServiceImpl.buscarPorNome("Lagarto", PageRequest.of(0, 10))
                .content().getFirst();

        var dto = new ConjuntoRequestDto(
                "Novo Conjunto",
                Set.of(estancia.id(), lagarto.id())
        );

        var regra = regraViagemService.adicionarConjunto(dto);

        var conjunto = regraViagemService.buscarConjuntosPorNome("Novo Conjunto").getFirst();
        assertThat(conjunto).isNotNull();
        assertThat(conjunto.nome()).isEqualTo("NOVO CONJUNTO");
        assertThat(conjunto.cidades()).hasSize(2);
    }

    @Test
    void adicionarConjuntoComCidadeInexistente() {

        var dto = new ConjuntoRequestDto(
                "Conjunto Inválido",
                Set.of(UUID.randomUUID())
        );

        assertThatThrownBy(() ->
                regraViagemService.adicionarConjunto(dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("não encontradas");
    }

    @Test
    void adicionarConjuntoComAlgumasCidadesInexistentes() {

        // Uma cidade real + uma inexistente
        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();

        var dto = new ConjuntoRequestDto(
                "Conjunto Parcial",
                Set.of(aracaju.id(), UUID.randomUUID())
        );

        assertThatThrownBy(() ->
                regraViagemService.adicionarConjunto(dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("não encontradas");
    }

    // =========================================================
    //  removerConjunto
    // =========================================================

    @Test
    void removerConjuntoComSucesso() {

        // Primeiro adiciona um conjunto para depois remover
        var lagarto = cidadeServiceImpl.buscarPorNome("Lagarto", PageRequest.of(0, 10))
                .content().getFirst();

        var regra = regraViagemService.adicionarConjunto(
                new ConjuntoRequestDto("Conjunto Temporario", Set.of(lagarto.id()))
        );

        var conjunto = regraViagemService.buscarConjuntosPorNome("Conjunto Temporario").getFirst();

        // Não deve lançar exceção
        regraViagemService.removerConjunto(conjunto.id());
    }

    @Test
    void removerConjuntoInexistente() {

        assertThatThrownBy(() ->
                regraViagemService.removerConjunto(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Conjunto de cidades não encontrado");
    }

    // =========================================================
    //  adicionarCidade (em conjunto existente)
    // =========================================================

    @Test
    void adicionarCidadeEmConjuntoComSucesso() {

        // Cria um conjunto com Aracaju
        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();
        var itabaiana = cidadeServiceImpl.buscarPorNome("Itabaiana", PageRequest.of(0, 10))
                .content().getFirst();

        var regra = regraViagemService.adicionarConjunto(
                new ConjuntoRequestDto("Conjunto Base", Set.of(aracaju.id()))
        );

        var conjunto = regraViagemService.buscarConjuntosPorNome("Conjunto Base").getFirst();

        // Adiciona Itabaiana ao conjunto
        var conjuntoAtualizado = regraViagemService.adicionarCidade(
                conjunto.id(), Set.of(aracaju.id(),itabaiana.id())
        );

        assertThat(conjuntoAtualizado.cidades()).hasSize(2);
    }

    @Test
    void adicionarCidadeInexistenteEmConjunto() {

        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();

        var regra = regraViagemService.adicionarConjunto(
                new ConjuntoRequestDto("Conjunto Base", Set.of(aracaju.id()))
        );

        var conjunto =  regraViagemService.buscarConjuntosPorNome("Conjunto Base").getFirst();
        assertThatThrownBy(() ->
                regraViagemService.adicionarCidade(
                        conjunto.id(), Set.of(UUID.randomUUID())))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    @Test
    void adicionarCidadeEmConjuntoInexistente() {

        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();

        assertThatThrownBy(() ->
                regraViagemService.adicionarCidade(
                        UUID.randomUUID(), Set.of(aracaju.id())))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Conjunto de cidades não encontrado");
    }

    // =========================================================
    //  removerCidade (de conjunto existente)
    // =========================================================

    @Test
    void removerCidadeDeConjuntoComSucesso() {

        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();
        var lagarto = cidadeServiceImpl.buscarPorNome("Lagarto", PageRequest.of(0, 10))
                .content().getFirst();

        var regra = regraViagemService.adicionarConjunto(
                new ConjuntoRequestDto("Conjunto com Duas", Set.of(aracaju.id(), lagarto.id()))
        );
        var conjunto = regraViagemService.buscarConjuntosPorNome("Conjunto com Duas").getFirst();
        conjunto = regraViagemService.removerCidade(
                            conjunto.id(), lagarto.id());

                    assertThat(conjunto.cidades()).hasSize(1);
    }

    @Test
    void removerCidadeDeConjuntoInexistente() {

        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();

        assertThatThrownBy(() ->
                regraViagemService.removerCidade(UUID.randomUUID(), aracaju.id()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Conjunto de cidades não encontrado");
    }

    @Test
    void removerCidadeInexistenteDeConjunto() {

        var aracaju = cidadeServiceImpl.buscarPorNome("Aracaju", PageRequest.of(0, 10))
                .content().getFirst();

        var regra = regraViagemService.adicionarConjunto(
                new ConjuntoRequestDto("Conjunto Qualquer", Set.of(aracaju.id()))
        );

        var conjunto = regraViagemService.buscarConjuntosPorNome("Conjunto Qualquer").getFirst();

        assertThatThrownBy(() ->
                regraViagemService.removerCidade(conjunto.id(), UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }
}