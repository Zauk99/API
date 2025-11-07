package gestion.tareas.api.repository;

import gestion.tareas.api.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Aquí puedes añadir métodos de búsqueda personalizados,
    // por ejemplo: Usuario findByEmail(String email);
}