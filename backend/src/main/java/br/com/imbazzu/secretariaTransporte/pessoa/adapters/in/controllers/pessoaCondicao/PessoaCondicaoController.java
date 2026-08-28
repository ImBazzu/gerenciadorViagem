package br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.pessoa.application.pessoaCondicao.PessoaCondicaoApplication;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto.PessoaCondicaoRequestDto;
import br.com.imbazzu.secretariaTransporte.pessoa.adapters.in.controllers.pessoaCondicao.dto.PessoaCondicaoResponseDto;
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
@RequestMapping("justificativa")
public class PessoaCondicaoController {

    private final PessoaCondicaoApplication service;

    @GetMapping({"{id}"})
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PessoaCondicaoResponseDto> buscarPorId(@PathVariable UUID id){
        var result = service.buscarPorId(id);
        var response = PessoaCondicaoMapper.converterDto(result);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<ResultadoPaginado<PessoaCondicaoResponseDto>>buscarTodos(
            @RequestParam(name = "procurar", defaultValue = "") String procura,
            @PageableDefault(size=20,sort = "name")
            Pageable pageable){
        var result = service.buscarPorNome(procura,pageable.getPageNumber(),pageable.getPageSize());
        var response = result.map(PessoaCondicaoMapper::converterDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PessoaCondicaoResponseDto>criar(
            @Valid @RequestParam PessoaCondicaoRequestDto dto){
        var pessoaSalvarInput = PessoaCondicaoMapper.paraInputSalvar(dto);
        var result = service.salvar(pessoaSalvarInput);
        var response = PessoaCondicaoMapper.converterDto(result);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> desabilitar(UUID id){
        service.desarquivar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PessoaCondicaoResponseDto> editar(
            @PathVariable UUID id, @Valid @RequestBody PessoaCondicaoRequestDto dto){
        var inputDto = PessoaCondicaoMapper.paraInputSalvar(dto);
        var result = service.editar(id, inputDto);
        var response =  PessoaCondicaoMapper.converterDto(result);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("{id}/habilitar")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> arquivar(UUID id){
        service.arquivar(id);
        return ResponseEntity.ok().build();
    }
}
