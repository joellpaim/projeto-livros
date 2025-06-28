/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.bd;

import combo.vo.VoConexao;
import java.sql.*;

/**
 *
 * @author felipe.bogo
 */
public class DaoConectarBD {

    private VoConexao voConexao;
    private Connection conexao;

    private DaoConectarBD(VoConexao voConexao, Connection conexao) {
        this.voConexao = voConexao;
        this.conexao = conexao;
    }

    public DaoConectarBD() throws E_BD, ClassNotFoundException, SQLException {
        this(null, null);
    }

    public Connection conectar(String tipoBanco) throws E_BD, java.lang.ClassNotFoundException, SQLException {

        // Declara a variável com o tipo da interface
        DaoStringConexao conexaoConfig;

        // Define o tipo de conexão com base no banco
        if ("mysql".equalsIgnoreCase(tipoBanco)) {
            conexaoConfig = new DaoStringConexaoMySQL();
            this.voConexao = conexaoConfig.getConfiguracaoDefault();
        } else {
            conexaoConfig = new DaoStringConexaoPostgreSQL();
            this.voConexao = conexaoConfig.getConfiguracaoDefault();
        }

        // testa dados da conexão, se não existem gera exceção
        if ((this.getVoConexao() == null) || (this.getVoConexao().getBaseDados() == null)
                || (this.getVoConexao().getHost() == null) || (this.getVoConexao().getPorta() == null)
                || (this.getVoConexao().getSenha() == null) || (this.getVoConexao().getSgbd() == null)
                || (this.getVoConexao().getUsuario() == null)) {
            throw new E_BD("Não foi possível conectar com o SGBD com as" +
                    " informações " + this.getVoConexao());
        }

        // pega configuração da conexao
        String url = conexaoConfig.getStringConexao(this.getVoConexao());

        // carrega o Driver
        Class.forName(this.getVoConexao().getClassDriver());

        // faz a conexao com o SGBD
        conexao = DriverManager.getConnection(url,
                this.getVoConexao().getUsuario(),
                this.getVoConexao().getSenha());
        conexao.setAutoCommit(false);

        // retorna conexao
        return conexao;
    }

    public void desConectar() throws SQLException {
        conexao.close();
    }

    public VoConexao getVoConexao() {
        return voConexao;
    }

    public void setVoConexao(VoConexao voConexao) {
        this.voConexao = voConexao;
    }

    public Connection getConexao() {
        return conexao;
    }
}
