package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String evaluacion,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Plot") String sinopsis,
                         @JsonAlias("Actors") String actores,
                         @JsonAlias("Poster") String poster) {
}
