package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new  ConvierteDatos();
    private final String URL_BASE = "https://www.omdbapi.com/?";
    private final String API_KEY = "apikey=2a6c230a"; //No dejar visible
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Top 5 Series
                    6 - Buscar Series por categoria
                    7 - Buqueda por numero de Temporadas y evaluacion
                    9 - top 5 episodios
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    mostrarSeriesPortitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriePorCategoria();
                    break;
                case 7:
                    busquedaPorNumeroTemporadas();
                    break;
                case 9:
                    top5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }


    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + API_KEY + "&t="+nombreSerie.replace(" ", "+"));
        //System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        // DatosSerie datosSerie = getDatosSerie(); esto trae series de la api, ahora lo quiero de la base de datos.
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la cual quieres ver los episodios:");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s->s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();
        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + API_KEY +"&t="+ serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d->d.episodios().stream()
                            .map(e->new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }

    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);;

    }
    private void mostrarSeriesBuscadas() {
         series = repositorio.findAll();
        //Si no utilizamos el repositorio
//                new ArrayList<>();
//        series = datosSeries.stream()
//                .map(d-> new Serie(d))
//                .collect(Collectors.toList());
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);


    }

    private void mostrarSeriesPortitulo() {
        System.out.println("Escribe el nombre de la serie que quieres ver:");
        var nombreSerie = teclado.nextLine();

        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: "+serieBuscada.get());
        }else{
            System.out.println("Serie noo encontrada");
        }

    }
    private void buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop3ByOrderByEvaluacionDesc();
        topSeries.forEach(s-> System.out.println("Serie: "+s.getTitulo()+" - Evaluacion: "+s.getEvaluacion()));

    }
    private void buscarSeriePorCategoria(){
        System.out.println("Escriba la categoria a buscar:");
        var nombregenero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(nombregenero);

        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("las series de la categoria "+nombregenero+" son:");
        seriesPorCategoria.forEach(System.out::println);


    }

    private void busquedaPorNumeroTemporadas(){
        System.out.println("Escriba la cantidad de Temporadas:");
        int numeroTemporadas = teclado.nextInt();
        System.out.println("Escriba la evaluacion minima:");
        double evaluacionMinima = teclado.nextDouble();

        List<Serie> seriesPorTemporadas = repositorio.findByTotalTemporadasGreaterThanEqualAndEvaluacionGreaterThanEqual(numeroTemporadas, evaluacionMinima);
        seriesPorTemporadas.forEach(System.out::println);

    }

    private void top5Episodios(){
        mostrarSeriesPortitulo();
        if(serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e-> System.out.printf("Serie: %s - Temporada: %s - Episodio: %s - Evaluacion: %s\n",
                    e.getSerie().getTitulo(),e.getTemporada(),e.getNumeroEpisodio(),e.getEvaluacion()));
        }
    }
}
