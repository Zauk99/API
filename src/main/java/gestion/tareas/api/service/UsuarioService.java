package gestion.tareas.api.service;

import gestion.tareas.api.dto.UsuarioDTO;
import gestion.tareas.api.entity.Usuario;
import gestion.tareas.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

// Asumo que tienes una clase para el hashing de contraseñas (BCryptPasswordEncoder, por ejemplo)
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 

// UsuarioService.java

@Service
public class UsuarioService {

    // 1. ✅ Ya no se necesita el PasswordEncoder como campo
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // 2. ✅ El constructor solo inyecta el repositorio
    // ❌ Antes: public UsuarioService(UsuarioRepository usuarioRepository,
    // PasswordEncoder passwordEncoder) {
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

        // 🟢 CORRECCIÓN DE SEGURIDAD: Cifrar la contraseña antes de guardarla
        String contrasenaCifrada = passwordEncoder.encode(dto.getContrasena());
        usuario.setContrasenaHash(contrasenaCifrada);

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirA_DTO(guardado);
    }

    public Long validarCredenciales(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) {
            return null;
        }

        if (this.passwordEncoder.matches(contrasena, usuario.getContrasenaHash())) {
            return usuario.getIdUsuario();
        } else {
            return null;
        }
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
        @SuppressWarnings("null")
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
        @SuppressWarnings("null")
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

        @SuppressWarnings("null")
        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return convertirA_DTO(actualizado);
    }

    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        // ❗ Lógica de negocio (ej: validaciones) antes de consultar la BBDD.

        // ❗ LLamada al Repositorio
        Usuario entidad = usuarioRepository.findByEmail(email); // <-- Necesita existir en el Repository

        return convertirA_DTO(entidad); // Debes convertir la Entidad a DTO
    }

    // ===================================
    // 5. DELETE: Eliminar Usuario (DELETE /api/usuarios/{id})
    // ===================================
    @SuppressWarnings("null")
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

    // En UsuarioService.java (API 8080)

    @Transactional
    public void actualizarContrasena(Long id, String nuevaContrasena) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado para actualizar con ID: " + id));

        // 1. Cifrar la nueva contraseña
        String contrasenaCifrada = passwordEncoder.encode(nuevaContrasena);

        // 2. Guardar el hash en la entidad
        usuarioExistente.setContrasenaHash(contrasenaCifrada);

        usuarioRepository.save(usuarioExistente);
    }
}