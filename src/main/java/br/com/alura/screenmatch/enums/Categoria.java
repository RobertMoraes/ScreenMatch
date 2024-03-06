package br.com.alura.screenmatch.enums;

public enum Categoria {
    ACAO("action", "ação"),
    ANIMACAO("animation", "animação"),
    COMEDIA("comedy", "comédia"),
    CRIME("crime", "crime"),
    DRAMA("drama", "drama"),
    INFANTIL("fantasy", "infantil"),
    ROMANCE("romance", "romance"),
    TERROR("horror", "terror");

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
            if (categoria.categoriaPortugues.contentEquals(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

}
