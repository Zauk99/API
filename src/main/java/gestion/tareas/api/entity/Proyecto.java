package gestion.tareas.api.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp; // Si usas Spring/Hibernate

@Entity
@Table(name = "Proyectos")
public class Proyecto {

    // Campos de la tabla

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    @Column(nullable = false)
    private String nombre;

    // Necesitas una descripción
    @Column(name = "descripcion")
    private String descripcion;

    @CreationTimestamp // Para manejar el DEFAULT CURRENT_TIMESTAMP
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private java.time.LocalDateTime fechaCreacion; // o java.util.Date

    // Relación Inversa: Un Proyecto tiene Múltiples Tareas
    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Tarea> tareas = new java.util.ArrayList<>();

    // ⚠️ También debes añadir los métodos Getter y Setter para 'tareas'
    // manualmente:

    public java.util.List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(java.util.List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public java.time.LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}