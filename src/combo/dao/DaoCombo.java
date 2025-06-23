/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.dao;

import combo.bo.BoConexao;
import combo.bd.E_BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author dlnotari
 */
public class DaoCombo {
    // atributos
    private BoConexao conexao;

    // construtor
    public DaoCombo(BoConexao conexao) {
        this.conexao = conexao;
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet listaLivros() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select titulo from livros"
        // + " limit 1200000"
        ;

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);

        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);

        // return
        return rs;
    }

    /**
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet pesquisaDadosLivros(int limite) throws SQLException, E_BD, ClassNotFoundException {
        String sql = "SELECT l.titulo, a.nome, e.numero, e.ano " +
                "FROM livros l " +
                "INNER JOIN livroAutor la ON l.codigo = la.codigoLivro " +
                "INNER JOIN autor a ON a.codigo = la.codigoAutor " +
                "INNER JOIN edicao e ON a.codigo = e.codigoLivro " +
                "LIMIT ?";

        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);
        ps.setInt(1, limite);
        return this.getConexao().getBd().consulta(ps);
    }

    // getter
    public BoConexao getConexao() {
        return conexao;
    }
}
