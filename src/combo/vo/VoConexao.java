package combo.vo;

public class VoConexao {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String sgbd;
    private String host;
    private String porta;
    private String baseDados;
    private String usuario;
    private String senha;
    private String classDriver;

    public VoConexao(String sgbd, String host, String porta, String baseDados,
            String usuario, String senha, String classDriver) {
        this.sgbd = sgbd;
        this.host = host;
        this.porta = porta;
        this.baseDados = baseDados;
        this.usuario = usuario;
        this.senha = senha;
        this.classDriver = classDriver;
    }

    public VoConexao() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VoConexao other = (VoConexao) obj;
        if ((this.sgbd == null) ? (other.sgbd != null) : !this.sgbd.equals(other.sgbd)) {
            return false;
        }
        if ((this.host == null) ? (other.host != null) : !this.host.equals(other.host)) {
            return false;
        }
        if ((this.porta == null) ? (other.porta != null) : !this.porta.equals(other.porta)) {
            return false;
        }
        if ((this.baseDados == null) ? (other.baseDados != null) : !this.baseDados.equals(other.baseDados)) {
            return false;
        }
        if ((this.usuario == null) ? (other.usuario != null) : !this.usuario.equals(other.usuario)) {
            return false;
        }
        if ((this.senha == null) ? (other.senha != null) : !this.senha.equals(other.senha)) {
            return false;
        }
        if ((this.classDriver == null) ? (other.classDriver != null) : !this.classDriver.equals(other.classDriver)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.sgbd != null ? this.sgbd.hashCode() : 0);
        hash = 59 * hash + (this.host != null ? this.host.hashCode() : 0);
        hash = 59 * hash + (this.porta != null ? this.porta.hashCode() : 0);
        hash = 59 * hash + (this.baseDados != null ? this.baseDados.hashCode() : 0);
        hash = 59 * hash + (this.usuario != null ? this.usuario.hashCode() : 0);
        hash = 59 * hash + (this.senha != null ? this.senha.hashCode() : 0);
        hash = 59 * hash + (this.classDriver != null ? this.classDriver.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "VO_Conexao [sgbd =" + sgbd + " baseDados=" + baseDados +
                ", host=" + host + ", porta=" + porta + ", senha=" + senha +
                ", usuario=" + usuario + " Class Driver="+classDriver+"]";
    }

    public String getSgbd() {
        return sgbd;
    }

    public void setSgbd(String sgbd) {
        this.sgbd = sgbd;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getBaseDados() {
        return baseDados;
    }

    public void setBaseDados(String baseDados) {
        this.baseDados = baseDados;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getClassDriver() {
        return classDriver;
    }

    public void setClassDriver(String classDriver) {
        this.classDriver = classDriver;
    }
}
