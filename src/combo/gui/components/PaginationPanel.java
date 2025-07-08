package combo.gui.components;

import combo.util.PaginationController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Componente reutilizável para controles de paginação
 */
public class PaginationPanel extends JPanel {
    private PaginationController controller;
    private ActionListener pageChangeListener;
    
    // Componentes
    private JLabel labelInfo;
    private JPanel painelBotoes;
    private JButton btnFirst, btnPrevious, btnNext, btnLast;
    private JTextField fieldGoToPage;
    private JComboBox<Integer> comboPageSize;
    
    // Cores do tema
    private static final Color COR_PRIMARIA = new Color(41, 128, 185);
    private static final Color COR_SECUNDARIA = new Color(52, 152, 219);
    private static final Color COR_FUNDO = new Color(236, 240, 241);
    private static final Color COR_TEXTO = new Color(44, 62, 80);
    private static final Color COR_BORDA = new Color(189, 195, 199);
    
    public PaginationPanel(PaginationController controller, ActionListener pageChangeListener) {
        this.controller = controller;
        this.pageChangeListener = pageChangeListener;
        
        inicializarComponentes();
        configurarLayout();
        atualizarInterface();
    }
    
    private void inicializarComponentes() {
        setBackground(COR_FUNDO);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Label de informações
        labelInfo = new JLabel();
        labelInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelInfo.setForeground(COR_TEXTO);
        
        // Botões de navegação
        btnFirst = criarBotaoNavegacao("<<", "Primeira página");
        btnPrevious = criarBotaoNavegacao("<", "Página anterior");
        btnNext = criarBotaoNavegacao(">", "Próxima página");
        btnLast = criarBotaoNavegacao(">>", "Última página");
        
        // Campo "Ir para página"
        fieldGoToPage = new JTextField(4);
        fieldGoToPage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fieldGoToPage.setHorizontalAlignment(JTextField.CENTER);
        fieldGoToPage.addActionListener(e -> irParaPagina());
        
        // ComboBox para tamanho da página
        Integer[] tamanhos = {10, 20, 50, 100};
        comboPageSize = new JComboBox<>(tamanhos);
        comboPageSize.setSelectedItem(controller.getPageSize());
        comboPageSize.addActionListener(e -> alterarTamanhoPagina());
        
        // Painel para botões de páginas numeradas
        painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
        painelBotoes.setBackground(COR_FUNDO);
        
        // Configura ações dos botões
        btnFirst.addActionListener(e -> {
            controller.firstPage();
            notificarMudancaPagina();
        });
        
        btnPrevious.addActionListener(e -> {
            controller.previousPage();
            notificarMudancaPagina();
        });
        
        btnNext.addActionListener(e -> {
            controller.nextPage();
            notificarMudancaPagina();
        });
        
        btnLast.addActionListener(e -> {
            controller.lastPage();
            notificarMudancaPagina();
        });
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout());
        
        // Painel esquerdo - informações
        JPanel painelEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelEsquerdo.setBackground(COR_FUNDO);
        painelEsquerdo.add(labelInfo);
        
        // Painel central - navegação
        JPanel painelCentral = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelCentral.setBackground(COR_FUNDO);
        
        painelCentral.add(btnFirst);
        painelCentral.add(btnPrevious);
        painelCentral.add(painelBotoes);
        painelCentral.add(btnNext);
        painelCentral.add(btnLast);
        
        // Painel direito - controles
        JPanel painelDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelDireito.setBackground(COR_FUNDO);
        
        JLabel labelIr = new JLabel("Ir para:");
        labelIr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelIr.setForeground(COR_TEXTO);
        
        JLabel labelTamanho = new JLabel("Itens:");
        labelTamanho.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelTamanho.setForeground(COR_TEXTO);
        
        painelDireito.add(labelIr);
        painelDireito.add(fieldGoToPage);
        painelDireito.add(Box.createHorizontalStrut(10));
        painelDireito.add(labelTamanho);
        painelDireito.add(comboPageSize);
        
        add(painelEsquerdo, BorderLayout.WEST);
        add(painelCentral, BorderLayout.CENTER);
        add(painelDireito, BorderLayout.EAST);
    }
    
    private JButton criarBotaoNavegacao(String texto, String tooltip) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 12));
        botao.setBackground(COR_PRIMARIA);
        botao.setForeground(Color.WHITE);
        botao.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setToolTipText(tooltip);
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (botao.isEnabled()) {
                    botao.setBackground(COR_PRIMARIA.darker());
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (botao.isEnabled()) {
                    botao.setBackground(COR_PRIMARIA);
                }
            }
        });
        
        return botao;
    }
    
    private JButton criarBotaoPagina(int numeroPagina, boolean isCurrentPage) {
        JButton botao = new JButton(String.valueOf(numeroPagina));
        botao.setFont(new Font("Segoe UI", Font.BOLD, 11));
        botao.setPreferredSize(new Dimension(35, 28));
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isCurrentPage) {
            botao.setBackground(COR_PRIMARIA);
            botao.setForeground(Color.WHITE);
            botao.setBorder(BorderFactory.createLineBorder(COR_PRIMARIA.darker(), 2));
        } else {
            botao.setBackground(Color.WHITE);
            botao.setForeground(COR_TEXTO);
            botao.setBorder(BorderFactory.createLineBorder(COR_BORDA));
            
            // Efeito hover para páginas não atuais
            botao.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    botao.setBackground(COR_SECUNDARIA.brighter());
                }
                
                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    botao.setBackground(Color.WHITE);
                }
            });
        }
        
        botao.addActionListener(e -> {
            controller.setCurrentPage(numeroPagina);
            notificarMudancaPagina();
        });
        
        return botao;
    }
    
    public void atualizarInterface() {
        // Atualiza informações
        labelInfo.setText(controller.getSummary());
        fieldGoToPage.setText(String.valueOf(controller.getCurrentPage()));
        
        // Atualiza estado dos botões de navegação
        btnFirst.setEnabled(controller.hasPreviousPage());
        btnPrevious.setEnabled(controller.hasPreviousPage());
        btnNext.setEnabled(controller.hasNextPage());
        btnLast.setEnabled(controller.hasNextPage());
        
        // Atualiza cor dos botões desabilitados
        Color corDesabilitado = new Color(149, 165, 166);
        if (!controller.hasPreviousPage()) {
            btnFirst.setBackground(corDesabilitado);
            btnPrevious.setBackground(corDesabilitado);
        } else {
            btnFirst.setBackground(COR_PRIMARIA);
            btnPrevious.setBackground(COR_PRIMARIA);
        }
        
        if (!controller.hasNextPage()) {
            btnNext.setBackground(corDesabilitado);
            btnLast.setBackground(corDesabilitado);
        } else {
            btnNext.setBackground(COR_PRIMARIA);
            btnLast.setBackground(COR_PRIMARIA);
        }
        
        // Reconstrói botões de páginas numeradas
        atualizarBotoesPaginas();
        
        repaint();
    }
    
    private void atualizarBotoesPaginas() {
        painelBotoes.removeAll();
        
        List<Integer> paginasVisiveis = controller.getVisiblePages();
        
        // Adiciona "..." no início se necessário
        if (controller.shouldShowStartEllipsis()) {
            JLabel ellipsis = new JLabel("...");
            ellipsis.setFont(new Font("Segoe UI", Font.BOLD, 12));
            ellipsis.setForeground(COR_TEXTO);
            painelBotoes.add(ellipsis);
        }
        
        // Adiciona botões das páginas visíveis
        for (Integer pagina : paginasVisiveis) {
            boolean isCurrentPage = pagina == controller.getCurrentPage();
            painelBotoes.add(criarBotaoPagina(pagina, isCurrentPage));
        }
        
        // Adiciona "..." no final se necessário
        if (controller.shouldShowEndEllipsis()) {
            JLabel ellipsis = new JLabel("...");
            ellipsis.setFont(new Font("Segoe UI", Font.BOLD, 12));
            ellipsis.setForeground(COR_TEXTO);
            painelBotoes.add(ellipsis);
        }
        
        painelBotoes.revalidate();
    }
    
    private void irParaPagina() {
        try {
            int pagina = Integer.parseInt(fieldGoToPage.getText());
            if (pagina >= 1 && pagina <= controller.getTotalPages()) {
                controller.setCurrentPage(pagina);
                notificarMudancaPagina();
            } else {
                fieldGoToPage.setText(String.valueOf(controller.getCurrentPage()));
                JOptionPane.showMessageDialog(this, 
                    "Página deve estar entre 1 e " + controller.getTotalPages(), 
                    "Página inválida", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            fieldGoToPage.setText(String.valueOf(controller.getCurrentPage()));
            JOptionPane.showMessageDialog(this, 
                "Digite um número válido", 
                "Entrada inválida", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void alterarTamanhoPagina() {
        Integer novoTamanho = (Integer) comboPageSize.getSelectedItem();
        if (novoTamanho != null && novoTamanho != controller.getPageSize()) {
            controller.setPageSize(novoTamanho);
            controller.firstPage(); // Volta para a primeira página
            notificarMudancaPagina();
        }
    }
    
    private void notificarMudancaPagina() {
        atualizarInterface();
        if (pageChangeListener != null) {
            pageChangeListener.actionPerformed(null);
        }
    }
    
    public PaginationController getController() {
        return controller;
    }
}

