package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.CidadeApplicationImpl;
import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto.CidadeRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.infraestrutura.BancoDeDadosException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
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
public class CidadeServiceTest {

    @Autowired
    private CidadeApplicationImpl cidadeServiceImpl;

    @Test
    void buscarPorIdExistente() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ITABAIANA",
                        PageRequest.of(0,10))
                .content()
                .getFirst();

        var cidadeBuscada = cidadeServiceImpl.buscarPorId(cidade.id());

        assertThat(cidadeBuscada).isNotNull();
        assertThat(cidadeBuscada.nome()).isEqualTo("ITABAIANA");
    }

    @Test
    void buscarPorIdInexistente() {

        assertThatThrownBy(() ->
                cidadeServiceImpl.buscarPorId(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }
    @Test
    void buscarEntidadePorIdExistente() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ITABAIANA",
                        PageRequest.of(0,10))
                .content()
                .getFirst();

        var entidade = cidadeServiceImpl.buscarEntidadePorId(cidade.id());

        assertThat(entidade).isNotNull();
        assertThat(entidade.getNome()).isEqualTo("ITABAIANA");
    }

    @Test
    void buscarEntidadePorIdInexistente() {

        assertThatThrownBy(() ->
                cidadeServiceImpl.buscarEntidadePorId(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    @Test
    void salvarCidadeComSucesso() {

        var dto = new CidadeRequestDto("MACEIO",4,0);

        var cidadeSalva = cidadeServiceImpl.salvar(dto);

        assertThat(cidadeSalva).isNotNull();
        assertThat(cidadeSalva.id()).isNotNull();
        assertThat(cidadeSalva.nome()).isEqualTo("MACEIO");
    }

    @Test
    void salvarCidadeDuplicada() {

        var dto = new CidadeRequestDto("ARACAJU",3,0);

        assertThatThrownBy(() ->
                cidadeServiceImpl.salvar(dto))
                .isInstanceOf(BancoDeDadosException.class)
                .hasMessageContaining("Erro ao salvar");
    }

    @Test
    void editarComSucesso() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ITABAIANA",
                        PageRequest.of(0,10))
                .content()
                .getFirst();

        var dto = new CidadeRequestDto("MACEIO",4,0);

        var cidadeEditada =
                cidadeServiceImpl.editar(cidade.id(), dto);

        assertThat(cidadeEditada.id())
                .isEqualTo(cidade.id());

        assertThat(cidadeEditada.nome())
                .isEqualTo("MACEIO");
    }


    @Test
    void editarInexistente() {

        var dto = new CidadeRequestDto("MACEIO",5,0);

        assertThatThrownBy(() ->
                cidadeServiceImpl.editar(UUID.randomUUID(), dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    @Test
    void editarDuplicada() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ITABAIANA",
                        PageRequest.of(0,10))
                .content()
                .getFirst();

        var dto = new CidadeRequestDto("ARACAJU",3,0);

        assertThatThrownBy(() ->
                cidadeServiceImpl.editar(cidade.id(), dto))
                .isInstanceOf(BancoDeDadosException.class)
                .hasMessageContaining("Erro ao atualizar cidade");
    }

    @Test
    void deletarCidadeComSucesso() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ITABAIANA",
                        PageRequest.of(0,10))
                .content()
                .getFirst();

        cidadeServiceImpl.deletar(cidade.id());

        assertThatThrownBy(() ->
                cidadeServiceImpl.buscarPorId(cidade.id()))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deletarCidadeInexistente() {

        assertThatThrownBy(() ->
                cidadeServiceImpl.deletar(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    @Test
    void buscarPorNomeExistente() {

        var resultado = cidadeServiceImpl.buscarPorNome(
                "ITABAIANA",
                PageRequest.of(0,10));

        assertThat(resultado.content())
                .hasSize(1);

        assertThat(resultado.content()
                .getFirst()
                .nome())
                .isEqualTo("ITABAIANA");
    }

    @Test
    void buscarPorNomeInexistente() {

        var resultado = cidadeServiceImpl.buscarPorNome(
                "CIDADE_INEXISTENTE",
                PageRequest.of(0,10));

        assertThat(resultado.content())
                .isEmpty();
    }
}