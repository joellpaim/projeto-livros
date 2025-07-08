package combo.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável por gerenciar as configurações de banco de dados
 * Carrega configurações de um arquivo de propriedades
 */
public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private Properties properties;
    private static String currentDatabaseType = "postgresql"; // Padrão
    
    public DatabaseConfig() {
        loadProperties();
    }
    
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                // Se não encontrar o arquivo, usa configurações padrão
                setDefaultProperties();
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo de configuração: " + e.getMessage());
            setDefaultProperties();
        }
    }
    
    private void setDefaultProperties() {
        // Configurações padrão para PostgreSQL
        properties.setProperty("postgresql.driver", "org.postgresql.Driver");
        properties.setProperty("postgresql.url", "jdbc:postgresql://localhost:5432/livros");
        properties.setProperty("postgresql.username", "livros_user");
        properties.setProperty("postgresql.password", "livros_pass");
        
        // Configurações padrão para MySQL
        properties.setProperty("mysql.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("mysql.url", "jdbc:mysql://localhost:3306/livros");
        properties.setProperty("mysql.username", "livros_user");
        properties.setProperty("mysql.password", "livros_pass");
    }
    
    public String getDriver(String dbType) {
        return properties.getProperty(dbType.toLowerCase() + ".driver");
    }
    
    public String getUrl(String dbType) {
        return properties.getProperty(dbType.toLowerCase() + ".url");
    }
    
    public String getUsername(String dbType) {
        return properties.getProperty(dbType.toLowerCase() + ".username");
    }
    
    public String getPassword(String dbType) {
        return properties.getProperty(dbType.toLowerCase() + ".password");
    }
    
    public boolean isValidDatabaseType(String dbType) {
        return "postgresql".equalsIgnoreCase(dbType) || "mysql".equalsIgnoreCase(dbType);
    }
    
    /**
     * Obtém uma conexão com o banco de dados atual.
     * 
     * @return Connection objeto de conexão
     * @throws SQLException se houver erro na conexão
     */
    public Connection getConnection() throws SQLException {
        try {
            String driver = getDriver(currentDatabaseType);
            String url = getUrl(currentDatabaseType);
            String username = getUsername(currentDatabaseType);
            String password = getPassword(currentDatabaseType);
            
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver não encontrado: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verifica se a conexão com o banco de dados é válida.
     * 
     * @return true se a conexão for válida, false caso contrário
     */
    public boolean isConnectionValid() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Erro ao verificar conexão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Define o tipo de banco de dados atual.
     * 
     * @param dbType tipo do banco (postgresql ou mysql)
     */
    public static void setCurrentDatabaseType(String dbType) {
        if ("postgresql".equalsIgnoreCase(dbType) || "mysql".equalsIgnoreCase(dbType)) {
            currentDatabaseType = dbType.toLowerCase();
        }
    }
    
    /**
     * Obtém o tipo de banco de dados atual.
     * 
     * @return tipo do banco atual
     */
    public static String getCurrentDatabaseType() {
        return currentDatabaseType;
    }
}

