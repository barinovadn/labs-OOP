package dto;

import java.util.Map;

public class SearchRequest {
    private String searchTerm;
    private Map<String, Object> criteria;
    private String sortField;
    private Boolean ascending;
    private Integer page;
    private Integer size;

    public SearchRequest() {}

    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }

    public Map<String, Object> getCriteria() { return criteria; }
    public void setCriteria(Map<String, Object> criteria) { this.criteria = criteria; }

    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }

    public Boolean getAscending() { return ascending; }
    public void setAscending(Boolean ascending) { this.ascending = ascending; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}

