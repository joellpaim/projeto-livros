package combo.factory;

import combo.config.DatabaseConfig;
import combo.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory para criação de conexões com banco de dados
 * Suporta PostgreSQL e MySQL
 */
public class ConnectionFactory {
    private static DatabaseConfig config = new DatabaseConfig();
    
    /**
     * Cria uma conexão com o banco de dados especificado
     * @param dbType Tipo do banco (postgresql ou mysql)
     * @return Conexão com o banco de dados
     * @throws DatabaseException Se houver erro na conexão
     */
    public static Connection createConnection(String dbType) throws DatabaseException {
        if (!config.isValidDatabaseType(dbType)) {
            throw new DatabaseException("Tipo de banco de dados inválido: " + dbType);
        }
        
        try {
            // Carrega o driver
            Class.forName(config.getDriver(dbType));
            
            // Cria a conexão
            Connection connection = DriverManager.getConnection(
                config.getUrl(dbType),
                config.getUsername(dbType),
                config.getPassword(dbType)
            );
            
            connection.setAutoCommit(false);
            return connection;
            
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Driver não encontrado para " + dbType, e);
        } catch (SQLException e) {
            throw new DatabaseException("Erro ao conectar com " + dbType, e);
        }
    }
    
    /**
     * Fecha uma conexão de forma segura
     * @param connection Conexão a ser fechada
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
}

