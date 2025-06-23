/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package combo.bd;

import javax.swing.JOptionPane;

/**
 *
 * @author dlnotari
 */
public class E_BD extends Exception {

    public E_BD(String message) {
        super(message);
        showMessage();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    private void showMessage() {
        JOptionPane.showConfirmDialog(null, this.getMessage(),
                "Exceção gerada no acesso ao SGBD",
                JOptionPane.DEFAULT_OPTION);
    }
}
