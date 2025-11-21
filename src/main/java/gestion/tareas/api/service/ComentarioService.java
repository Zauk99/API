package gestion.tareas.api.service;

import gestion.tareas.api.dto.ComentarioDTO;
import gestion.tareas.api.entity.Comentario;
import gestion.tareas.api.repository.ComentarioRepository;
import gestion.tareas.api.repository.TareaRepository;
import gestion.tareas.api.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;

    // Inyección de dependencias
    public ComentarioService(ComentarioRepository cRepo, TareaRepository tRepo, UsuarioRepository uRepo) {
        this.comentarioRepository = cRepo;
        this.tareaRepository = tRepo;
        this.usuarioRepository = uRepo;
    }

    // --- Lógica GET (Obtener todos) ---
    public List<ComentarioDTO> obtenerTodosLosComentarios() {
        return comentarioRepository.findAll().stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    // --- Lógica POST (Guardar) ---
    public ComentarioDTO guardarComentario(ComentarioDTO dto) {
        Comentario comentario = convertirA_Entidad(dto);
        @SuppressWarnings("null")
        Comentario guardado = comentarioRepository.save(comentario);
        return convertirA_DTO(guardado);
    }

    // ===================================
    // 3. READ ONE: Obtener por ID (GET /api/comentarios/{id})
    // ===================================
    public ComentarioDTO obtenerComentarioPorId(Long id) {
        @SuppressWarnings("null")
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado con ID: " + id));
        return convertirA_DTO(comentario);
    }

    // ===================================
    // 4. UPDATE: Actualizar por ID (PUT /api/comentarios/{id})
    // Solo se permite actualizar el contenido
    // ===================================
    @Transactional
    public ComentarioDTO actualizarComentario(Long id, ComentarioDTO dto) {
        // 1. Buscar la entidad existente
        @SuppressWarnings("null")
        Comentario comentarioExistente = comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado con ID: " + id));

        // 2. Aplicar los cambios: Solo se actualiza el contenido
        if (dto.getContenido() != null && !dto.getContenido().trim().isEmpty()) {
            comentarioExistente.setContenido(dto.getContenido());
        }

        // 3. Guardar y devolver DTO
        @SuppressWarnings("null")
        Comentario comentarioActualizado = comentarioRepository.save(comentarioExistente);
        return convertirA_DTO(comentarioActualizado);
    }

    // ===================================
    // 5. DELETE: Eliminar por ID (DELETE /api/comentarios/{id})
    // ===================================
    @SuppressWarnings("null")
    @Transactional
    public void eliminarComentario(Long id) {
        if (!comentarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Comentario no encontrado con ID: " + id);
        }
        comentarioRepository.deleteById(id);
    }

    // --- Mapeo: Entidad -> DTO ---
    private ComentarioDTO convertirA_DTO(Comentario comentario) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setIdComentario(comentario.getIdComentario());
        dto.setContenido(comentario.getContenido());
        dto.setFechaComentario(comentario.getFechaCreacion());

        // Asignar IDs de las relaciones
        if (comentario.getTarea() != null) {
            dto.setIdTarea(comentario.getTarea().getIdTarea());
        }
        if (comentario.getUsuario() != null) {
            dto.setIdUsuario(comentario.getUsuario().getIdUsuario());
        }
        return dto;
    }

    // Dentro de ComentarioService.java

    @SuppressWarnings("null")
    private Comentario convertirA_Entidad(ComentarioDTO dto) {
        Comentario comentario = new Comentario();

        // 1. Asignar Contenido
        comentario.setContenido(dto.getContenido());

        // 2. Asignar Tarea (Busca la Entidad con la ID del DTO)
        comentario.setTarea(tareaRepository.findById(dto.getIdTarea())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + dto.getIdTarea())));

        // 3. Asignar Usuario (Busca la Entidad con la ID del DTO)
        // ⚠️ CRÍTICO: Asegúrate de que este setter coincide con el nombre de la
        // propiedad en Comentario.java
        comentario.setUsuario(usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getIdUsuario())));

        return comentario;
    }
}