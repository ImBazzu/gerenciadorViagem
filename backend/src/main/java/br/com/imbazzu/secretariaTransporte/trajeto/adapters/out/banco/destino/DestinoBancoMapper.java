package br.com.imbazzu.secretariaTransporte.trajeto.adapters.out.banco.destino;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.valueObject.Nome;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.domain.Destino;

public class DestinoBancoMapper {

    public static DestinoBanco paraEntity(Destino destino) {

        return new DestinoBanco(
                destino.getId(),
                destino.getNome().valor(),
                destino.getIdCidade(),
                destino.isArquivado());
    }

    public static Destino paraDomain(DestinoBanco destino) {

        return Destino.reconstruir(
                destino.getId(),
                destino.getCidadeId(),
                new Nome(destino.getNome()),
                destino.isDeletado());
    }
}
