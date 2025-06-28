/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package combo.bo;

import combo.bd.DaoConectarBD;
import combo.bd.DaoConsultarBD;
import combo.bd.E_BD;
import java.sql.SQLException;

/**
 *
 * @author daniel
 */
public class BoConexao {
    private DaoConectarBD conexao;
    private DaoConsultarBD bd;

    public BoConexao() {
    }

    public void conectar(String tipoBanco) throws E_BD, ClassNotFoundException, SQLException {
        // testa se existe conexao
        if (this.getConexao() == null) {
            // cria conexao
            this.setConexao(new DaoConectarBD());

            // cria objeto de consulta
            this.setBd(new DaoConsultarBD(this.getConexao()));

            // conectar
            this.getConexao().conectar(tipoBanco);
        }
    }

    public void desconectar() throws SQLException {
        this.getConexao().desConectar();
    }

    public DaoConectarBD getConexao() {
        return conexao;
    }

    private void setConexao(DaoConectarBD conexao) {
        this.conexao = conexao;
    }

    public DaoConsultarBD getBd() {
        return bd;
    }

    private void setBd(DaoConsultarBD bd) {
        this.bd = bd;
    }
}
