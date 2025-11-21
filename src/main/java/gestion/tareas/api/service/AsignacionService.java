package gestion.tareas.api.service;

import gestion.tareas.api.dto.AsignacionDTO;
import gestion.tareas.api.entity.Asignacion;
import gestion.tareas.api.repository.AsignacionRepository;
import gestion.tareas.api.repository.TareaRepository;
import gestion.tareas.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;;

@Service
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias
    public AsignacionService(AsignacionRepository repo, TareaRepository tRepo, UsuarioRepository uRepo) {
        this.asignacionRepository = repo;
        this.tareaRepository = tRepo;
        this.usuarioRepository = uRepo;
    }

    // ... Implementar obtenerTodasLasAsignaciones() ...
    public List<AsignacionDTO> obtenerTodasLasAsignaciones() {
    
    // 1. Obtener todas las entidades del repositorio
    return asignacionRepository.findAll()
            
            // 2. Crear un stream para procesar la lista
            .stream()
            
            // 3. Mapear cada entidad Asignacion a su DTO correspondiente
            .map(this::convertirA_DTO) 
            
            // 4. Recolectar en una lista de DTOs
            .collect(Collectors.toList());
        }

    @SuppressWarnings("null")
    public AsignacionDTO guardarAsignacion(AsignacionDTO dto) {
        Asignacion asignacion = new Asignacion();

        // 1. Buscar Entidades: Esto es la clave para persistir FKs
        // (Asegúrate de implementar el orElseThrow para IDs no encontrados)
        asignacion.setTarea(tareaRepository.findById(dto.getIdTarea())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada")));
        asignacion.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")));

        // 2. Guardar
        Asignacion guardada = asignacionRepository.save(asignacion);

        // 3. Devolver DTO de respuesta
        return convertirA_DTO(guardada);
    }

    // ===================================
    // 3. READ ONE: Obtener por ID (GET /api/asignaciones/{id})
    // ===================================
    public AsignacionDTO obtenerAsignacionPorId(Long id) {
        @SuppressWarnings("null")
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignación no encontrada con ID: " + id));
        return convertirA_DTO(asignacion);
    }

    // ===================================
    // 4. UPDATE: Actualizar por ID (PUT /api/asignaciones/{id})
    // ⚠️ Si solo tienes referencias y fecha de creación, este método solo actualiza campos
    // que decidas agregar (ej. prioridad). Lo dejamos funcional, asumiendo que el DTO
    // no requiere los IDs de Tarea/Usuario para actualizar.
    // ===================================
    @Transactional
    public AsignacionDTO actualizarAsignacion(Long id, AsignacionDTO dto) {
        @SuppressWarnings("null")
        Asignacion asignacionExistente = asignacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Asignación no encontrada con ID: " + id));

        // Aquí iría la lógica para actualizar campos editables de Asignacion.
        // Ej: asignacionExistente.setEstado(dto.getEstado());

        @SuppressWarnings("null")
        Asignacion asignacionActualizada = asignacionRepository.save(asignacionExistente);
        return convertirA_DTO(asignacionActualizada);
    }

    // ===================================
    // 5. DELETE: Eliminar por ID (DELETE /api/asignaciones/{id})
    // ===================================
    @SuppressWarnings("null")
    @Transactional
    public void eliminarAsignacion(Long id) {
        if (!asignacionRepository.existsById(id)) {
            throw new EntityNotFoundException("Asignación no encontrada con ID: " + id);
        }
        asignacionRepository.deleteById(id);
    }

    // Mapeo Entidad -> DTO
    private AsignacionDTO convertirA_DTO(Asignacion asignacion) {
        AsignacionDTO dto = new AsignacionDTO();
        dto.setIdAsignacion(asignacion.getIdAsignacion());
        dto.setIdTarea(asignacion.getTarea().getIdTarea());
        dto.setIdUsuario(asignacion.getUsuario().getIdUsuario());
        dto.setFechaAsignacion(asignacion.getFechaAsignacion());
        return dto;
    }
}