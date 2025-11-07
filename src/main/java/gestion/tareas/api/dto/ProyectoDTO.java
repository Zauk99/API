package gestion.tareas.api.dto;

import java.time.LocalDateTime;

// Recuerda añadir el constructor vacío y todos los getters/setters

public class ProyectoDTO {

    private Long idProyecto;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion; // Devolvemos el timestamp
    
    // --- Constructor Vacío OBLIGATORIO ---
    public ProyectoDTO() {}
    
    // --- Getters y Setters para TODOS los campos ---
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}