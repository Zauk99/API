package gestion.tareas.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List; // Importar List para las colecciones
import java.util.ArrayList; // Importar ArrayList para inicializar

@Entity
@Table(name = "Tareas")
public class Tarea {

    // Campos de la tabla
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long idTarea;

    // Mapeo de la clave foránea (id_proyecto)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", nullable = false, 
    foreignKey = @ForeignKey(name = "fk_tarea_proyecto")) // ON DELETE CASCADE se configura por defecto o con anotaciones específicas de Hibernate
    private Proyecto proyecto;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTarea estado = EstadoTarea.POR_REALIZAR; // Valor por defecto
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadTarea prioridad = PrioridadTarea.MEDIA; // Valor por defecto
    
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_final")
    private LocalDate fechaFinal;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asignacion> asignaciones = new ArrayList<>(); // Inicializar para evitar NullPointerException

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>(); // Inicializar para evitar NullPointerException
    
    
    // Constructores, Getters y Setters
    
    public Tarea() {
    }

    public Long getIdTarea() {
        return idTarea;
    }
    
    public LocalDate getFechaFinal() {
        return fechaFinal;
    }
    
    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    public PrioridadTarea getPrioridad() {
        return prioridad;
    }
    
    public void setPrioridad(PrioridadTarea prioridad) {
        this.prioridad = prioridad;
    }
    public EstadoTarea getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoTarea estado) {
        this.estado = estado;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Proyecto getProyecto() {
        return proyecto;
    }
    
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    
    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public List<Comentario> getComentarios(){
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios){
        this.comentarios = comentarios;
    }
}