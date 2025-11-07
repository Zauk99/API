package gestion.tareas.api.controller;

import gestion.tareas.api.dto.TareaDTO;
import gestion.tareas.api.service.TareaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Indica que esta clase es un controlador REST y mapea a /api/tareas
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    // Inyección de dependencias por constructor
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // 1. CREATE: Crear Tarea (POST)
    // URL: POST http://localhost:8080/api/tareas
    @PostMapping
    public ResponseEntity<TareaDTO> crearTarea(@RequestBody TareaDTO dto) {
        TareaDTO tareaCreada = tareaService.guardarTarea(dto);
        // Devuelve 201 Created
        return new ResponseEntity<>(tareaCreada, HttpStatus.CREATED);
    }

    // 2. READ ALL: Obtener todas las Tareas (GET)
    // URL: GET http://localhost:8080/api/tareas
    @GetMapping
    public ResponseEntity<List<TareaDTO>> obtenerTodasLasTareas() {
        List<TareaDTO> tareas = tareaService.obtenerTodasLasTareas();
        // Devuelve 200 OK
        return ResponseEntity.ok(tareas);
    }
    
    // 3. READ BY ID: Obtener Tarea por ID (GET /id)
    // URL: GET http://localhost:8080/api/tareas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> obtenerTareaPorId(@PathVariable Long id) {
        // La excepción 404 es manejada por el Service
        TareaDTO tarea = tareaService.obtenerTareaPorId(id);
        // Devuelve 200 OK
        return ResponseEntity.ok(tarea);
    }

    // 4. UPDATE: Actualizar Tarea (PUT /id)
    // URL: PUT http://localhost:8080/api/tareas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TareaDTO> actualizarTarea(@PathVariable Long id, @RequestBody TareaDTO dto) {
        TareaDTO tareaActualizada = tareaService.actualizarTarea(id, dto);
        // Devuelve 200 OK
        return ResponseEntity.ok(tareaActualizada);
    }

    // 5. DELETE: Eliminar Tarea (DELETE /id)
    // URL: DELETE http://localhost:8080/api/tareas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarea(@PathVariable Long id) {
        // La excepción 404 es manejada por el Service
        tareaService.eliminarTarea(id);
        // Devuelve 204 No Content (respuesta estándar para eliminación exitosa)
        return ResponseEntity.noContent().build();
    }
}