/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.controller;

import combo.bd.E_BD;
import combo.bo.BoCombo;
import combo.bo.BoConexao;
import combo.gui.GuiCombo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel
 */
public class CoCombo {
    // atributos

    private BoCombo bo;
    private GuiCombo gui;

    // construtor
    public CoCombo(GuiCombo gui, BoConexao conexao) {
        this.gui = gui;
        this.bo = new BoCombo(conexao);
    }

    /**
     * *
     * carrega os livros no combobox
     */
    public void carregarListaLivros() {
        try {
            // obter lista de livros
            ResultSet rs = this.getBo().listaLivros();

            // vai para o último registro
            rs.last();

            // pega o tamanho do registro
            int tamanho = rs.getRow();

            // volta para o primeiro registro
            rs.beforeFirst();

            // monta combobox
            String aux[] = new String[tamanho];

            // percorre o cursor
            int i = 0;
            while (rs.next()) {
                // monta o string
                aux[i] = rs.getString("titulo");
                i++;
            }

            // seta combobox
            this.getGui().getjComboBoxCliente().setModel(new javax.swing.DefaultComboBoxModel(aux));

            // mensagem
            JOptionPane.showMessageDialog(this.getGui(), "Carregados " + tamanho + " registros!",
                    "Teste TDD", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (E_BD ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(null, "Estouro de memória, compre mais !!! ",
                    "Consulta de Informações", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * carrega as informações de livros em uma jtable
     */
    public void obterLista() {
        try {
            // obter lista de livros e seus dados relacionados
            ResultSet rs = this.getBo().pesquisaDadosLivros();

            // cria objeto controlador da tela de consulta
            String title = "Consultar livros";
            CoConsulta controllerConsulta = new CoConsulta(new JFrame(),
                    true, rs, title);

            // mostrar consulta
            controllerConsulta.consultar();

            // pega retorno
            if (controllerConsulta.isRetorno()) {
                // pega objeto selecionado
                ArrayList objeto = controllerConsulta.getObjetoConsulta();

                // recupera todos os clientes deste horário
                JOptionPane.showMessageDialog(this.getGui(), objeto,
                        "Consultar livros", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (E_BD ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoCombo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BoCombo getBo() {
        return bo;
    }

    public GuiCombo getGui() {
        return gui;
    }
}
