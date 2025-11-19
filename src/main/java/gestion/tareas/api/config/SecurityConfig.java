package gestion.tareas.api.config; // ⬅️ Asegúrate que coincida con tu estructura

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig { 


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilita la protección CSRF para APIs REST
            .csrf(csrf -> csrf.disable()) 
            
            // 2. Configura las autorizaciones de las solicitudes
            .authorizeHttpRequests(authorize -> authorize
                
                // 🟢 PERMITE acceso libre a TODAS las rutas que empiezan con /api/
                // Si quieres solo tareas: .requestMatchers("/api/tareas/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                
                // 🟢 PERMITE acceso libre a recursos estáticos (CSS, JS, Imágenes) y JSPs (Vistas)
                .requestMatchers("/", "/dashboard", "/css/**", "/js/**", "/img/**").permitAll()
                
                // 3. Cualquier otra solicitud DEBE estar autenticada
                .anyRequest().authenticated()
            );

        return
         http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}