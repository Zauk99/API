package gestion.tareas.api.controller;

import gestion.tareas.api.dto.UsuarioDTO;
import gestion.tareas.api.service.UsuarioService;
import gestion.tareas.api.dto.ContrasenaUpdateDTO;
import gestion.tareas.api.dto.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ===================================
    // 1. CREATE (POST) - Ya debería existir
    // ===================================
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO dto) {
        UsuarioDTO usuarioCreado = usuarioService.guardarUsuario(dto);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

    // ===================================
    // 2. READ ALL (GET) - Ya debería existir
    // ===================================
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // ===================================
    // Nuevo método para buscar por email
    // ===================================
    @GetMapping("/buscar-email")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorEmail(@RequestParam String email) {
        // ❗ Implementación crítica: Tu UsuarioService debe tener este método.
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build(); // 404 si no existe
        }
    }

    // ===================================
    // 3. READ BY ID (GET /id) - Nuevo
    // ===================================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    // ===================================
    // 4. UPDATE (PUT /id) - Nuevo
    // ===================================
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    // ===================================
    // 5. DELETE (DELETE /id) - Nuevo
    // ===================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // ===================================
    // 6. LOGIN (POST /login) - CRÍTICO
    // ===================================
    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody LoginRequestDTO credenciales) {

        // 1. Llama al servicio para validar las credenciales
        Long userId = usuarioService.validarCredenciales(
                credenciales.getEmail(),
                credenciales.getContrasena());

        if (userId != null) {
            // 🟢 Éxito: Devuelve 200 OK y el ID del usuario
            return ResponseEntity.ok(userId);
        } else {
            // 🔴 Falla: Devuelve 401 Unauthorized para indicar credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // En UsuarioController.java (API 8080)
    // ===================================
    // 6. UPDATE PASSWORD (PATCH /id/contrasena)
    // ===================================
    @PutMapping("/{id}/contrasena") // O @PutMapping
    public ResponseEntity<Void> actualizarContrasena(@PathVariable Long id,
            @RequestBody ContrasenaUpdateDTO dto) {
        usuarioService.actualizarContrasena(id, dto.getContrasena());
        return ResponseEntity.ok().build(); // 200 OK
    }
}