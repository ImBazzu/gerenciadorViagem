package br.com.imbazzu.secretariaTransporte.segurança;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguracao {


    @Value("${api.security.origenspermitidas}")
    private List<String> origensPermitidas;

    //Filtro Token personalizado
    private final SecurityFiltroToken filtroToken;

    private final RetornoQueryConfig retornoQueryConfig;

    /**
     * Metodo para configurar os filtros de seguranças
     *
     * @param http classe que contem as informações de Segurança
     * @return filtro de Segurança configurado
     */
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) {

        return
                //Onde vamos configurar a segurança da aplicação
                http
                        //Desabilita o csrf por ser uma aplicação Rest
                        .csrf(AbstractHttpConfigurer::disable)

                        .cors(c->c.configurationSource(corsConfigurationSource()))
                        //Política de armazenamento de credencial do usuario desativo
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        //Política para as requisições para HttpRequests
                        .authorizeHttpRequests(request->{
                            //As requisições são inicialmente todas aceitas
                            request.anyRequest().permitAll();
                        })
                        //Após as requisições aceitas, o filtro personalizado verifica a permissão e autencidade
                        .addFilterBefore(filtroToken, UsernamePasswordAuthenticationFilter.class)
                        .addFilterBefore(retornoQueryConfig, SecurityFiltroToken.class)
                //Constrói o molde conforme as especificações
                .build();
    }

    /**
     * Criptografia da Senha
     *
     * @return Senha criptografada
     */
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Retorna a classe responsável pela autenticação do usuario
     *
     * @param http Responsável por gerar a classe de autenticação de usuario
     * @return classe para autenticação
     */
    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration http) {
        return http.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(origensPermitidas);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

}
