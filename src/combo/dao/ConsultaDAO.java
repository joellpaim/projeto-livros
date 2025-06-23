package combo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost:5432/livros";
    private static final String USUARIO = "livros_user";
    private static final String SENHA = "livros_pass";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC não encontrado", e);
        }
    }

    public int getTotalRegistros() {
        String sql = "SELECT COUNT(*) FROM livros";
        try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter total de registros", e);
        }
    }

    public List<String[]> getDadosPaginados(int offset, int limit) {
        List<String[]> dados = new ArrayList<>();
        String sql = "SELECT codigo, titulo FROM livros ORDER BY codigo LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dados.add(new String[] {
                            String.valueOf(rs.getInt("codigo")),
                            rs.getString("titulo")
                    });
                }
            }
            return dados;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter dados paginados", e);
        }
    }

    public ResultSet getDadosPaginadosAsResultSet(int offset, int limit) throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
        String sql = "SELECT codigo, titulo FROM livros ORDER BY codigo LIMIT ? OFFSET ?";
        PreparedStatement stmt = conn.prepareStatement(sql,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);

        stmt.setInt(1, limit);
        stmt.setInt(2, offset);
        return stmt.executeQuery();
    }

    public int getTotalPaginas(int itensPorPagina) {
        int total = getTotalRegistros();
        return (int) Math.ceil((double) total / itensPorPagina);
    }

    public List<String[]> getDadosPagina(int pagina, int itensPorPagina) {
        int offset = (pagina - 1) * itensPorPagina;
        return getDadosPaginados(offset, itensPorPagina);
    }
}