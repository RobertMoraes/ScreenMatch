package br.com.alura.screenmatch.enums;

public enum Categoria {
    ACAO("action", "Ação"),
    DRAMA("drama", "Drama"),
    COMEDIA("comedy", "Comédia"),
    ROMANCE("romance", "Romance"),
    TERROR("horror", "Terror"),
    INFANTIL("fantasy", "Infantil"),
    ANIMACAO("animation", "Animação"),
    CRIME("crime", "Crime");

    private final String categoriaOmdb;

    private final String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
