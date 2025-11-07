package gestion.tareas.api.entity;

import jakarta.persistence.*;

// Si RolUsuario es un ENUM, asegúrate de tener este archivo:
// package gestion.tareas.api.entity;
// public enum RolUsuario { USUARIO, ADMINISTRADOR, ... }

@Entity
@Table(name = "Usuarios")
public class Usuario {

    // --- Campos de la tabla ---
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name= "telefono")
    private String telefono;
    
    @Column(name = "contraseña_hash", nullable = false)
    private String contrasenaHash;

    // ⚠️ CORRECCIÓN: Asignamos un valor por defecto para evitar que sea NULL en la BD
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol = RolUsuario.TRABAJADOR; // Asume que 'USUARIO' es un valor válido

    // --- Relaciones Opcionales (Si existen) ---
    // Si tu usuario tiene tareas asignadas o proyectos, van aquí las colecciones
    /*
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Asignacion> asignaciones;
    */

    // --- Constructor Vacío (OBLIGATORIO para JPA/Hibernate) ---
    
    public Usuario(){
    }

    // --- Getters y Setters ---

    // Getter principal de la ID (usaremos idUsuario)
    public Long getIdUsuario() {
        return idUsuario;
    }

    // Setter de ID (normalmente no se usa, pero lo mantendremos por consistencia)
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }
    
    // Si usas el getter getId() que tenías antes, puedes mantenerlo, pero se recomienda uno solo:
    /*
    public Long getId() {
        return idUsuario;
    }
    */
}