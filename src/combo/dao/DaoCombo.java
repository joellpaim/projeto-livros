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
                 //   + " limit 1200000"
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
    public ResultSet pesquisaDadosLivros() throws SQLException, E_BD, ClassNotFoundException {
        // consultar o código
        String sql = "select l.titulo, a.nome, e.numero, e.ano"
                + "   from livros l inner join livroAutor la on l.codigo = la.codigoLivro "
                + "   inner join autor a on a.codigo = la.codigoAutor"
                + "   inner join edicao e on a.codigo = e.codigoLivro"
               // + "   limit 1000000"
                ;

        // obtem objeto
        PreparedStatement ps = this.getConexao().getBd().getStatement(sql);
        
        // executar sql
        ResultSet rs = this.getConexao().getBd().consulta(ps);
        
        // return
        return rs;
    }  
    
    
    // getter
    public BoConexao getConexao() {
        return conexao;
    }
}
