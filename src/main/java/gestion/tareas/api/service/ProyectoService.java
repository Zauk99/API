package gestion.tareas.api.service;

import gestion.tareas.api.entity.Proyecto;
import gestion.tareas.api.dto.ProyectoDTO;
import gestion.tareas.api.repository.ProyectoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Autowired
    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    // --- Lógica GET (Obtener todos) ---
    public List<ProyectoDTO> obtenerTodosLosProyectos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    // --- Lógica POST (Guardar) ---
    public ProyectoDTO guardarProyecto(ProyectoDTO proyectoDTO) {
        Proyecto nuevoProyecto = convertirA_Entidad(proyectoDTO);
        @SuppressWarnings("null")
        Proyecto proyectoGuardado = proyectoRepository.save(nuevoProyecto);
        return convertirA_DTO(proyectoGuardado);
    }

    // ===================================
    // 3. READ ONE: Obtener por ID (GET /api/proyectos/{id})
    // ===================================
    public ProyectoDTO obtenerProyectoPorId(Long id) {
        @SuppressWarnings("null")
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado con ID: " + id));
        return convertirA_DTO(proyecto);
    }

    // ===================================
    // 4. UPDATE: Actualizar por ID (PUT /api/proyectos/{id})
    // ===================================
    @Transactional
    public ProyectoDTO actualizarProyecto(Long id, ProyectoDTO dto) {
        // 1. Buscar la entidad existente
        @SuppressWarnings("null")
        Proyecto proyectoExistente = proyectoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proyecto no encontrado con ID: " + id));

        // 2. Aplicar los cambios del DTO
        // Solo actualizamos campos modificables
        if (dto.getNombre() != null) {
            proyectoExistente.setNombre(dto.getNombre());
        }
        // Asumiendo que has añadido getDescripcion() y setDescripcion() al DTO/Entidad
        if (dto.getDescripcion() != null) {
            proyectoExistente.setDescripcion(dto.getDescripcion());
        }
        // (Otros campos como fechaCreacion no se actualizan)

        // 3. Guardar (save() actualizará la entidad existente gracias al ID)
        @SuppressWarnings("null")
        Proyecto proyectoActualizado = proyectoRepository.save(proyectoExistente);

        // 4. Devolver el DTO del resultado
        return convertirA_DTO(proyectoActualizado);
    }

    // ===================================
    // 5. DELETE: Eliminar por ID (DELETE /api/proyectos/{id})
    // ===================================
    @SuppressWarnings("null")
    @Transactional
    public void eliminarProyecto(Long id) {
        if (!proyectoRepository.existsById(id)) {
            throw new EntityNotFoundException("Proyecto no encontrado con ID: " + id);
        }
        proyectoRepository.deleteById(id);
    }

    // --- Mapeo: Entidad -> DTO ---
    private ProyectoDTO convertirA_DTO(Proyecto proyecto) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setIdProyecto(proyecto.getIdProyecto());
        dto.setNombre(proyecto.getNombre());
        dto.setFechaCreacion(proyecto.getFechaCreacion());
        return dto;
    }

    // --- Mapeo: DTO -> Entidad ---
    private Proyecto convertirA_Entidad(ProyectoDTO dto) {
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(dto.getNombre());
        // NO seteamos idProyecto (autogenerado) ni fechaCreacion (automático)
        return proyecto;
    }
}