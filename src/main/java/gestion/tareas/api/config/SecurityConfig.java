package gestion.tareas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// ❌ ELIMINAMOS: import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// ❌ ELIMINAMOS: import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
public class SecurityConfig {
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilita CSRF (Esencial para API REST)
            .csrf(csrf -> csrf.disable())
           
            // 2. Deshabilita la gestión de sesiones (Stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           
            // 3. Deshabilita la autenticación de formularios y básica por defecto
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
           
            // 4. Configura las autorizaciones
            .authorizeHttpRequests(authorize -> authorize
                // ✅ Permite acceso libre a todas las rutas que empiezan con /api/ (Incluye el registro)
                .requestMatchers("/api/**").permitAll()
               
                // Permite acceso libre a recursos estáticos
                .requestMatchers("/", "/dashboard", "/css/**", "/js/**", "/img/**").permitAll()
               
                // Cualquier otra solicitud DEBE estar autenticada
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // ❌ ELIMINAMOS el bean de PasswordEncoder

    /*
    // Si necesitas volver a usar un PasswordEncoder que no sea bcrypt (por ejemplo, NoOpPasswordEncoder)
    // tendrías que definir el bean aquí y volver a inyectarlo en el servicio.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Requiere contraseña en DB con prefijo {noop}
    }
    */
}