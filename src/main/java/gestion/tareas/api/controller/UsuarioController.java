package gestion.tareas.api.controller;

import gestion.tareas.api.dto.UsuarioDTO;
import gestion.tareas.api.service.UsuarioService;

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
}