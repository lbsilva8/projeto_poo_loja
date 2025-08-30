import controller.SistemaController;

/**
 * Ponto de entrada (Entry Point) da aplicação de Ponto de Venda.
 * A única responsabilidade desta classe é instanciar e iniciar o controlador
 * principal do sistema, que gerenciará todo o fluxo da aplicação.
 */
public class Main {
    public static void main(String[] args) {
        SistemaController controller = new SistemaController();
        controller.iniciar();
    }
}