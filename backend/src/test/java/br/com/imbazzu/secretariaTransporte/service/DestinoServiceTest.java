package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.trajeto.application.destino.dto.DestinoSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeDuplicadaException;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.trajeto.application.cidade.CidadeApplicationImpl;
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
public class DestinoServiceTest {

    @Autowired
    private DestinoService destinoService;

    @Autowired
    private CidadeApplicationImpl cidadeServiceImpl;

    // =========================================================
    //  buscarPorNome
    // =========================================================

    @Test
    void buscarPorNomeExistente() {

        var resultado = destinoService.buscarPorNome(
                "Hospital de Cirurgia",
                "ARACAJU",
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .hasSize(1);

        assertThat(resultado.content()
                .getFirst()
                .nome())
                .isEqualTo("HOSPITAL DE CIRURGIA");
    }

    @Test
    void buscarPorNomeInexistente() {

        var resultado = destinoService.buscarPorNome(
                "DESTINO_INEXISTENTE",
                "",
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .isEmpty();
    }

    @Test
    void buscarPorNomeParcialRetornaMultiplos() {

        // "Hospital de Cirurgia", "Hospital Universitario" e "Hospital Regional"
        var resultado = destinoService.buscarPorNome(
                "Hospital",
                "",
                PageRequest.of(0, 10));

        assertThat(resultado.content())
                .hasSizeGreaterThanOrEqualTo(3);
    }

    // =========================================================
    //  buscarPorId
    // =========================================================

    @Test
    void buscarEntidadePorIdExistente() {

        var destino = destinoService.buscarPorNome(
                        "Clinica São Lucas",
                        "ITABAIANA",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var destinoBuscado = destinoService.buscarEntidadePorId(destino.id());

        assertThat(destinoBuscado).isNotNull();
        assertThat(destinoBuscado.nome()).isEqualTo("CLINICA SÃO LUCAS");
    }

    @Test
    void buscarEntidadePorIdInexistente() {

        assertThatThrownBy(() ->
                destinoService.buscarEntidadePorId(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Destino não encontrado");
    }

    // =========================================================
    //  buscarEntidadePorId
    // =========================================================

    @Test
    void buscarEntidadePorIdExistente() {

        var destino = destinoService.buscarPorNome(
                        "Hospital Regional",
                        "LAGARTO",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var entidade = destinoService.buscarEntidadePorId(destino.id());

        assertThat(entidade).isNotNull();
        assertThat(entidade.getNome()).isEqualTo("HOSPITAL REGIONAL");
    }

    @Test
    void buscarEntidadePorIdInexistente() {

        assertThatThrownBy(() ->
                destinoService.buscarEntidadePorId(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Destino não encontrado");
    }

    // =========================================================
    //  salvar
    // =========================================================

    @Test
    void salvarDestinoComSucesso() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "Lagarto",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new DestinoSalvarInputDto("UPA Lagarto", cidade.id());

        var destinoSalvo = destinoService.salvar(dto);

        assertThat(destinoSalvo).isNotNull();
        assertThat(destinoSalvo.id()).isNotNull();
        assertThat(destinoSalvo.nome()).isEqualTo("UPA LAGARTO");
    }

    @Test
    void salvarDestinoDuplicado() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "ARACAJU",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        // "Hospital de Cirurgia" já existe no seed para Aracaju
        var dto = new DestinoSalvarInputDto("HOSPITAL DE CIRURGIA", cidade.id());

        assertThatThrownBy(() ->
                destinoService.salvar(dto))
                .isInstanceOf(EntidadeDuplicadaException.class);
    }

    @Test
    void salvarDestinoComCidadeInexistente() {

        var dto = new DestinoSalvarInputDto("Novo Destino", UUID.randomUUID());

        assertThatThrownBy(() ->
                destinoService.salvar(dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    // =========================================================
    //  editar
    // =========================================================

    @Test
    void editarDestinoComSucesso() {

        var destino = destinoService.buscarPorNome(
                        "Clinica Santa Maria",
                        "",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "Itabaiana",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new DestinoSalvarInputDto("Clinica Santa Maria Editada", cidade.id());

        var destinoEditado = destinoService.editar(destino.id(), dto);

        assertThat(destinoEditado.id()).isEqualTo(destino.id());
        assertThat(destinoEditado.nome()).isEqualTo("CLINICA SANTA MARIA EDITADA");
    }

    @Test
    void editarDestinoInexistente() {

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "Lagarto",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new DestinoSalvarInputDto("Qualquer Nome", cidade.id());

        assertThatThrownBy(() ->
                destinoService.editar(UUID.randomUUID(), dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Destino não encontrado");
    }

    @Test
    void editarDestinoComCidadeInexistente() {

        var destino = destinoService.buscarPorNome(
                        "Hospital Regional",
                        "LAGARTO",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new DestinoSalvarInputDto("Hospital Regional", UUID.randomUUID());

        assertThatThrownBy(() ->
                destinoService.editar(destino.id(), dto))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Cidade não encontrada");
    }

    @Test
    void editarDestinoDuplicado() {

        // Tenta renomear "Hospital Universitario" para "Hospital de Cirurgia"
        // que já existe na mesma cidade (Aracaju)
        var destino = destinoService.buscarPorNome(
                        "Hospital Universitario",
                        "ARACAJU",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var cidade = cidadeServiceImpl.buscarPorNome(
                        "Aracaju",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        var dto = new DestinoSalvarInputDto("Hospital de Cirurgia", cidade.id());

        assertThatThrownBy(() ->
                destinoService.editar(destino.id(), dto))
                .isInstanceOf(EntidadeDuplicadaException.class);
    }

    // =========================================================
    //  deletar
    // =========================================================

    @Test
    void deletarDestinoComSucesso() {

        var destino = destinoService.buscarPorNome(
                        "Clinica São Lucas",
                        "ITABAIANA",
                        PageRequest.of(0, 10))
                .content()
                .getFirst();

        destinoService.deletar(destino.id());

        assertThatThrownBy(() ->
                destinoService.buscarEntidadePorId(destino.id()))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deletarDestinoInexistente() {

        assertThatThrownBy(() ->
                destinoService.deletar(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Destino não encontrado");
    }
}