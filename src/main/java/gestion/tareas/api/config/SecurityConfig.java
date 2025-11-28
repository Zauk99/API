package gestion.tareas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF (Crucial para API REST)
                .csrf(csrf -> csrf.disable())

                // 2. Deshabilitar gestión de sesiones web (opcional, pero buena práctica para
                // REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configurar la autorización: Permitir acceso a la API
                .authorizeHttpRequests(auth -> auth
                        // Permitir el acceso a todas las rutas de la API (el único propósito de esta
                        // configuración)
                        .requestMatchers("/api/**").permitAll()
                        // Requerir autenticación (o denegar) cualquier otra cosa, si es necesario.
                        .anyRequest().authenticated());

        // 🛑 4. ELIMINA EL BLOQUE .logout(...) COMPLETO. NO PERTENECE AQUÍ.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}