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
public interface DaoStringConexao {

    public String getStringConexao(VoConexao vo);
    public VoConexao getConfiguracaoDefault();
    public VoConexao getConfiguracaoAlternativa();
}
