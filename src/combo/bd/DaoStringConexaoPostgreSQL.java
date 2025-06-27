/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.bd;

import combo.vo.VoConexao;

/**
 *
 * @author dlnotari
 */
public class DaoStringConexaoPostgreSQL implements DaoStringConexao {

    @Override
    public String getStringConexao(VoConexao vo) {
        // monta conexão
        String url = "jdbc:postgresql://" + vo.getHost() +
                ":" + vo.getPorta() + "/" + vo.getBaseDados();

        // Usar para MySQL
        // String url =
        // "jdbc:mysql://localhost:3306/livros?useSSL=false&serverTimezone=UTC";

        // mostra conexao
        System.out.println(url);

        // retorna
        return url;
    }

    @Override
    public VoConexao getConfiguracaoDefault() {
        // cria objeto
        VoConexao vo = new VoConexao();

        // seta valores
        vo.setSgbd("PostgreSQL");
        // vo.setHost("slinf30.ucs.br");
        // vo.setPorta("5432");
        // vo.setBaseDados("inf0211");
        // vo.setUsuario("alunos");
        // vo.setSenha("postgres");
        vo.setClassDriver("org.postgresql.Driver");

        // Alterado para o banco de dados livros via Docker
        vo.setHost("localhost");
        vo.setPorta("5432");
        vo.setBaseDados("livros");
        vo.setUsuario("livros_user");
        vo.setSenha("livros_pass");

        // returns
        return vo;
    }

    @Override
    public VoConexao getConfiguracaoAlternativa() {
        // cria objeto
        VoConexao vo = new VoConexao();

        // seta valores
        vo.setSgbd("MySQL");
        vo.setHost("localhost");
        vo.setPorta("3306");
        vo.setBaseDados("livros");
        vo.setUsuario("usuario");
        vo.setSenha("senha_usuario");
        vo.setClassDriver("com.mysql.cj.jdbc.Driver");

        return vo;
    }

}