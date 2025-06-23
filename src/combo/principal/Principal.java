/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.principal;

import combo.bo.BoConexao;
import combo.gui.GuiCombo;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author dlnotari
 */
public class Principal {
    // atributos
    private GuiCombo gui;
    private BoConexao conexao;

    // construtor
    public Principal() {
        this.conexao = new BoConexao();
    }

    /**
     * conectar
     */
    private void conectar() {
        try {
            // conectar
            this.getConexao().conectar();
            System.out.println("conectou");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao conectar no banco de dados!"
                            + ex.toString(),
                    "Manipular tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
            System.out.println(ex.toString());
        }
    }

    /**
     * desconectar do banco de dados
     */
    private void desconectar() {
        try {
            // desconectar
            this.getConexao().desconectar();
            System.out.println("desconectou");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(new javax.swing.JFrame(),
                    "Erro ao desconectar no banco de dados!"
                            + ex.toString(),
                    "Manipular tabelas do banco de dados!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public BoConexao getConexao() {
        return conexao;
    }

    /**
     * *
     * controle de execucao
     */
    public void executar() {
        // conectar
        this.conectar();

        // cria tela
        this.gui = new GuiCombo(true, conexao);

        // mostra tela
        this.gui.setVisible(true);

        // desconescta
        this.desconectar();
    }

    // main
    public static void main(String args[]) {
        Principal p = new Principal();
        p.executar();

    }
}
