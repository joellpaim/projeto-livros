package combo.util;

import java.util.List;

/**
 * Classe que encapsula o resultado de uma consulta paginada
 * @param <T> Tipo dos dados retornados
 */
public class PaginationResult<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private int totalRecords;
    private int totalPages;
    
    public PaginationResult(List<T> data, int currentPage, int pageSize, int totalRecords, int totalPages) {
        this.data = data;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
    }
    
    // Getters
    public List<T> getData() {
        return data;
    }
    
    public int getCurrentPage() {
        return currentPage;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public boolean hasNextPage() {
        return currentPage < totalPages;
    }
    
    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
    
    public int getNextPage() {
        return hasNextPage() ? currentPage + 1 : currentPage;
    }
    
    public int getPreviousPage() {
        return hasPreviousPage() ? currentPage - 1 : currentPage;
    }
    
    public int getStartRecord() {
        return (currentPage - 1) * pageSize + 1;
    }
    
    public int getEndRecord() {
        return Math.min(currentPage * pageSize, totalRecords);
    }
    
    @Override
    public String toString() {
        return String.format("Página %d de %d (%d-%d de %d registros)", 
                currentPage, totalPages, getStartRecord(), getEndRecord(), totalRecords);
    }
}

