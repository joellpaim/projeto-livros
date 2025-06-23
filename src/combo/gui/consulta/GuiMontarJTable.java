/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.gui.consulta;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author felipe.bogo
 */
public class GuiMontarJTable {

    private ResultSet consulta;

    public GuiMontarJTable(ResultSet consulta) {
        this.consulta = consulta;
    }

    public DefaultTableModel criaTabela() throws SQLException {

        DefaultTableModel dataModel;
        dataModel = this.monta_tabela();
        return dataModel;
    }

    public DefaultTableModel monta_tabela() throws SQLException {

        // variáveis locais
        DefaultTableModel dataModel;
        boolean registros = this.consulta.next();

        // testa quantidade de registros
        if (!registros) {
            System.out.println("Tabela Vazia.");
            return null;
        }

        // monta cabecalho
        dataModel = new DefaultTableModel(this.montarCabecalho(), 0);

        // montar linhas
        this.montarLinhas(dataModel);

        // retorna
        return dataModel;
    }

    private Object[] montarCabecalho() throws SQLException {
        // variáveis locais
        Object[] vetorColunas;
        int numColunas;
        ResultSetMetaData meta;

        // busca metadadados da consulta
        meta = consulta.getMetaData();

        // Busca quantidade de colunas
        numColunas = meta.getColumnCount();
        consulta.last();

        // Monta Vetor Colunas
        vetorColunas = new Object[numColunas];
        for (int j = 0; j < numColunas; j++) {
            vetorColunas[j] = meta.getColumnLabel(j + 1);
        }

        // retorna
        return vetorColunas;
    }

    private void montarLinhas(DefaultTableModel dataModel) throws SQLException {
        try {
            // variáveis locais
            Object[] vetorLinhas;
            int numColunas;
            ResultSetMetaData meta;

            // busca metadadados da consulta
            meta = consulta.getMetaData();

            // Busca quantidade de colunas
            numColunas = meta.getColumnCount();
            consulta.beforeFirst();

            // Monta Vetor linhas
            vetorLinhas = new Object[numColunas];
            while (consulta.next()) {
                for (int j = 0; j < numColunas; j++) {
                    switch (meta.getColumnType(j + 1)) {
                        case Types.VARCHAR:
                            vetorLinhas[j] = consulta.getString(j + 1).trim();
                            break;
                        case Types.CHAR:
                            vetorLinhas[j] = consulta.getString(j + 1).trim();
                            break;
                        case Types.INTEGER:
                            vetorLinhas[j] = consulta.getInt(j + 1);
                            break;
                        case Types.DATE:
                            vetorLinhas[j] = consulta.getDate(j + 1);
                            break;
                        case Types.TIMESTAMP:
                            vetorLinhas[j] = consulta.getDate(j + 1);
                            break;
                        case Types.DOUBLE:
                            vetorLinhas[j] = consulta.getDouble(j + 1);
                            break;
                        case Types.BOOLEAN:
                            vetorLinhas[j] = consulta.getBoolean(j + 1);
                            break;
                        default:
                            vetorLinhas[j] = consulta.getString(j + 1).trim();
                            break;
                    }
                }

                // adiciona linha ao data model
                dataModel.addRow(vetorLinhas);
            }
        } catch (OutOfMemoryError e) {
            JOptionPane.showMessageDialog(null, "Estouro de memória, compre mais !!! ",
                    "Consulta de Informações", JOptionPane.ERROR_MESSAGE);
        }
    }
}
