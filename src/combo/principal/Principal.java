package combo.principal;

import combo.gui.GuiPrincipal;
import combo.config.DatabaseConfig;
import combo.exception.DatabaseException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

/**
 * Classe principal da aplicação de gerenciamento de livros.
 * 
 * Esta classe é responsável por:
 * - Inicializar a aplicação
 * - Configurar o Look and Feel
 * - Verificar a conexão com o banco de dados
 * - Exibir a interface principal
 * 
 * @author Sistema Melhorado
 * @version 2.0
 */
public class Principal {

    /**
     * Método principal da aplicação.
     * 
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        // Executa a aplicação na thread de eventos do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                Principal app = new Principal();
                app.inicializar();
            } catch (Exception e) {
                System.err.println("Erro ao inicializar a aplicação: " + e.getMessage());
                e.printStackTrace();

                // Exibe mensagem de erro para o usuário
                JOptionPane.showMessageDialog(
                        null,
                        "Erro ao inicializar a aplicação:\n" + e.getMessage(),
                        "Erro de Inicialização",
                        JOptionPane.ERROR_MESSAGE);

                System.exit(1);
            }
        });
    }

    /**
     * Inicializa a aplicação.
     * 
     * @throws DatabaseException se houver erro na conexão com o banco
     */
    private void inicializar() throws DatabaseException {
        System.out.println("=== Iniciando Sistema de Gerenciamento de Livros ===");

        // Configura o Look and Feel
        configurarLookAndFeel();

        // Verifica a conexão com o banco de dados
        verificarConexaoBanco();

        // Exibe a interface principal
        exibirInterfacePrincipal();

        System.out.println("=== Sistema iniciado com sucesso! ===");
    }

    /**
     * Configura o Look and Feel da aplicação.
     */
    private void configurarLookAndFeel() {
        try {
            // Tenta usar o Look and Feel do sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Se falhar, usa o padrão
            System.out.println("Usando Look and Feel padrão: " + e.getMessage());
        }
    }

    /**
     * Verifica se a conexão com o banco de dados está funcionando.
     * 
     * @throws DatabaseException se não conseguir conectar
     */
    private void verificarConexaoBanco() throws DatabaseException {
        try {
            System.out.println("Verificando conexão com o banco de dados...");

            // Testa a conexão usando o DatabaseConfig
            DatabaseConfig config = new DatabaseConfig();
            System.out.printf("Configuração: ", config.toString());
            if (config.isConnectionValid()) {
                System.out.println("✅ Conexão com o banco de dados estabelecida!");
            } else {
                throw new DatabaseException("Conexão inválida");
            }

        } catch (Exception e) {
            System.err.println("❌ Erro na conexão com o banco de dados: " + e.getMessage());
            throw new DatabaseException("Falha na conexão com o banco de dados", e);
        }
    }

    /**
     * Exibe a interface principal da aplicação.
     */
    private void exibirInterfacePrincipal() {
        try {
            // Cria a interface principal com título padrão
            GuiPrincipal janelaPrincipal = new GuiPrincipal("postgresql");
            janelaPrincipal.setVisible(true);

            System.out.println("Interface principal exibida");

        } catch (Exception e) {
            System.err.println("Erro ao exibir interface principal: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao exibir a interface principal:\n" + e.getMessage(),
                    "Erro de Interface",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
