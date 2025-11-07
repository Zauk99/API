package gestion.tareas.api.controller;

import gestion.tareas.api.dto.AsignacionDTO;
import gestion.tareas.api.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired; // Necesario para la inyección
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionController {

    private final AsignacionService asignacionService;

    // ⬅️ CONSTRUCTOR (Inyección de Dependencias)
    // Spring inyecta la instancia del Service a través de este constructor.
    @Autowired 
    public AsignacionController(AsignacionService asignacionService) {
        // ⬅️ Aquí se inicializa el campo final, resolviendo el error.
        this.asignacionService = asignacionService;
    }

    // Endpoint POST para crear una nueva asignación
    // POST /api/asignaciones
    @PostMapping
    public ResponseEntity<AsignacionDTO> crearAsignacion(@RequestBody AsignacionDTO dto) {
        try {
            AsignacionDTO asignacionCreada = asignacionService.guardarAsignacion(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignacionCreada);
        } catch (RuntimeException e) {
            // Manejo básico de errores como "Tarea no encontrada" o "Usuario no encontrado"
            // Devolvemos 404 si es un error de recurso no encontrado, o 400 si es un error de datos.
            if (e.getMessage().contains("no encontrada")) {
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
             // Si el error es desconocido o de otro tipo, devolvemos 400 o el 500 original
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }
    }
    
    // Endpoint GET para obtener todas las asignaciones
    // GET /api/asignaciones
    @GetMapping
    public ResponseEntity<List<AsignacionDTO>> obtenerTodas() {
        // Asumiendo que el método ya está implementado en el Service
        List<AsignacionDTO> asignaciones = asignacionService.obtenerTodasLasAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }

    // ===================================
    // GET ONE: /api/asignaciones/{id} 
    // ===================================
    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDTO> obtenerPorId(@PathVariable Long id) {
        AsignacionDTO asignacion = asignacionService.obtenerAsignacionPorId(id);
        return ResponseEntity.ok(asignacion);
    }

    // ===================================
    // PUT: /api/asignaciones/{id} 
    // ===================================
    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDTO> actualizarAsignacion(@PathVariable Long id, @RequestBody AsignacionDTO dto) {
        AsignacionDTO asignacionActualizada = asignacionService.actualizarAsignacion(id, dto);
        return ResponseEntity.ok(asignacionActualizada);
    }

    // ===================================
    // DELETE: /api/asignaciones/{id} 
    // ===================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long id) {
        asignacionService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}