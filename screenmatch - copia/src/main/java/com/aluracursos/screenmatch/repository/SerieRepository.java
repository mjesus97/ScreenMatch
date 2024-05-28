package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop3ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasGreaterThanEqualAndEvaluacionGreaterThanEqual(int temporadas, double evaluacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s =:serie ORDER BY e.evaluacion DESC LIMIT 3")
    List<Episodio> top5Episodios(Serie serie);

    //@Query("SELECT s FROM Serie s "+"JOIN s.episodios e "+"GROUP BY s "+"ORDER BY MAX(e.fechaDeLanazamiento) DESC LIMIT 5")
    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 3")
    List<Serie> lanzamientosMasRecientes();
}



