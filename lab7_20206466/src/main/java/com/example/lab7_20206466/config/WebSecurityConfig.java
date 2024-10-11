package com.example.lab7_20206466.config;
import com.example.lab7_20206466.entity.*;
import com.example.lab7_20206466.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class WebSecurityConfig {

    final UsuarioRepository usuarioRepository;
    final DataSource dataSource;

    public WebSecurityConfig(DataSource dataSource, UsuarioRepository usuarioRepository) {
        this.dataSource = dataSource;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/openLoginWindow")
                .loginProcessingUrl("/submitLoginForm")
                .successHandler((request, response, authentication) -> {

                    DefaultSavedRequest defaultSavedRequest =
                            (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", usuarioRepository.findByEmail(authentication.getName()));


                    //si vengo por url -> defaultSR existe
                    if (defaultSavedRequest != null) {
                        String targetURl = defaultSavedRequest.getRequestURL();
                        new DefaultRedirectStrategy().sendRedirect(request, response, targetURl);
                    } else { //estoy viniendo del botón de login
                        String rol = "";
                        for (GrantedAuthority role : authentication.getAuthorities()) {
                            rol = role.getAuthority();
                            break;
                        }

                        switch (rol) {
                            case "ADMIN":
                                response.sendRedirect("/admin");
                                break;
                            case "GERENTE":
                                response.sendRedirect("/gerente");
                                break;
                            case "CLIENTE":
                                response.sendRedirect("/cliente");
                                break;
                            default:
                                response.sendRedirect("/default"); // Puedes cambiar esto según lo que necesites
                                break;
                        }
                    }
                });
        /*
            /employee -> ruta protegida -> rol admin y logistica (oscar.diaz|victor.chang)
            /shipper -> ruta protegida -> rol admin (oscar.diaz)
            todo lo demas (en este ejemplo, product) -> libre
         */
        http.authorizeHttpRequests()
                .requestMatchers("/admin", "/admin/**").hasAnyAuthority("ADMIN")
                .requestMatchers("/gerente", "/gerente/**").hasAnyAuthority("GERENTE")
                .requestMatchers("/gerente", "/gerente/**").hasAnyAuthority("CLIENTE")
                .anyRequest().permitAll();

        http.logout()
                .logoutSuccessUrl("/product")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        return http.build();
    }

    @Bean
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);

        // Devolver email, password y 'true' como enabled
        String sqlAuth = "SELECT email, password, true as enabled FROM users WHERE email = ?";

        // Consulta para obtener las autoridades (roles)
        String sqlAuto = "SELECT u.email, r.name FROM users u " +
                "INNER JOIN roles r ON u.roleId = r.id " +
                "WHERE u.email = ?";

        users.setUsersByUsernameQuery(sqlAuth);
        users.setAuthoritiesByUsernameQuery(sqlAuto);

        return users;
    }
}









