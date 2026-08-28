package br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;

import java.util.Optional;
import java.util.UUID;

public interface DestinoBancoPort {

    Destino salvar(Destino destino);

    Optional<Destino> buscarPorId(UUID idDestino);

    ResultadoPaginado<Destino> listarPorCidade(UUID idCidade,String nome, int pagina, int tamanho);

    ResultadoPaginado<Destino> listar(String nome, int pagina, int tamanho);

    boolean verificarNomeEmDuplicidade(UUID idCidade, String nome);

    boolean verificarNomeEmDuplicidadeIgnorandoProprioDestino(UUID idCidade,String nome, UUID idDestino);
}
