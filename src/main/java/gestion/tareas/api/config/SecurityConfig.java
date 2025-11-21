package gestion.tareas.api.config; // ⬅️ Asegúrate que coincida con tu estructura

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig { 
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilita CSRF (ya lo tienes)
            .csrf(csrf -> csrf.disable()) 
            
            // 🟢 AÑADE: Deshabilita la gestión de sesiones por defecto (Stateless)
            // Esto es CRUCIAL para una API REST
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 🟢 AÑADE: Deshabilita la autenticación de formularios por defecto
            .formLogin(AbstractHttpConfigurer::disable)
            
            // 🟢 AÑADE: Deshabilita la autenticación básica HTTP por defecto
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // 2. Configura las autorizaciones de las solicitudes
            .authorizeHttpRequests(authorize -> authorize
                // Mantén el acceso libre a TODAS las rutas que empiezan con /api/
                .requestMatchers("/api/**").permitAll()
                
                // ... (otras reglas)
                
                // 3. Cualquier otra solicitud DEBE estar autenticada
                .anyRequest().authenticated()
            );

        return http.build();
    }
    // ... (PasswordEncoder bean)
}
