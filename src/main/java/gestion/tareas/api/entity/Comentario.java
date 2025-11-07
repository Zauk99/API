package gestion.tareas.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp; // ⬅️ Nuevo Import

@Entity
@Table(name = "Comentarios") // ⬅️ Convención: Mayúscula inicial
public class Comentario {

    // --- Campos de la tabla ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ⬅️ CRÍTICO: Alineado con el nombre real de la PK en la BD.
    @Column(name = "id")
    private Long idComentario;

    // Relación Many-to-One: Muchos comentarios pertenecen a una Tarea
    @ManyToOne(fetch = FetchType.LAZY)
    // ⬅️ CRÍTICO: Forzamos la escritura en el nombre que la restricción exige:
    // "id_tarea"
    @JoinColumn(name = "id_tarea", nullable = false, foreignKey = @ForeignKey(name = "fk_comentario_tarea"))
    private Tarea tarea;

    // Relación Many-to-One: El comentario es escrito por un Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    // ⬅️ Forzamos el nombre que la BD podría estar usando: "usuario_id"
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comentario_usuario"))
    private Usuario usuario;

    // Contenido
    @Column(nullable = false, length = 500)
    private String contenido;

    // Fecha (usando el nombre sin guion bajo de la BD)
    @CreationTimestamp
    @Column(name = "fechacreacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // --- Constructores ---
    // ⬅️ Constructor vacío OBLIGATORIO para JPA
    public Comentario() {
    }

    // --- Getters y Setters ---

    // ⬅️ Getter/Setter usando el nuevo nombre idComentario
    public Long getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    // ⬅️ Cambiado de getAutor() a getUsuario()
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    // No necesitamos setter para fechaCreacion ya que @CreationTimestamp la maneja.
    // Lo eliminamos para evitar errores de lógica.

    // Si quieres un método de conveniencia para tu DTO:
    // public LocalDateTime getFechaComentario() { return fechaCreacion; }
}