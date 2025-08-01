package com.catalogo.libros.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros;

    // Constructor vacío requerido por JPA
    public Autor() {}

    public Autor(String nombre, Integer anioNacimiento, Integer anioFallecimiento) {
        this.nombre = nombre;
        this.anioNacimiento = anioNacimiento;
        this.anioFallecimiento = anioFallecimiento;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "\n----- AUTOR -----" +
                "\nNombre: " + nombre +
                "\nFecha de Nacimiento: " + (anioNacimiento != null ? anioNacimiento : "N/A") +
                "\nFecha de Fallecimiento: " + (anioFallecimiento != null ? anioFallecimiento : "N/A") +
                "\nLibros: " + (libros != null ? libros.stream().map(Libro::getTitulo).reduce((a, b) -> a + ", " + b).orElse("N/A") : "N/A") +
                "\n-----------------\n";
    }
}
