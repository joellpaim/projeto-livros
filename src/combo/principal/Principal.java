package combo.principal;

import combo.gui.GuiPrincipal;
import combo.config.DatabaseConfig;
import combo.exception.DatabaseException;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.util.Properties;
import java.io.InputStream;

public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Principal app = new Principal();
                app.inicializar();
            } catch (Exception e) {
                System.err.println("Erro ao inicializar a aplicação: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Erro ao inicializar a aplicação:\n" + e.getMessage(),
                        "Erro de Inicialização",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    private void inicializar() throws DatabaseException {
        System.out.println("=== Iniciando Sistema de Gerenciamento de Livros ===");

        configurarLookAndFeel();

        // Pegar o tipo do banco do arquivo .properties
        String dbType = carregarTipoBanco();
        verificarConexaoBanco(dbType);
        System.out.println("Tipo de banco configurado: " + dbType);

        // Passar para a GuiPrincipal
        exibirInterfacePrincipal(dbType);

        System.out.println("=== Sistema iniciado com sucesso! ===");
    }

    private String carregarTipoBanco() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            // Pega o db.type ou usa "mysql" como padrão se não existir
            return prop.getProperty("db.type", "postgresql");

        } catch (Exception e) {
            System.out.println("Usando banco padrão (mysql) - Não foi possível ler config.properties");
            return "postgresql";
        }
    }

    private void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Usando Look and Feel padrão: " + e.getMessage());
        }
    }

    private void verificarConexaoBanco(String dbType) throws DatabaseException {
        try {
            System.out.println("Verificando conexão com o banco de dados...");
            DatabaseConfig config = new DatabaseConfig();
            if (config.isConnectionValid(dbType)) {
                System.out.println("✅ Conexão com o banco de dados estabelecida!");
            } else {
                throw new DatabaseException("Conexão inválida");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro na conexão com o banco de dados: " + e.getMessage());
            throw new DatabaseException("Falha na conexão com o banco de dados", e);
        }
    }

    private void exibirInterfacePrincipal(String dbType) {
        try {
            GuiPrincipal janelaPrincipal = new GuiPrincipal(dbType);
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