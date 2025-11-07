package gestion.tareas.api.controller;

import gestion.tareas.api.dto.ComentarioDTO;
import gestion.tareas.api.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Autowired
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    // Endpoint GET: /api/comentarios
    @GetMapping
    public ResponseEntity<List<ComentarioDTO>> obtenerTodos() {
        List<ComentarioDTO> comentarios = comentarioService.obtenerTodosLosComentarios();
        return ResponseEntity.ok(comentarios);
    }

    // Endpoint POST: /api/comentarios
    @PostMapping
    public ResponseEntity<ComentarioDTO> crearComentario(@RequestBody ComentarioDTO dto) {
        // Se recomienda envolver esto en try-catch para manejar RuntimeException
        ComentarioDTO comentarioCreado = comentarioService.guardarComentario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioCreado);
    }

    // ===================================
    // GET ONE: /api/comentarios/{id} 
    // ===================================
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDTO> obtenerPorId(@PathVariable("id") Long id) { // Usamos @PathVariable("id") por si el nombre de la variable no coincide con el path
        ComentarioDTO comentario = comentarioService.obtenerComentarioPorId(id);
        return ResponseEntity.ok(comentario);
    }

    // ===================================
    // PUT: /api/comentarios/{id} 
    // ===================================
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioDTO> actualizarComentario(@PathVariable("id") Long id, @RequestBody ComentarioDTO dto) {
        ComentarioDTO comentarioActualizado = comentarioService.actualizarComentario(id, dto);
        return ResponseEntity.ok(comentarioActualizado);
    }

    // ===================================
    // DELETE: /api/comentarios/{id} 
    // ===================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable("id") Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}