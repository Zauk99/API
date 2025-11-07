package gestion.tareas.api.dto;

import java.time.LocalDateTime;

// No olvides el constructor vacío y todos los getters/setters

public class ComentarioDTO {

    private Long idComentario;
    private Long idTarea;
    private Long idUsuario;
    private String contenido; // ⬅️ Campo de datos principal
    private LocalDateTime fechaComentario;

    public ComentarioDTO() { 
        
    }

    public Long getIdComentario() {
        return idComentario;
    }
    public void setIdComentario(Long idComentario) {
        this.idComentario = idComentario;
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
    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    public LocalDateTime getFechaComentario() {
        return fechaComentario;
    }
    public void setFechaComentario(LocalDateTime fechaComentario) {
        this.fechaComentario = fechaComentario;
    } 
    
    // ... Constructor vacío, Getters y Setters ...
}