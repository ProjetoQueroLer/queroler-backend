package com.usuario.quero_ler.enums;

public enum LivroIdioma {

    PORTUGUES("pt", "Português"),
    INGLES("en", "Inglês"),
    ESPANHOL("es", "Espanhol"),
    FRANCES("fr", "Francês"),
    ALEMAO("de", "Alemão"),
    ITALIANO("it", "Italiano"),
    JAPONES("ja", "Japonês"),
    CHINES("zh", "Chinês"),
    COREANO("ko", "Coreano"),
    ARABE("ar", "Árabe"),
    RUSSO("ru", "Russo"),
    HINDI("hi", "Hindi"),
    HOLANDES("nl", "Holandês"),
    SUECO("sv", "Sueco"),
    DINAMARQUES("da", "Dinamarquês"),
    FINLANDES("fi", "Finlandês"),
    NORUEGUES("no", "Norueguês"),
    POLONES("pl", "Polonês"),
    TURCO("tr", "Turco"),
    GREGO("el", "Grego"),
    HEBRAICO("he", "Hebraico"),
    TAILANDES("th", "Tailandês"),
    VIETNAMITA("vi", "Vietnamita"),
    INDONESIO("id", "Indonésio"),
    UCRANIANO("uk", "Ucraniano"),
    TCHECO("cs", "Tcheco"),
    HUNGARO("hu", "Húngaro");

    private final String codigo;
    private final String descricao;

    LivroIdioma(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}