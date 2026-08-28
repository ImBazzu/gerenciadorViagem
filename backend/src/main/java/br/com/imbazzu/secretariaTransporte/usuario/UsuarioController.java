package br.com.imbazzu.secretariaTransporte.usuario;

import br.com.imbazzu.secretariaTransporte.usuario.dto.TokenRequestDto;
import br.com.imbazzu.secretariaTransporte.usuario.dto.TokenResponseDto;
import br.com.imbazzu.secretariaTransporte.usuario.dto.UsuarioRequestDto;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;



    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid UsuarioRequestDto dto,
                                                  HttpServletRequest request) {
        var tokenRetorno=  service.login(dto.login(), dto.senha(), request);
        // adicionarTokenAtualizarNoCookie(response,tokenAtualizar);
        return ResponseEntity.ok().body(tokenRetorno);
    }

    @PostMapping("/atualizarToken")
    @PermitAll
    public ResponseEntity<TokenResponseDto> atualizar(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        var tokenRetorno = service.atualizar(tokenRequestDto.refreshToken());
        return ResponseEntity.ok().body(tokenRetorno);
    }

    @PostMapping("/registrar")
    @PermitAll
    public ResponseEntity<String> registrar(@RequestBody @Valid UsuarioRequestDto dto) {
        this.service.salvarUsuario(dto);
        return ResponseEntity.ok().body("Usuario cadastrado com sucesso");
    }

    @PostMapping("/registrarAdm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registrarAdm(@RequestBody @Valid UsuarioRequestDto dto) {

        this.service.salvarAdm(dto);
        return ResponseEntity.ok().body("Administrador cadastrado com sucesso");
    }

    private void adicionarTokenAtualizarNoCookie(HttpServletResponse response,
            String token) {
        var cookie = ResponseCookie.from("tokenAtualizar", token)
                .httpOnly(true)
                .path("/auth/atualizarToken")
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
