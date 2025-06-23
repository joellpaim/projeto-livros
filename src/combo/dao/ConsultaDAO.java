package combo.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // private String url = "jdbc:postgresql://localhost:5432/livros";
    // private String usuario = "postgres";
    // private String senha = "15Moto05!";

    // Alterado para o banco de dados livros via Docker
    private String url = "jdbc:postgresql://localhost:5432/livros";
    private String usuario = "livros_user";
    private String senha = "livros_pass";


    public int getTotalPaginas(int itensPorPagina) {
        String sql = "SELECT COUNT(*) FROM livros";
        try (Connection conn = DriverManager.getConnection(url, usuario, senha);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int totalRegistros = rs.getInt(1);
                return (int) Math.ceil((double) totalRegistros / itensPorPagina);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public List<String[]> getDadosPagina(int pagina, int itensPorPagina) {
        List<String[]> dados = new ArrayList<>();
        int offset = (pagina - 1) * itensPorPagina;

        String sql = "SELECT id, titulo FROM livros ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DriverManager.getConnection(url, usuario, senha);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, itensPorPagina);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                dados.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("titulo")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dados;
    }
}
