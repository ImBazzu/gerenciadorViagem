package br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista;

import br.com.imbazzu.secretariaTransporte.compartilhados.domain.pagination.ResultadoPaginado;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.command.MotoristaApplicationCommandImpl;
import br.com.imbazzu.secretariaTransporte.frota.application.motorista.query.MotoristaApplicationQueryImpl;
import br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto.MotoristaRequestDto;
import br.com.imbazzu.secretariaTransporte.frota.adapters.in.motorista.dto.MotoristaResponseDto;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.command.MotoristaApplicationCommandPorta;
import br.com.imbazzu.secretariaTransporte.frota.core.portas.motorista.query.MotoristaApplicationQueryPorta;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * Responsável pela interação do navegador e o servidor
 */
@RestController
@RequestMapping("motorista")
public class MotoristaController {

    //Responsável por conter a lógica do Motorista
    private final MotoristaApplicationQueryPorta query;

    private final MotoristaApplicationCommandPorta command;

    public MotoristaController(MotoristaApplicationCommandImpl command, MotoristaApplicationQueryImpl query) {
        this.command = command;
        this.query = query;
    }

    /**
     * Responsável por receber a requisição para a procura pelo motorista com base no nome
     *
     * @param nome nome do motorista procurado
     * @param pagina pagina procurado
     * @return retorna para o navegador a lista do motorista
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResultadoPaginado<MotoristaResponseDto>> buscarPorNome(
            @RequestParam(name = "nome", defaultValue = "") String nome,
            @PageableDefault(size = 20,sort = "nome") Pageable pagina) {
        //procura o motorista pelo nome

        var result = query.buscarPorNome(nome, pagina.getPageNumber(), pagina.getPageSize());
        var response = result.map(MotoristaControllerMapper::paraResponseDto);
        //Retorna a lista para o navegador
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar o motorista pelo o identificador dele
     *
     * @param id identificador
     * @return motorista encontrado
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MotoristaResponseDto> buscarPorId(@PathVariable UUID id) {
        //motorista encontrado
        var result = query.buscarPorId(id);
        var response = MotoristaControllerMapper.paraResponseDto(result);
        //Retorna para o navegador
        return ResponseEntity.ok(response);
    }

    /**
     * Delete o motorista usando o identificador
     *
     * @param id identificador
     * @return nada
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> arquivar(@PathVariable UUID id) {
        //Deleta o motorista
        command.arquivar(id);
        //Retorna o código que deu tudo certo ao deletar
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete o motorista usando o identificador
     *
     * @param id identificador
     * @return nada
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desarquivar(@PathVariable UUID id) {
        //Deleta o motorista
        command.desarquivar(id);
        //Retorna o código que deu tudo certo ao deletar
        return ResponseEntity.noContent().build();
    }

    /**
     * Salva um novo motorista no banco de dados
     * @param dto contendo as informações do novo motorista
     * @return Dto do novo motorista cadastrado no banco
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MotoristaResponseDto> salvar(@RequestBody @Valid MotoristaRequestDto dto) {
        //Salvando o motorista no banco de dados
        var dtoCommand = MotoristaControllerMapper.paraCommandSalvarDto(dto);
        UUID idSalvo =command.salvar(dtoCommand);
        var result = query.buscarPorId(idSalvo);
        var response = MotoristaControllerMapper.paraResponseDto(result);
        //Uri do para caso precise realizar um get para a procura do motorista salvo
        URI uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/motorista/{id}")
                .buildAndExpand(idSalvo)
                .toUri();
        //Retorno no banco
        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Busca o motorista usando o Id e o edita
     * @param id identificador do motorista
     * @param dto novos dados do motorista
     * @return retorna o motorista editado
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MotoristaResponseDto> editar(@PathVariable UUID id,
            @RequestBody @Valid MotoristaRequestDto dto) {
        //Motorista editado
        var dtoCommand = MotoristaControllerMapper.paraCommandSalvarDto(dto);
        command.editar(id, dtoCommand);
        var result =  query.buscarPorId(id);
        var response =  MotoristaControllerMapper.paraResponseDto(result);
        //Retorna o motorista editado e ok
        return ResponseEntity.ok(response);
    }

}
