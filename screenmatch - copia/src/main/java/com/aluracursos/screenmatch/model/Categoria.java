package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama","Drama"),
    CRIMEN("Crime","Crimen"),
    AVENTURA("Adventure", "Aventura");

    private String categoriaOmbd;
    private String categoriaEspanol;

    Categoria (String categoriaOmbd, String categoriaEspanol){
        this.categoriaOmbd = categoriaOmbd;
        this.categoriaEspanol = categoriaEspanol;
    }
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmbd.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
