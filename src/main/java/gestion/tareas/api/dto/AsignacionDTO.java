package gestion.tareas.api.dto;

import java.time.LocalDateTime;


// No olvides el constructor vacío y los getters/setters si no usas Lombok

public class AsignacionDTO {

    private Long idAsignacion;
    private Long idTarea;  // ⬅️ Clave foránea a Tarea
    private Long idUsuario; // ⬅️ Clave foránea a Usuario
    private LocalDateTime fechaAsignacion; // Para la respuesta GET

    public AsignacionDTO() {

    }

    public Long getIdAsignacion() {
        return idAsignacion;
    }
    public void setIdAsignacion(Long idAsignacion) {
        this.idAsignacion = idAsignacion;
    }
    public Long getIdTarea() {
        return idTarea;
    }
    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
    }
    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    // ... Constructor vacío, Getters y Setters ...
}