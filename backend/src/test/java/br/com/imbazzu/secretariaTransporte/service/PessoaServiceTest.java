package br.com.imbazzu.secretariaTransporte.service;

import br.com.imbazzu.secretariaTransporte.compartilhados.dadosPessoais.dto.DadosPessoaisRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.gerais.EntidadeNaoEncontradoException;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.out.banco.pessoa.PessoaBancoRepository;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.PessoaApplicationImpl;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.domain.Pessoa;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaResponseDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(TestContainersConfig.class)
class PessoaServiceTest {

    @Autowired
    private PessoaApplicationImpl pessoaApplicationImpl;

    @Autowired
    private PessoaBancoRepository pessoaBancoRepository;

    @Test
    void deveSalvarPessoaComSucesso() {

        PessoaRequestDto dto = new PessoaRequestDto(
                new DadosPessoaisRequestDto("ROBERTO SILVA",
                "33060919011",
                "(75)99999-9999"),
                "Rua das Flores",
                PessoaTipoEnum.COMUM
        );

        PessoaResponseDto pessoa = pessoaApplicationImpl.salvar(dto);

        assertThat(pessoa).isNotNull();
        assertThat(pessoa.dadosPessoais().id()).isNotNull();
        assertThat(pessoa.dadosPessoais().nome()).isEqualTo("ROBERTO SILVA");
    }

    @Test
    void deveBuscarPessoaPorId() {

        Pessoa pessoaExistente =
                pessoaBancoRepository.findAll().getFirst();

        Pessoa pessoa =
                pessoaApplicationImpl.buscarEntidade(pessoaExistente.getId());

        assertThat(pessoa).isNotNull();
        assertThat(pessoa.getId()).isEqualTo(pessoaExistente.getId());
    }

    @Test
    void deveLancarExcecaoQuandoBuscarPessoaInexistente() {

        assertThatThrownBy(() ->
                pessoaApplicationImpl.buscarEntidade(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class)
                .hasMessageContaining("Paciente não encontrado");
    }

    @Test
    void deveBuscarListaPorNome() {

        ResultadoPaginado<PessoaResponseDto> resultado =
                pessoaApplicationImpl.buscarListaPorNome(
                        "SANTOS",
                        null,
                        PageRequest.of(0, 10)
                );

        assertThat(resultado).isNotNull();
        assertThat(resultado.content()).isNotEmpty();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoEncontrarNome() {

        ResultadoPaginado<PessoaResponseDto> resultado =
                pessoaApplicationImpl.buscarListaPorNome(
                        "NOME_INEXISTENTE",
                        PessoaTipoEnum.COMUM,
                        PageRequest.of(0, 10)
                );

        assertThat(resultado).isNotNull();
        assertThat(resultado.content()).isEmpty();
    }

    @Test
    void deveEditarPessoaComSucesso() {

        Pessoa pessoaExistente =
                pessoaBancoRepository.findAll().getFirst();

        PessoaRequestDto dto = new PessoaRequestDto(
                new  DadosPessoaisRequestDto(
                "NOME ALTERADO",
                "33060919011",
                "(75)99975-9899"),
                "Rua das Flores",
                pessoaExistente.getTipoPessoa()
        );

        PessoaResponseDto atualizado =
                pessoaApplicationImpl.editar(
                        pessoaExistente.getId(),
                        dto
                );

        assertThat(atualizado.dadosPessoais().nome()).isEqualTo("NOME ALTERADO");
    }

    @Test
    void deveLancarExcecaoAoEditarPessoaInexistente() {

        PessoaRequestDto dto = new PessoaRequestDto(
                new DadosPessoaisRequestDto(
                "Teste",
                "777.555.222-25",
                "75998974455"),
                "Rua São Miguel",
                PessoaTipoEnum.SOMENTE_CARRO
        );

        assertThatThrownBy(() ->
                pessoaApplicationImpl.editar(
                        UUID.randomUUID(),
                        dto
                ))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deveDeletarPessoaComSucesso() {

        Pessoa pessoaExistente =
                pessoaBancoRepository.findAll().getFirst();

        UUID id = pessoaExistente.getId();

        pessoaApplicationImpl.deletar(id);

        assertThatThrownBy(() ->
                pessoaApplicationImpl.buscarEntidade(id))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }

    @Test
    void deveLancarExcecaoAoDeletarPessoaInexistente() {

        assertThatThrownBy(() ->
                pessoaApplicationImpl.deletar(UUID.randomUUID()))
                .isInstanceOf(EntidadeNaoEncontradoException.class);
    }
}