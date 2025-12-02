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

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

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

        // Cifrar la contraseña antes de guardarla
        String contrasenaCifrada = passwordEncoder.encode(dto.getContrasena());
        usuario.setContrasenaHash(contrasenaCifrada);

        Usuario guardado = usuarioRepository.save(usuario);
        return convertirA_DTO(guardado);
    }

    // ===================================
    // Método de soporte para el login interno del API (No usado por el Cliente 8081)
    // ===================================
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
    // 🛑 4. READ: Obtener Usuario por Email (CRÍTICO PARA EL LOGIN DEL CLIENTE 8081)
    // Este método devuelve el DTO CON el hash de la contraseña.
    // ===================================
    public UsuarioDTO obtenerUsuarioPorEmail(String email) {
        // Llama al Repositorio para obtener la entidad completa
        Usuario entidad = usuarioRepository.findByEmail(email); 

        if (entidad == null) {
            return null; // Usuario no encontrado
        }
        
        // Creamos el DTO usando el mapeo estándar para todos los campos excepto el hash
        UsuarioDTO dtoParaLogin = convertirA_DTO(entidad);
        
        // 🛑 CRÍTICO: Añadir el hash de la contraseña al DTO que se enviará al Cliente (8081).
        // El Cliente (8081) lo necesita para hacer la validación BCrypt.
        dtoParaLogin.setContrasena(entidad.getContrasenaHash()); 
        
        return dtoParaLogin; // Devolvemos el DTO CON el hash
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
    // 5. UPDATE: Actualizar Usuario (PUT /api/usuarios/{id})
    // ===================================
    @Transactional
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        @SuppressWarnings("null")
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario no encontrado para actualizar con ID: " + id));

        if (dto.getNombreCompleto() != null) {
            usuarioExistente.setNombreCompleto(dto.getNombreCompleto());
        }

        if (dto.getEmail() != null) { 
            usuarioExistente.setEmail(dto.getEmail());
        }

        if (dto.getTelefono() != null) {
            usuarioExistente.setTelefono(dto.getTelefono());
        }

        if (dto.getRol() != null) {
            usuarioExistente.setRol(dto.getRol());
        }

        @SuppressWarnings("null")
        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        return convertirA_DTO(actualizado);
    }

    // ===================================
    // 6. DELETE: Eliminar Usuario (DELETE /api/usuarios/{id})
    // ===================================
    @SuppressWarnings("null")
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Usuario no encontrado para eliminar con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    // ===================================
    // 7. UPDATE PASSWORD (PATCH /id/contrasena)
    // ===================================
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

    // ===================================
    // MAPPERS (Convertidores)
    // ===================================

    // Convierte la Entidad a DTO. Por seguridad, no incluye el hash de la contraseña.
    private UsuarioDTO convertirA_DTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getIdUsuario()); // Asumo que el campo es getIdUsuario()
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setRol(usuario.getRol());
        // NO se incluye el hash aquí para operaciones generales.
        return dto;
    }

    // Convierte el DTO a Entidad (usado en POST/PUT)
    private Usuario convertirA_Entidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRol(dto.getRol());
        
        // Nota: La contraseña se cifra y se setea por separado en guardarUsuario().
        
        return usuario;
    }
}