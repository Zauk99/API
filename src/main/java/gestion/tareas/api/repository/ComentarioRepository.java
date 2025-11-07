package gestion.tareas.api.repository;

import gestion.tareas.api.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    // Aquí puedes añadir métodos de búsqueda personalizados,
    // por ejemplo: Usuario findByEmail(String email);
}