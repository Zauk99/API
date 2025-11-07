package gestion.tareas.api.dto;

import gestion.tareas.api.entity.EstadoTarea;
import gestion.tareas.api.entity.PrioridadTarea;
import java.time.LocalDate;

// Recuerda añadir el constructor vacío y todos los getters/setters si no usas Lombok

public class TareaDTO {

    private Long idTarea; // Para la salida GET y la respuesta del POST

    private Long idProyecto; // ⬅️ SOLO el ID del proyecto relacionado
    
    private String nombre;
    private String descripcion;
    
    private EstadoTarea estado;
    private PrioridadTarea prioridad;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    
    // --- Constructor Vacío OBLIGATORIO ---
    public TareaDTO() {}

    public Long getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(Long idTarea) {
        this.idTarea = idTarea;
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

    public EstadoTarea getEstado() {
        return estado;
    }

    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }

    public PrioridadTarea getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(PrioridadTarea prioridad) {
        this.prioridad = prioridad;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    // --- Getters y Setters para TODOS los campos ---
    // (Asegúrate de escribirlos todos, especialmente idProyecto)
    // ...
    
}