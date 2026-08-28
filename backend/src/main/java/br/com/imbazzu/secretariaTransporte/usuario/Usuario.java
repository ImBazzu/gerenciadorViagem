package br.com.imbazzu.secretariaTransporte.usuario;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Table(name = "usuarios",
uniqueConstraints = {@UniqueConstraint(name = "uk_usuario_nome",columnNames = "nome")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String nome;
    @Setter
    private String senha;

    @Enumerated(EnumType.STRING)
    private UsuarioRoleEnum role;


    public Usuario(String login, String senha, String role) {
        this.nome = login;
        this.senha = senha;
        this.role = UsuarioRoleEnum.valueOf(role);
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ UsuarioRoleEnum.USUARIO.getRole()));

        if(this.role == UsuarioRoleEnum.ADMIN){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+ UsuarioRoleEnum.ADMIN.getRole()));
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return getNome();
    }
}
