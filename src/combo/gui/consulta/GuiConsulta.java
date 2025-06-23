package combo.gui.consulta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import combo.dao.ConsultaDAO;

public class GuiConsulta extends JDialog {

    private JTable tabela;
    private JScrollPane scrollPane;
    private JLabel lblPagina;
    private JButton btnPrimeira, btnAnterior, btnProxima, btnUltima;
    private int paginaAtual = 1;
    private final int itensPorPagina = 10;
    private int totalPaginas;
    private ConsultaDAO dao;

    public GuiConsulta(Frame parent, boolean modal, String title) {
        super(parent, modal);
        dao = new ConsultaDAO();
        atualizarTotalPaginas(); // Calcula o total de páginas

        initComponents();
        setTitle(title);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);

        // Carrega a primeira página imediatamente
        carregarPagina(1);
        atualizarBotoes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tabela = new JTable();
        scrollPane = new JScrollPane(tabela);
        add(scrollPane, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel();
        btnPrimeira = new JButton("<<");
        btnAnterior = new JButton("<");
        lblPagina = new JLabel();
        btnProxima = new JButton(">");
        btnUltima = new JButton(">>");

        updateLabel();

        paginationPanel.add(btnPrimeira);
        paginationPanel.add(btnAnterior);
        paginationPanel.add(lblPagina);
        paginationPanel.add(btnProxima);
        paginationPanel.add(btnUltima);

        add(paginationPanel, BorderLayout.SOUTH);

        btnPrimeira.addActionListener(e -> mudarPagina(1));
        btnAnterior.addActionListener(e -> mudarPagina(paginaAtual - 1));
        btnProxima.addActionListener(e -> mudarPagina(paginaAtual + 1));
        btnUltima.addActionListener(e -> mudarPagina(totalPaginas));
    }

    private void atualizarTotalPaginas() {
        int totalItens = dao.getTotalRegistros();
        totalPaginas = (int) Math.ceil((double) totalItens / itensPorPagina);
        totalPaginas = Math.max(totalPaginas, 1);
    }

    private void mudarPagina(int novaPagina) {
        int paginaDestino = Math.max(1, Math.min(novaPagina, totalPaginas));
        if (paginaDestino != paginaAtual) {
            paginaAtual = paginaDestino;
            carregarPagina(paginaAtual);
            updateLabel();
            atualizarBotoes();
        }
    }

    private void carregarPagina(int pagina) {
        try {
            paginaAtual = Math.max(1, Math.min(pagina, totalPaginas));
            int offset = (paginaAtual - 1) * itensPorPagina;

            ResultSet rs = dao.getDadosPaginadosAsResultSet(offset, itensPorPagina);
            GuiMontarJTable montador = new GuiMontarJTable(rs);
            DefaultTableModel modelo = montador.criaTabela();

            if (modelo != null) {
                tabela.setModel(modelo);
            }

            updateLabel();
            atualizarBotoes();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateLabel() {
        lblPagina.setText(String.format("Página %d de %d", paginaAtual, totalPaginas));
    }

    private void atualizarBotoes() {
        btnPrimeira.setEnabled(paginaAtual > 1);
        btnAnterior.setEnabled(paginaAtual > 1);
        btnProxima.setEnabled(paginaAtual < totalPaginas);
        btnUltima.setEnabled(paginaAtual < totalPaginas);
    }

    // Métodos para integração com CoConsulta
    public JTable getjTable() {
        return this.tabela;
    }

    public void setjTable(JTable tabela) {
        this.tabela = tabela;
    }

    public JScrollPane getjScrollPane() {
        return this.scrollPane;
    }

    public void setjScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiConsulta dialog = new GuiConsulta(null, true, "Consulta com Paginação e PostgreSQL");
            dialog.setVisible(true);
        });
    }
}
