package br.com.imbazzu.secretariaTransporte.usuario;

import br.com.imbazzu.secretariaTransporte.segurança.LoginTentativasService;
import br.com.imbazzu.secretariaTransporte.segurança.TokenServiceJwt;
import br.com.imbazzu.secretariaTransporte.usuario.dto.TokenResponseDto;
import br.com.imbazzu.secretariaTransporte.usuario.dto.UsuarioRequestDto;
import br.com.imbazzu.secretariaTransporte.compartilhados.excecoes.autenticacao.UsuarioNaoAutorizadoException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;

    private final PasswordEncoder encoder;

    private final TokenServiceJwt tokenService;

    private final AuthenticationManager authManager;

    private final LoginTentativasService loginTentativasService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repo.findByNome(username).orElseThrow(
                ()->new UsernameNotFoundException("Usuario não encontrado")
        );
    }


    public TokenResponseDto login(String login, String senha, HttpServletRequest request) {
        String chave = request.getRemoteAddr() + ":" + login;
        loginTentativasService.validarBloqueio(chave);
        try{
            var usuarioSenha = new UsernamePasswordAuthenticationToken(login, senha);
            var auth = this.authManager.authenticate(usuarioSenha);

            var usuario = (Usuario) auth.getPrincipal();

            loginTentativasService.limparTentativas(chave);

            var tokenAcesso = tokenService.gerarToken(usuario);
            var tokenAtualizar = tokenService.gerarAttToken(usuario);

            return new TokenResponseDto(tokenAcesso, tokenAtualizar);
        }catch(BadCredentialsException | UsernameNotFoundException e){
            loginTentativasService.registrarFalha(chave);
            throw e;
        }

    }

    public TokenResponseDto atualizar(String refreshToken) {
        var decode = tokenService.validarToken(refreshToken);
        var usuario = (Usuario) this.loadUserByUsername(decode.getSubject());

        var tokenAcesso = tokenService.gerarToken(usuario);
        var tokenAtualizar = tokenService.gerarAttToken(usuario);
        return new TokenResponseDto(tokenAcesso, tokenAtualizar);
    }

    public boolean verificarUsuario(String usuario) {
        return this.repo.existsByNome(usuario);
    }

    public void salvarAdm(UsuarioRequestDto dto) {
        this.salvar(dto, UsuarioRoleEnum.ADMIN.getRole());
    }

    public void salvarUsuario(UsuarioRequestDto dto) {
        this.salvar(dto, UsuarioRoleEnum.USUARIO.getRole());
    }

    private void salvar(UsuarioRequestDto dto, String role) {
        if(verificarUsuario(dto.login())){
            throw new UsuarioNaoAutorizadoException("Usuario ja cadastrado");
        }

        String senhaCriptografada = encoder.encode(dto.senha());


        var usuario = new Usuario(dto.login(),senhaCriptografada, role);
        this.repo.save(usuario);

    }
}
