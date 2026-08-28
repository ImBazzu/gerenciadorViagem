package br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade;

import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto.CidadeResponseDto;
import br.com.imbazzu.secretariaTransporte.trajeto.adapters.in.controllers.cidade.dto.CidadeRequestDto;
import br.com.imbazzu.secretariaTransporte.trajeto.core.cidade.portas.CidadeApplicationPort;
import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cidade")
public class CidadeController {

    private final CidadeApplicationPort application;

    // ============================================================
    // CIDADE
    // ============================================================
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponseDto> buscarPorId(
            @PathVariable UUID id
    ) {
        var result = application.buscarPorId(id);

        var response = CidadeControllerMapper.paraResponse(result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<ResultadoPaginado<CidadeResponseDto>> buscarPorNome(
            @RequestParam(name = "nome", defaultValue = "") String nome,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable
    ) {
        var result = application.buscarPorNome(
                nome,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        var response = result.map(
                CidadeControllerMapper::paraResponse
        );

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CidadeResponseDto> salvarCidade(
            @RequestBody @Valid CidadeRequestDto request
    ) {
        var command = CidadeControllerMapper.paraSalvarInput(request);

        var result = application.salvar(command);

        var response = CidadeControllerMapper.paraResponse(result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponseDto> editarCidade(
            @PathVariable UUID id,
            @RequestBody @Valid CidadeRequestDto request
    ) {
        var command = CidadeControllerMapper.paraSalvarInput(request);

        var result = application.editar(id, command);

        var response = CidadeControllerMapper.paraResponse(result);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/arquivar")
    public ResponseEntity<Void> arquivarCidade(
            @PathVariable UUID id
    ) {
        application.arquivar(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{id}/desarquivar")
    public ResponseEntity<Void> desarquivarCidade(
            @PathVariable UUID id
    ) {
        application.desarquivar(id);

        return ResponseEntity.noContent().build();
    }




}