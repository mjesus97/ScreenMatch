package com.aluracursos.screenmatch.dto;

import com.aluracursos.screenmatch.model.Categoria;

public record SerieDTO(String titulo, Long id,
        Integer totalTemporadas,
        Double evaluacion,
        Categoria genero,
        String sinopsis,
        String actores,
        String poster) {
}
