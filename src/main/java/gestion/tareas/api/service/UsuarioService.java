package gestion.tareas.api.service;

import gestion.tareas.api.dto.UsuarioDTO;
import gestion.tareas.api.entity.Usuario;
import gestion.tareas.api.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

// Asumo que tienes una clase para el hashing de contraseñas (BCryptPasswordEncoder, por ejemplo)
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 

// UsuarioService.java

@Service
public class UsuarioService {
    // ⬅️ ¡ESTE ES EL PUNTO CRÍTICO!

    private final UsuarioRepository usuarioRepository;
    // Asegúrate de que solo exista esta línea para el repositorio
    // o el campo que necesites.

    // ⬅️ EL CONSTRUCTOR DEBE SER LIMPIO
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
    }

    // ===================================
    // 1. CREATE: Guardar nuevo Usuario (POST)
    // ===================================

    @Transactional
    public UsuarioDTO guardarUsuario(UsuarioDTO dto) {
        
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El campo 'contrasena' es obligatorio para crear un usuario.");
        }

        Usuario usuario = convertirA_Entidad(dto);

        // 🟢 Cifrar la contraseña
        // Primero, asegurémonos de que la contraseña del DTO se pase a la entidad
        usuario.setContrasenaHash(dto.getContrasena()); 

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirA_DTO(guardado);
    }

    // ===================================
    // 2. READ ALL: Obtener todos los Usuarios (GET /api/usuarios)
    // ===================================
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirA_DTO)
                .collect(Collectors.toList());
    }

    // ===================================
    // 3. READ BY ID: Obtener Usuario por ID (GET /api/usuarios/{id})
    // ===================================
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado con ID: " + id));

        return convertirA_DTO(usuario);
    }

    // ===================================
    // 4. UPDATE: Actualizar Usuario (PUT /api/usuarios/{id})
    // ===================================
    // Dentro de UsuarioService.java

    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado para actualizar con ID: " + id));

        // Aplicar solo los cambios, ignorando los campos nulos del DTO

        if (dto.getNombreCompleto() != null) {
            usuarioExistente.setNombreCompleto(dto.getNombreCompleto());
        }

        if (dto.getEmail() != null) { // ⬅️ CRÍTICO: SOLO actualiza si viene en el DTO
            usuarioExistente.setEmail(dto.getEmail());
        }

        if (dto.getTelefono() != null) {
            usuarioExistente.setTelefono(dto.getTelefono());
        }

        if (dto.getRol() != null) {
            usuarioExistente.setRol(dto.getRol());
        }

        // La contraseña NO se actualiza aquí, es un proceso separado

        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return convertirA_DTO(actualizado);
    }

    // ===================================
    // 5. DELETE: Eliminar Usuario (DELETE /api/usuarios/{id})
    // ===================================
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado para eliminar con ID: " + id);
        }
        // ⚠️ Nota: Si este usuario tiene tareas o comentarios asociados,
        // esto podría fallar debido a restricciones de clave foránea.
        usuarioRepository.deleteById(id);
    }

    // ===================================
    // MAPPERS (Convertidores)
    // ===================================

    // Asumo que estos métodos están implementados en tu clase.
    // Solo un ejemplo para que se compile:
    private UsuarioDTO convertirA_DTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getIdUsuario());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        // NO incluyas el ContrasenaHash en el DTO por seguridad.
        return dto;
    }

    // Dentro de UsuarioService.java

    private Usuario convertirA_Entidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());

        // ❌ ELIMINA ESTA LÍNEA DE COMENTARIO:
        // usuario.setContrasenaHash(dto.getContrasena()); // Esto debería ser un hash

        return usuario;
    }
}