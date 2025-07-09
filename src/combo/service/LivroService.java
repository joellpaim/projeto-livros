package combo.service;

import combo.exception.DatabaseException;
import combo.factory.ConnectionFactory;
import combo.util.PaginationResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para operações relacionadas a livros
 */
public class LivroService {
    private String databaseType;

    public LivroService(String databaseType) {
        this.databaseType = databaseType;
    }

    /**
     * Obtém todos os livros com paginação
     * 
     * @param page     Página atual (começando em 1)
     * @param pageSize Número de itens por página
     * @return Resultado paginado
     * @throws DatabaseException Se houver erro na consulta
     */
    public PaginationResult<String[]> getLivrosPaginados(int page, int pageSize) throws DatabaseException {
        if (page < 1) {
            throw new IllegalArgumentException("O número da página deve ser maior que zero");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("O tamanho da página deve ser maior que zero");
        }

        int offset = (page - 1) * pageSize;

        try (Connection conn = ConnectionFactory.createConnection(databaseType)) {
            // Conta total de registros
            int totalRecords = getTotalLivros(conn);

            // Busca registros da página atual
            List<String[]> livros = getLivrosPorPagina(conn, offset, pageSize);

            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            return new PaginationResult<>(livros, page, pageSize, totalRecords, totalPages);

        } catch (SQLException e) {
            String errorMsg = String.format(
                    "Erro ao buscar livros paginados (página: %d, tamanho página: %d). SQL State: %s, Error Code: %d, Mensagem: %s",
                    page, pageSize, e.getSQLState(), e.getErrorCode(), e.getMessage());
            throw new DatabaseException(errorMsg, e);
        }
    }

    /**
     * Obtém lista de livros para combobox
     * 
     * @return Lista de livros formatada para combobox
     * @throws DatabaseException Se houver erro na consulta
     */
    public List<String> getLivrosParaCombo() throws DatabaseException {
        List<String> livros = new ArrayList<>();
        String sql = "SELECT codigo, titulo FROM livros ORDER BY titulo LIMIT 100";

        try (Connection conn = ConnectionFactory.createConnection(databaseType);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                try {
                    String item = rs.getInt("codigo") + " - " + rs.getString("titulo");
                    livros.add(item);
                } catch (SQLException e) {
                    String errorMsg = String.format(
                            "Erro ao processar registro (linha %d). SQL State: %s, Error Code: %d, Mensagem: %s",
                            livros.size() + 1, e.getSQLState(), e.getErrorCode(), e.getMessage());
                    throw new DatabaseException(errorMsg, e);
                }
            }

        } catch (SQLException e) {
            String errorMsg = String.format(
                    "Erro ao buscar livros para combo. SQL State: %s, Error Code: %d, Mensagem: %s. Query: %s",
                    e.getSQLState(), e.getErrorCode(), e.getMessage(), sql);
            throw new DatabaseException(errorMsg, e);
        }

        return livros;
    }

    /**
     * Busca livros por título
     * 
     * @param titulo   Título ou parte do título
     * @param page     Página atual
     * @param pageSize Itens por página
     * @return Resultado paginado
     * @throws DatabaseException Se houver erro na consulta
     */
    public PaginationResult<String[]> buscarLivrosPorTitulo(String titulo, int page, int pageSize)
            throws DatabaseException {

        // Validação dos parâmetros de entrada
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio");
        }
        if (page < 1) {
            throw new IllegalArgumentException("O número da página deve ser maior que zero");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("O tamanho da página deve ser maior que zero");
        }

        int offset = (page - 1) * pageSize;

        try (Connection conn = ConnectionFactory.createConnection(databaseType)) {
            // Conta total de registros que atendem ao critério
            int totalRecords = getTotalLivrosPorTitulo(conn, titulo);

            // Busca registros da página atual
            List<String[]> livros = getLivrosPorTituloPaginado(conn, titulo, offset, pageSize);

            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            return new PaginationResult<>(livros, page, pageSize, totalRecords, totalPages);

        } catch (SQLException e) {
            String errorMsg = String.format(
                    "Erro ao buscar livros por título. Parâmetros: título='%s', página=%d, tamanhoPágina=%d. " +
                            "SQL State: %s, Error Code: %d, Mensagem: %s",
                    titulo, page, pageSize, e.getSQLState(), e.getErrorCode(), e.getMessage());
            throw new DatabaseException(errorMsg, e);
        }
    }

    private int getTotalLivros(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livros";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private List<String[]> getLivrosPorPagina(Connection conn, int offset, int pageSize) throws SQLException {
        List<String[]> livros = new ArrayList<>();
        String sql = "SELECT codigo, titulo FROM livros ORDER BY codigo LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(new String[] {
                            String.valueOf(rs.getInt("codigo")),
                            rs.getString("titulo")
                    });
                }
            }
        }

        return livros;
    }

    private int getTotalLivrosPorTitulo(Connection conn, String titulo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM livros WHERE LOWER(titulo) LIKE LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private List<String[]> getLivrosPorTituloPaginado(Connection conn, String titulo, int offset, int pageSize)
            throws SQLException {
        List<String[]> livros = new ArrayList<>();
        String sql = "SELECT codigo, titulo FROM livros WHERE LOWER(titulo) LIKE LOWER(?) ORDER BY titulo LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + titulo + "%");
            stmt.setInt(2, pageSize);
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    livros.add(new String[] {
                            String.valueOf(rs.getInt("codigo")),
                            rs.getString("titulo")
                    });
                }
            }
        }

        return livros;
    }
}
