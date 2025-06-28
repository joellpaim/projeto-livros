package combo.bd;

import combo.vo.VoConexao;

public class DaoStringConexaoMySQL implements DaoStringConexao {

    @Override
    public String getStringConexao(VoConexao vo) {
        // Monta a URL de conexão para MySQL
        String url = "jdbc:mysql://" + vo.getHost() +
                ":" + vo.getPorta() + "/" + vo.getBaseDados() +
                "?useSSL=false&serverTimezone=UTC";

        System.out.println(url); // Mostra a URL montada
        return url;
    }

    @Override
    public VoConexao getConfiguracaoDefault() {
        VoConexao vo = new VoConexao();

        vo.setSgbd("MySQL");
        vo.setHost("localhost");
        vo.setPorta("3306");
        vo.setBaseDados("livros");
        vo.setUsuario("usuario");
        vo.setSenha("senha_usuario");
        vo.setClassDriver("com.mysql.cj.jdbc.Driver");

        return vo;
    }

    @Override
    public VoConexao getConfiguracaoAlternativa() {
        return getConfiguracaoDefault();
    }
}
