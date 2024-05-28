package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.EpisodioDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository repositorio;
    public List<SerieDTO> obtenerTodasLasSeries(){
        return repositorio.findAll().stream()
                .map(s-> new SerieDTO(s.getTitulo(),s.getId()
                        ,s.getTotalTemporadas()
                        ,s.getEvaluacion()
                        ,s.getGenero()
                        ,s.getSinopsis()
                        ,s.getActores()
                        ,s.getPoster()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obtenerTop5() {
        return convierteDatos(repositorio.findTop3ByOrderByEvaluacionDesc());
    }
    public List<SerieDTO> obtenerLanzamientosMasRecientes() {
        return convierteDatos(repositorio.lanzamientosMasRecientes());
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return serie.stream()
                .map(s-> new SerieDTO(s.getTitulo(),s.getId()
                ,s.getTotalTemporadas()
                ,s.getEvaluacion()
                ,s.getGenero()
                ,s.getSinopsis()
                ,s.getActores()
                ,s.getPoster()))
                .collect(Collectors.toList());
    }

    public SerieDTO obtenerPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getTitulo()
                    ,s.getId()
                    ,s.getTotalTemporadas()
                    ,s.getEvaluacion()
                    ,s.getGenero()
                    ,s.getSinopsis()
                    ,s.getActores()
                    ,s.getPoster());
        }
        return null;
    }

    public List<EpisodioDTO> obtenerTodasLasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada()
                            ,e.getTitulo()
                            ,e.getNumeroEpisodio()))
                    .collect(Collectors.toList());
        }
        return null;
    }
    public List<EpisodioDTO> obtenerTemporada(Long id,Integer temp){
        Optional<Serie> serie = repositorio.findById(id);
        if (serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .filter(e -> e.getTemporada() == temp)
                    .map(e -> new EpisodioDTO(e.getTemporada()
                            ,e.getTitulo()
                            ,e.getNumeroEpisodio()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<SerieDTO> obtenerSeriesporcategoria(String nombreGenero) {
        Categoria categoria = Categoria.fromEspanol(nombreGenero); //metodo que convierte de string a enum
        return convierteDatos(repositorio.findByGenero(categoria));
    }
}
