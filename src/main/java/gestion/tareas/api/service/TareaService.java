package gestion.tareas.api.service;

import gestion.tareas.api.dto.TareaDTO;
import gestion.tareas.api.entity.Tarea;
import gestion.tareas.api.entity.Proyecto; // Necesitas este import
import gestion.tareas.api.repository.TareaRepository;
import gestion.tareas.api.repository.ProyectoRepository; // Necesitas este import

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ProyectoRepository proyectoRepository; // Para buscar el Proyecto

    public TareaService(TareaRepository tareaRepository, ProyectoRepository proyectoRepository) {
        this.tareaRepository = tareaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    // --- 1. CREATE: Guardar nueva Tarea (POST) ---
    @Transactional
    public TareaDTO guardarTarea(TareaDTO dto) {
        Tarea tarea = convertirA_Entidad(dto);
        @SuppressWarnings("null")
        Tarea guardada = tareaRepository.save(tarea);
        return convertirA_DTO(guardada);
    }

    // --- 2. READ: Obtener todas las Tareas (GET /api/tareas) ---
    public List<TareaDTO> obtenerTodasLasTareas() {
        return tareaRepository.findAll().stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }
    
    // --- 3. READ: Obtener Tarea por ID (GET /api/tareas/{id}) ---
    public TareaDTO obtenerTareaPorId(Long id) {
        @SuppressWarnings("null")
        Tarea tarea = tareaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarea no encontrada con ID: " + id
            ));
        
        return convertirA_DTO(tarea);
    }

    // --- 4. UPDATE: Actualizar Tarea (PUT /api/tareas/{id}) ---
    @Transactional
    public TareaDTO actualizarTarea(Long id, TareaDTO dto) {
        // 1. Buscar la tarea existente (lanzará 404 si no existe)
        @SuppressWarnings("null")
        Tarea tareaExistente = tareaRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarea no encontrada para actualizar con ID: " + id
            ));
        
        // 2. Aplicar solo los cambios del DTO
        tareaExistente.setNombre(dto.getNombre());
        tareaExistente.setDescripcion(dto.getDescripcion());
        tareaExistente.setEstado(dto.getEstado()); 
        tareaExistente.setPrioridad(dto.getPrioridad());
        tareaExistente.setFechaInicio(dto.getFechaInicio());
        tareaExistente.setFechaFinal(dto.getFechaFinal());

        // 3. Opcional: Actualizar la clave foránea del proyecto si se proporciona
        if (dto.getIdProyecto() != null && !dto.getIdProyecto().equals(tareaExistente.getProyecto().getIdProyecto())) {
            @SuppressWarnings("null")
            Proyecto nuevoProyecto = proyectoRepository.findById(dto.getIdProyecto())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Proyecto no encontrado con ID: " + dto.getIdProyecto()
                ));
            tareaExistente.setProyecto(nuevoProyecto);
        }

        // 4. Guardar y devolver DTO
        Tarea actualizada = tareaRepository.save(tareaExistente);
        return convertirA_DTO(actualizada);
    }

    // --- 5. DELETE: Eliminar Tarea (DELETE /api/tareas/{id}) ---
    @SuppressWarnings("null")
    @Transactional
    public void eliminarTarea(Long id) {
        // Opcional: Verificar existencia antes de eliminar para devolver 404
        if (!tareaRepository.existsById(id)) {
             throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, 
                "Tarea no encontrada para eliminar con ID: " + id
            );
        }
        tareaRepository.deleteById(id);
    }

    // ==========================================================
    //                        MAPPERS (Convertidores)
    // ==========================================================

    // --- Entidad -> DTO ---
    private TareaDTO convertirA_DTO(Tarea tarea) {
        TareaDTO dto = new TareaDTO();
        dto.setIdTarea(tarea.getIdTarea());
        dto.setNombre(tarea.getNombre());
        dto.setDescripcion(tarea.getDescripcion());
        dto.setEstado(tarea.getEstado());
        dto.setPrioridad(tarea.getPrioridad());
        dto.setFechaInicio(tarea.getFechaInicio());
        dto.setFechaFinal(tarea.getFechaFinal());
        
        // Mapear la ID del Proyecto relacionado
        if (tarea.getProyecto() != null) {
            dto.setIdProyecto(tarea.getProyecto().getIdProyecto());
        }
        return dto;
    }

    // --- DTO -> Entidad ---
    private Tarea convertirA_Entidad(TareaDTO dto) {
        Tarea tarea = new Tarea();
        tarea.setNombre(dto.getNombre());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setEstado(dto.getEstado());
        tarea.setPrioridad(dto.getPrioridad());
        tarea.setFechaInicio(dto.getFechaInicio());
        tarea.setFechaFinal(dto.getFechaFinal());
        
        // ⚠️ CRÍTICO: Buscar y asignar el Proyecto
        if (dto.getIdProyecto() == null) {
             throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "El campo 'idProyecto' es obligatorio para crear una Tarea."
            );
        }
        
        @SuppressWarnings("null")
        Proyecto proyecto = proyectoRepository.findById(dto.getIdProyecto())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Proyecto no encontrado con ID: " + dto.getIdProyecto()
            ));
        
        tarea.setProyecto(proyecto);
        
        return tarea;
    }
}