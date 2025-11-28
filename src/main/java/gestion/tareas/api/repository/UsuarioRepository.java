package gestion.tareas.api.repository;

import gestion.tareas.api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Aquí puedes añadir métodos de búsqueda personalizados,
    // por ejemplo: Usuario findByEmail(String email);
    // Spring Data JPA automáticamente implementará este método:
    Usuario findByEmail(String email);
}