package combo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador avançado de paginação com recursos extras
 */
public class PaginationController {
    private int currentPage;
    private int pageSize;
    private int totalRecords;
    private int totalPages;
    private int maxVisiblePages;
    
    public PaginationController(int pageSize, int maxVisiblePages) {
        this.pageSize = pageSize;
        this.maxVisiblePages = maxVisiblePages;
        this.currentPage = 1;
    }
    
    public void updateTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
        this.totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        
        // Ajusta a página atual se necessário
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
    }
    
    public void setCurrentPage(int page) {
        if (page >= 1 && page <= totalPages) {
            this.currentPage = page;
        }
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        updateTotalRecords(totalRecords); // Recalcula páginas
    }
    
    public int getOffset() {
        return (currentPage - 1) * pageSize;
    }
    
    public boolean hasNextPage() {
        return currentPage < totalPages;
    }
    
    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
    
    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
        }
    }
    
    public void previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
        }
    }
    
    public void firstPage() {
        currentPage = 1;
    }
    
    public void lastPage() {
        currentPage = totalPages;
    }
    
    /**
     * Gera lista de páginas visíveis para navegação
     * @return Lista de números de páginas para exibir
     */
    public List<Integer> getVisiblePages() {
        List<Integer> pages = new ArrayList<>();
        
        if (totalPages <= maxVisiblePages) {
            // Se o total de páginas é menor que o máximo visível, mostra todas
            for (int i = 1; i <= totalPages; i++) {
                pages.add(i);
            }
        } else {
            // Calcula o range de páginas visíveis
            int start = Math.max(1, currentPage - maxVisiblePages / 2);
            int end = Math.min(totalPages, start + maxVisiblePages - 1);
            
            // Ajusta o início se o fim ultrapassou o limite
            if (end - start + 1 < maxVisiblePages) {
                start = Math.max(1, end - maxVisiblePages + 1);
            }
            
            for (int i = start; i <= end; i++) {
                pages.add(i);
            }
        }
        
        return pages;
    }
    
    /**
     * Verifica se deve mostrar "..." antes das páginas visíveis
     */
    public boolean shouldShowStartEllipsis() {
        List<Integer> visiblePages = getVisiblePages();
        return !visiblePages.isEmpty() && visiblePages.get(0) > 1;
    }
    
    /**
     * Verifica se deve mostrar "..." depois das páginas visíveis
     */
    public boolean shouldShowEndEllipsis() {
        List<Integer> visiblePages = getVisiblePages();
        return !visiblePages.isEmpty() && visiblePages.get(visiblePages.size() - 1) < totalPages;
    }
    
    /**
     * Gera informações de resumo da paginação
     */
    public String getSummary() {
        if (totalRecords == 0) {
            return "Nenhum registro encontrado";
        }
        
        int start = getOffset() + 1;
        int end = Math.min(getOffset() + pageSize, totalRecords);
        
        return String.format("Exibindo %d-%d de %d registros (Página %d de %d)", 
                start, end, totalRecords, currentPage, totalPages);
    }
    
    /**
     * Gera informações resumidas para status bar
     */
    public String getShortSummary() {
        return String.format("Página %d de %d (%d registros)", 
                currentPage, totalPages, totalRecords);
    }
    
    // Getters
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public int getTotalRecords() { return totalRecords; }
    public int getTotalPages() { return totalPages; }
    public int getMaxVisiblePages() { return maxVisiblePages; }
    
    // Setters
    public void setMaxVisiblePages(int maxVisiblePages) {
        this.maxVisiblePages = maxVisiblePages;
    }
}

