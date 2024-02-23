package br.com.alura.screenmatch.enums;

public enum Categoria {
    ACAO("action"),
    DRAMA("drama"),
    COMEDIA("comedy"),
    ROMANCE("romance"),
    TERROR("horror"),
    INFANTIL("fantasy"),
    ANIMACAO("animation"),
    CRIME("crime");

    private final String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
