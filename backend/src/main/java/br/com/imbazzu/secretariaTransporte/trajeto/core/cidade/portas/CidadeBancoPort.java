package br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas;

import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.Cidade;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.domain.CidadeEstadoEnum;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;

import java.util.Optional;
import java.util.UUID;

public interface CidadeBancoPort {

    Cidade salvar(Cidade cidade);

    Optional<Cidade> buscarPorId(UUID idCidade);

    ResultadoPaginado<Cidade> listar(String nome, int pagina, int tamanho);

    boolean verificarCidadeCadastrada(String nome, CidadeEstadoEnum estado);

    boolean verificarCidadeCadastradaIgnorandoId(UUID idCidade, String nomeCidade, CidadeEstadoEnum estado);
}
