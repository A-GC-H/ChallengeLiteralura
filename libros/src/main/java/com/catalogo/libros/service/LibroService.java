package com.catalogo.libros.service;

import com.catalogo.libros.dto.AutorDTO;
import com.catalogo.libros.dto.GutendexResponse;
import com.catalogo.libros.dto.LibroDTO;
import com.catalogo.libros.model.Autor;
import com.catalogo.libros.model.Libro;
import com.catalogo.libros.repository.AutorRepository;
import com.catalogo.libros.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;
    @Autowired
    private ConsumoAPI consumoAPI;
    @Autowired
    private ConvierteDatos convierteDatos;

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    public Libro buscarYLguardarLibro(String tituloLibro) {
        Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(tituloLibro);
        if (libroExistente.isPresent()) {
            System.out.println("El libro '" + tituloLibro + "' ya está registrado en la base de datos.");
            return null;
        }

        String json = consumoAPI.obtenerDatos(URL_BASE + tituloLibro.replace(" ", "%20"));
        GutendexResponse response = convierteDatos.obtenerDatos(json, GutendexResponse.class);

        if (response != null && !response.getResults().isEmpty()) {
            LibroDTO libroDTO = response.getResults().get(0); // Tomamos el primer resultado
            Libro libro = new Libro(libroDTO.getTitle(), libroDTO.getLanguages().isEmpty() ? "N/A" : libroDTO.getLanguages().get(0), libroDTO.getDownloadCount());

            List<Autor> autores = libroDTO.getAuthors().stream()
                    .map(this::obtenerOguardarAutor)
                    .collect(Collectors.toList());

            libro.setAutores(autores);
            libroRepository.save(libro);
            System.out.println("Libro guardado exitosamente: " + libro.getTitulo());
            return libro;
        } else {
            System.out.println("Libro '" + tituloLibro + "' no encontrado en la API de Gutendex.");
            return null;
        }
    }

    private Autor obtenerOguardarAutor(AutorDTO autorDTO) {
        Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(autorDTO.getName());
        if (autorExistente.isPresent()) {
            return autorExistente.get();
        } else {
            Autor nuevoAutor = new Autor(autorDTO.getName(), autorDTO.getBirthYear(), autorDTO.getDeathYear());
            return autorRepository.save(nuevoAutor);
        }
    }

    public List<Libro> listarLibrosRegistrados() {
        return libroRepository.findAll();
    }

    public List<Autor> listarAutoresRegistrados() {
        return autorRepository.findAll();
    }

    public List<Autor> listarAutoresVivosEnAnio(Integer anio) {
        return autorRepository.findAutoresVivosEnAnio(anio);
    }

    public List<Libro> listarLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }
}