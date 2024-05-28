package com.aluracursos.screenmatch.principal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {

    public void muestraejemplo(){
        List<String> nombres = Arrays.asList("yo","tu","el","nosotros","ellos");

        nombres.stream()
                .sorted()
                .limit(4)
                .filter(n -> n.startsWith("e"))
                .map(n->n.toUpperCase())
                .forEach(System.out::println);
    }
}
