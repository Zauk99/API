package gestion.tareas.api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp; // ⬅️ IMPORTANTE: Necesario para la fecha automática

@Entity
@Table(name = "Asignaciones", uniqueConstraints = {
    // Restricción para asegurar que un usuario solo pueda ser asignado una vez por tarea
    @UniqueConstraint(columnNames = {"id_tarea", "id_usuario"}) 
})
public class Asignacion {

    // --- Campos de la tabla ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Long idAsignacion;
    
    // Clave Foránea a Tarea
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarea", nullable = false, 
    foreignKey = @ForeignKey(name = "fk_asignacion_tarea"))
    private Tarea tarea;
    
    // Clave Foránea a Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, 
    foreignKey = @ForeignKey(name = "fk_asignacion_usuario"))
    private Usuario usuario;
    
    // ⬅️ CAMPO DE FECHA AÑADIDO Y CONFIGURADO COMO AUTOMÁTICO
    @CreationTimestamp // Esto le dice a Hibernate que establezca la fecha actual al guardar
    @Column(name = "fecha_asignacion", nullable = false, updatable = false)
    private LocalDateTime fechaAsignacion;
    
    
    // --- Constructor Vacío (OBLIGATORIO para JPA) ---
    public Asignacion() {
        // Super() es implícito
    }

    // --- Getters y Setters ---
    
    public Long getIdAsignacion() {
        return idAsignacion;
    }
    
    // Nota: Normalmente no necesitas un setter para la ID autogenerada.
    public void setIdAsignacion(Long idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Tarea getTarea() {
        return tarea;
    }
    
    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // ⬅️ GETTER CORREGIDO: Devuelve el campo, no arroja excepción
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
    
    // Nota: El setter de fecha no es necesario porque @CreationTimestamp la gestiona.
}