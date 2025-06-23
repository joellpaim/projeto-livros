package combo.gui.consulta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
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
        totalPaginas = dao.getTotalPaginas(itensPorPagina);

        initComponents();
        setTitle(title);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(800, 600);
        carregarPagina(paginaAtual);
    }

    private void initComponents() {
        setLayout(null);

        tabela = new JTable();
        scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(0, 0, 800, 400);
        add(scrollPane);

        JPanel paginationPanel = new JPanel();
        paginationPanel.setBounds(0, 420, 800, 50);
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

        add(paginationPanel);

        btnPrimeira.addActionListener(e -> mudarPagina(1));
        btnAnterior.addActionListener(e -> mudarPagina(paginaAtual - 1));
        btnProxima.addActionListener(e -> mudarPagina(paginaAtual + 1));
        btnUltima.addActionListener(e -> mudarPagina(totalPaginas));
    }

    private void mudarPagina(int novaPagina) {
        if (novaPagina >= 1 && novaPagina <= totalPaginas) {
            paginaAtual = novaPagina;
            updateLabel();
            carregarPagina(paginaAtual);
        }
    }

    private void carregarPagina(int pagina) {
        List<String[]> dados = dao.getDadosPagina(pagina, itensPorPagina);
        String[] colunas = { "Código", "Título" };
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        for (String[] linha : dados) {
            modelo.addRow(linha);
        }
        tabela.setModel(modelo);
    }

    private void updateLabel() {
        lblPagina.setText("Página " + paginaAtual + " de " + totalPaginas);
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

