package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino.dto.DestinoRequestDto;
import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.destino.dto.DestinoResponseDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.destino.portas.DestinoApplicationPort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/destino")
public class DestinoController {

    private final DestinoApplicationPort destinoApplicationPort;

    private DestinoController(DestinoApplicationPort destinoApplicationPort) {
        this.destinoApplicationPort = destinoApplicationPort;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/salvar")
    public ResponseEntity<DestinoResponseDto> salvarDestino(
            @Valid @RequestParam DestinoRequestDto dto
    ) {
        var result = destinoApplicationPort.salvar(
                dto.idCidade(),
                dto.nome()
        );
        var response = DestinoControllerMapper.paraDestinoResponse(result);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/editar/{idDestino}")
    public ResponseEntity<DestinoResponseDto> editarDestino(
            @PathVariable UUID idDestino,
            @RequestParam String nome
    ) {
        var result = destinoApplicationPort.editar(
                idDestino,
                nome
        );
        var response = DestinoControllerMapper.paraDestinoResponse(result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/arquivar/{idDestino}")
    public ResponseEntity<Void> arquivarDestino(
            @PathVariable UUID idDestino
    ) {
        destinoApplicationPort.arquivar(idDestino);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/desarquivar/{idDestino}")
    public ResponseEntity<Void> desarquivarDestino(
            @PathVariable UUID idDestino
    ) {
        destinoApplicationPort.desarquivar( idDestino);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{idCidade}/destinos")
    public ResponseEntity<ResultadoPaginado<DestinoResponseDto>> buscarDestinos(
            @PathVariable UUID idCidade,
            @RequestParam(name = "nome", defaultValue = "") String nome,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable
    ) {
        var result = destinoApplicationPort.buscarPorNomeECidade(
                idCidade,
                nome,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        var response = result.map(DestinoControllerMapper::paraDestinoResponse);
        return ResponseEntity.ok(response);
    }
}
