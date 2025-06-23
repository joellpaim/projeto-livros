/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.bo;

import combo.bd.E_BD;
import combo.dao.DaoCombo;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Daniel
 */
public class BoCombo {
    // atributos
    private BoConexao conexao;
    private DaoCombo dao;
    private static final int LIMITE_PADRAO = 10;

    // construtor
    public BoCombo(BoConexao conexao) {
        this.conexao = conexao;
        this.dao = new DaoCombo(conexao);
    }

    /**
     * obtem lista de livros
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet listaLivros() throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().listaLivros();
    }

    /**
     * obtem lista de livros e seus dados relacionados
     * 
     * @return
     * @throws SQLException
     * @throws E_BD
     * @throws ClassNotFoundException
     */
    public ResultSet pesquisaDadosLivros() throws SQLException, E_BD, ClassNotFoundException {
        return this.getDao().pesquisaDadosLivros(LIMITE_PADRAO);
    }

    // getters
    public BoConexao getConexao() {
        return conexao;
    }

    public DaoCombo getDao() {
        return dao;
    }

}
