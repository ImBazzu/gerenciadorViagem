package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa;

import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoa.dto.PessoaSalvarInputDto;
import br.com.imbazzu.secretariaTransporte.pessoa.core.pessoa.portas.PessoaApplicationPorta;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoa.dto.PessoaResponseDto;
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
@RequestMapping("paciente")
public class PessoaController {

    private final PessoaApplicationPorta application;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResultadoPaginado<PessoaResponseDto>> buscarListaPorNome(
            @RequestParam(name = "nome", defaultValue = "") String nome,
            @PageableDefault(size = 20, sort = "nome") Pageable pagina) {
        var result = application.listarPorNome(nome, pagina.getPageNumber(), pagina.getPageSize());
        var response = result.map(PessoaControllerMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PessoaResponseDto> buscarPorId(@PathVariable UUID id) {
        var result = application.buscarPorId(id);
        return ResponseEntity.ok(PessoaControllerMapper.toResponse(result));
    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PessoaResponseDto> editar(@PathVariable UUID id,
            @Valid @RequestBody PessoaRequestDto dto) {
        var result = application.editar(id, new PessoaSalvarInputDto(
                dto.nome(),dto.cpf(),dto.idCondicao(),dto.telefone(),dto.endereco(),dto.observacao()
        ));
        var response =  PessoaControllerMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PessoaResponseDto> salvar(@RequestBody @Valid PessoaRequestDto dto) {
        var pessoaSalvarInput = PessoaControllerMapper.toInputDto(dto);
        var result = application.salvar(pessoaSalvarInput);
        var response =   PessoaControllerMapper.toResponse(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/arquivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> arquivar(@PathVariable UUID idPessoa){
        application.arquivar(idPessoa);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/desarquivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desarquivar(@PathVariable UUID idPessoa){
        application.desarquivar(idPessoa);
        return ResponseEntity.ok().build();
    }
}
