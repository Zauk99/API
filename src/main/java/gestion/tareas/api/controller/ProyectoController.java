package gestion.tareas.api.controller;

import gestion.tareas.api.dto.ProyectoDTO;
import gestion.tareas.api.service.ProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos") 
public class ProyectoController {

    private final ProyectoService proyectoService;

    @Autowired
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // ===================================
    // 1. GET ALL: /api/proyectos (Obtener todos)
    // ===================================
    @GetMapping
    public ResponseEntity<List<ProyectoDTO>> obtenerTodos() {
        List<ProyectoDTO> proyectos = proyectoService.obtenerTodosLosProyectos();
        return ResponseEntity.ok(proyectos);
    }

    // ===================================
    // 2. GET ONE: /api/proyectos/{id} (Obtener por ID)
    // ===================================
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoDTO> obtenerPorId(@PathVariable Long id) {
        // Asume que ProyectoService lanza EntityNotFoundException si no lo encuentra
        ProyectoDTO proyecto = proyectoService.obtenerProyectoPorId(id);
        return ResponseEntity.ok(proyecto);
    }

    // ===================================
    // 3. POST: /api/proyectos (Crear)
    // ===================================
    @PostMapping
    public ResponseEntity<ProyectoDTO> crearProyecto(@RequestBody ProyectoDTO proyectoDTO) {
        ProyectoDTO proyectoCreado = proyectoService.guardarProyecto(proyectoDTO);
        // Código de estado 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoCreado);
    }
    
    // ===================================
    // 4. PUT: /api/proyectos/{id} (Actualizar)
    // ===================================
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoDTO> actualizarProyecto(@PathVariable Long id, @RequestBody ProyectoDTO dto) {
        // Asume que ProyectoService maneja la excepción si el ID no existe
        ProyectoDTO proyectoActualizado = proyectoService.actualizarProyecto(id, dto);
        return ResponseEntity.ok(proyectoActualizado); // Código de estado 200 OK
    }

    // ===================================
    // 5. DELETE: /api/proyectos/{id} (Eliminar)
    // ===================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        // Asume que ProyectoService maneja la excepción si el ID no existe
        proyectoService.eliminarProyecto(id);
        // Código de estado 204 NO CONTENT para eliminación exitosa sin cuerpo de respuesta
        return ResponseEntity.noContent().build(); 
    }
}