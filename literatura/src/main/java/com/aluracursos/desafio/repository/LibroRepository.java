package com.aluracursos.desafio.repository;

import com.aluracursos.desafio.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro,Long> {
}
