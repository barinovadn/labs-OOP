package manual.search;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SearchCriteria {
    private static final Logger logger = Logger.getLogger(SearchCriteria.class.getName());

    private String functionName;
    private String functionType;
    private Double minX;
    private Double maxX;
    private List<String> sortFields = new ArrayList<>();
    private boolean sortAscending = true;
    private int limit = 100;

    public SearchCriteria() {
        logger.fine("Creating search criteria");
    }

    public SearchCriteria functionName(String functionName) {
        this.functionName = functionName;
        logger.fine("Function name filter: " + functionName);
        return this;
    }

    public SearchCriteria functionType(String functionType) {
        this.functionType = functionType;
        logger.fine("Function type filter: " + functionType);
        return this;
    }

    public SearchCriteria xRange(Double minX, Double maxX) {
        this.minX = minX;
        this.maxX = maxX;
        logger.fine("X range filter: " + minX + " to " + maxX);
        return this;
    }

    public SearchCriteria sortBy(String field, boolean ascending) {
        this.sortFields.add(field);
        this.sortAscending = ascending;
        logger.fine("Sort by: " + field + " (" + (ascending ? "ASC" : "DESC") + ")");
        return this;
    }

    public SearchCriteria limit(int limit) {
        this.limit = limit;
        logger.fine("Limit: " + limit);
        return this;
    }

    public String getFunctionName() { return functionName; }
    public String getFunctionType() { return functionType; }
    public Double getMinX() { return minX; }
    public Double getMaxX() { return maxX; }
    public List<String> getSortFields() { return sortFields; }
    public boolean isSortAscending() { return sortAscending; }
    public int getLimit() { return limit; }

    public boolean hasFunctionNameFilter() { return functionName != null && !functionName.isEmpty(); }
    public boolean hasFunctionTypeFilter() { return functionType != null && !functionType.isEmpty(); }
    public boolean hasXRangeFilter() { return minX != null || maxX != null; }
    public boolean hasSorting() { return !sortFields.isEmpty(); }
}