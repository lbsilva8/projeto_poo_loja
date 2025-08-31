package com.seuprojeto; // Use o nome do seu pacote aqui

/**
 * Autoras:
 * Andreísy Neves Ferreira
 * Isabella Paranhos Meireles
 * Lorena da Silva Borges
 */

/**
 * Classe "lançadora" que serve como o ponto de entrada principal para o JAR executável.
 * O objetivo desta classe é contornar um problema de carregamento de módulos do JavaFX
 * ao executar a partir de um "fat JAR". Ela chama o metodo main da classe {@link App}
 * principal, iniciando o ciclo de vida do JavaFX da maneira correta.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}