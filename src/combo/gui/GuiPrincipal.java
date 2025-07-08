package combo.gui;

import combo.exception.DatabaseException;
import combo.service.LivroService;
import combo.util.PaginationResult;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interface principal da aplicação com design moderno
 */
public class GuiPrincipal extends JFrame {
    private LivroService livroService;
    private String databaseType;

    // Componentes da interface
    private JTable tabelaLivros;
    private DefaultTableModel modeloTabela;
    private JTextField campoBusca;
    private JLabel labelInfo;
    private JLabel labelPaginacao;
    private JButton btnPrimeira, btnAnterior, btnProxima, btnUltima;
    private JComboBox<String> comboLivros;
    private JSpinner spinnerTamanhoPagina;

    // Controle de paginação
    private PaginationResult<String[]> resultadoAtual;
    private int paginaAtual = 1;
    private int tamanhoPagina = 20;

    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SECUNDARIA = new Color(52, 152, 219);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    private static final Color COR_BORDA = new Color(189, 195, 199);

    public GuiPrincipal(String databaseType) {
        this.databaseType = databaseType;
        this.livroService = new LivroService(databaseType);

        inicializarInterface();
        carregarDados();
    }

    private void inicializarInterface() {
        setTitle("Sistema de Gerenciamento de Livros - " + databaseType.toUpperCase());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Configura o layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(COR_FUNDO);

        // Cria os painéis
        add(criarPainelSuperior(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
        add(criarPainelInferior(), BorderLayout.SOUTH);

        // Configura ícone da aplicação (se disponível)
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
        } catch (Exception e) {
            // Ignora se não encontrar o ícone
        }
    }

    private JPanel criarPainelSuperior() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_PRIMARIA);
        painel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Título
        JLabel titulo = new JLabel("Sistema de Gerenciamento de Livros");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);

        // Informações do banco
        JLabel infoBanco = new JLabel("Banco: " + databaseType.toUpperCase());
        infoBanco.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoBanco.setForeground(Color.WHITE);

        painel.add(titulo, BorderLayout.WEST);
        painel.add(infoBanco, BorderLayout.EAST);

        return painel;
    }

    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(20, 20, 10, 20));

        // Painel de busca
        painel.add(criarPainelBusca(), BorderLayout.NORTH);

        // Tabela de livros
        painel.add(criarPainelTabela(), BorderLayout.CENTER);

        // Painel de combo
        painel.add(criarPainelCombo(), BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        painel.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar por título:");
        labelBusca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelBusca.setForeground(COR_TEXTO);

        campoBusca = new JTextField(20);
        campoBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton btnBuscar = criarBotao("Buscar", COR_SECUNDARIA);
        btnBuscar.addActionListener(e -> buscarLivros());

        JButton btnLimpar = criarBotao("Limpar", new Color(231, 76, 60));
        btnLimpar.addActionListener(e -> limparBusca());

        // Configurações de paginação
        JLabel labelTamanho = new JLabel("Itens por página:");
        labelTamanho.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelTamanho.setForeground(COR_TEXTO);

        spinnerTamanhoPagina = new JSpinner(new SpinnerNumberModel(20, 5, 100, 5));
        spinnerTamanhoPagina.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerTamanhoPagina.addChangeListener(e -> {
            tamanhoPagina = (Integer) spinnerTamanhoPagina.getValue();
            paginaAtual = 1;
            carregarDados();
        });

        painel.add(labelBusca);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(campoBusca);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnBuscar);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnLimpar);
        painel.add(Box.createHorizontalStrut(30));
        painel.add(labelTamanho);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(spinnerTamanhoPagina);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createLineBorder(COR_BORDA));

        // Modelo da tabela
        String[] colunas = { "Código", "Título", "Ano" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaLivros = new JTable(modeloTabela);
        tabelaLivros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelaLivros.setRowHeight(25);
        tabelaLivros.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelaLivros.getTableHeader().setBackground(COR_PRIMARIA);
        tabelaLivros.getTableHeader().setForeground(Color.WHITE);
        tabelaLivros.setSelectionBackground(new Color(174, 214, 241));
        tabelaLivros.setGridColor(COR_BORDA);

        // Configura larguras das colunas
        tabelaLivros.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabelaLivros.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabelaLivros.getColumnModel().getColumn(2).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        scrollPane.setBorder(null);

        painel.add(scrollPane, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelCombo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBackground(COR_FUNDO);

        JLabel labelCombo = new JLabel("Livros (ComboBox):");
        labelCombo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelCombo.setForeground(COR_TEXTO);

        comboLivros = new JComboBox<>();
        comboLivros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboLivros.setPreferredSize(new Dimension(400, 30));

        JButton btnCarregarCombo = criarBotao("Carregar ComboBox", COR_SECUNDARIA);
        btnCarregarCombo.addActionListener(e -> carregarComboBox());

        painel.add(labelCombo);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(comboLivros);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnCarregarCombo);

        return painel;
    }

    private JPanel criarPainelInferior() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(10, 20, 15, 20));

        // Informações e paginação
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelInfo.setBackground(COR_FUNDO);

        labelInfo = new JLabel("Carregando...");
        labelInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelInfo.setForeground(COR_TEXTO);

        labelPaginacao = new JLabel("");
        labelPaginacao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPaginacao.setForeground(COR_PRIMARIA);

        painelInfo.add(labelInfo);
        painelInfo.add(Box.createHorizontalStrut(20));
        painelInfo.add(labelPaginacao);

        // Controles de paginação
        JPanel painelControles = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelControles.setBackground(COR_FUNDO);

        btnPrimeira = criarBotao("<<", COR_PRIMARIA);
        btnAnterior = criarBotao("<", COR_PRIMARIA);
        btnProxima = criarBotao(">", COR_PRIMARIA);
        btnUltima = criarBotao(">>", COR_PRIMARIA);

        btnPrimeira.addActionListener(e -> irParaPrimeiraPagina());
        btnAnterior.addActionListener(e -> irParaPaginaAnterior());
        btnProxima.addActionListener(e -> irParaProximaPagina());
        btnUltima.addActionListener(e -> irParaUltimaPagina());

        painelControles.add(btnPrimeira);
        painelControles.add(btnAnterior);
        painelControles.add(btnProxima);
        painelControles.add(btnUltima);

        painel.add(painelInfo, BorderLayout.WEST);
        painel.add(painelControles, BorderLayout.EAST);

        return painel;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });

        return botao;
    }

    private void carregarDados() {
        SwingUtilities.invokeLater(() -> {
            try {
                String termoBusca = campoBusca.getText().trim();

                if (termoBusca.isEmpty()) {
                    resultadoAtual = livroService.getLivrosPaginados(paginaAtual, tamanhoPagina);
                } else {
                    resultadoAtual = livroService.buscarLivrosPorTitulo(termoBusca, paginaAtual, tamanhoPagina);
                }

                atualizarTabela();
                atualizarControlesPaginacao();

            } catch (DatabaseException e) {
                mostrarErro("Erro ao carregar dados: " + e.getMessage());
            }
        });
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);

        if (resultadoAtual != null) {
            for (String[] linha : resultadoAtual.getData()) {
                modeloTabela.addRow(linha);
            }
        }
    }

    private void atualizarControlesPaginacao() {
        if (resultadoAtual != null) {
            labelInfo.setText(String.format("Total: %d registros", resultadoAtual.getTotalRecords()));
            labelPaginacao.setText(resultadoAtual.toString());

            btnPrimeira.setEnabled(resultadoAtual.hasPreviousPage());
            btnAnterior.setEnabled(resultadoAtual.hasPreviousPage());
            btnProxima.setEnabled(resultadoAtual.hasNextPage());
            btnUltima.setEnabled(resultadoAtual.hasNextPage());
        }
    }

    private void buscarLivros() {
        paginaAtual = 1;
        carregarDados();
    }

    private void limparBusca() {
        campoBusca.setText("");
        paginaAtual = 1;
        carregarDados();
    }

    private void carregarComboBox() {
        try {
            List<String> livros = livroService.getLivrosParaCombo();
            comboLivros.removeAllItems();

            for (String livro : livros) {
                comboLivros.addItem(livro);
            }

            JOptionPane.showMessageDialog(this,
                    "ComboBox carregado com " + livros.size() + " livros!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (DatabaseException e) {
            mostrarErro("Erro ao carregar ComboBox: " + e.getMessage());
        }
    }

    private void irParaPrimeiraPagina() {
        paginaAtual = 1;
        carregarDados();
    }

    private void irParaPaginaAnterior() {
        if (resultadoAtual != null && resultadoAtual.hasPreviousPage()) {
            paginaAtual = resultadoAtual.getPreviousPage();
            carregarDados();
        }
    }

    private void irParaProximaPagina() {
        if (resultadoAtual != null && resultadoAtual.hasNextPage()) {
            paginaAtual = resultadoAtual.getNextPage();
            carregarDados();
        }
    }

    private void irParaUltimaPagina() {
        if (resultadoAtual != null) {
            paginaAtual = resultadoAtual.getTotalPages();
            carregarDados();
        }
    }

    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
