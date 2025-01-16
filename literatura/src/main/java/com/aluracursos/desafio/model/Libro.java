package com.aluracursos.desafio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String titulo;

    // Mantén solo esta propiedad 'idioma' de tipo String
    private String idioma;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private Double numeroDeDescargas;

    public Libro() {}

    // Modificar constructor para asignar un 'String' a idioma
    public Libro(DatosLibros datosLibros, List<Autor> autores, List<String> idiomas) {
        this.titulo = datosLibros.titulo();
        this.autor = autores.get(0);
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();

        // Asignar el primer idioma de la lista (String)
        this.idioma = idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : null;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    // Método setter para 'idioma' como String
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return "Titulo del libro= " + titulo +
                "\nautor= " + (autor != null ? autor.getNombre() : "N/A") + '\'' +
                ",\nidioma= " + (idioma != null ? idioma : "N/A") + '\'' +
                "\nDescargas= " + numeroDeDescargas;
    }
}
