package manual.api;

public final class ApiPaths {
    
    private ApiPaths() { }
    
    public static final String API_BASE = "/api";
    
    public static final String USERS = API_BASE + "/users";
    public static final String USER_BY_ID = USERS + "/{id}";
    public static final String USER_FUNCTIONS = USERS + "/{userId}/functions";
    public static final String USER_COMPOSITE_FUNCTIONS = USERS + "/{userId}/composite-functions";
    
    public static final String FUNCTIONS = API_BASE + "/functions";
    public static final String FUNCTION_BY_ID = FUNCTIONS + "/{id}";
    public static final String FUNCTION_CALCULATE = FUNCTIONS + "/{functionId}/calculate";
    public static final String FUNCTION_DIFFERENTIATE = FUNCTIONS + "/{functionId}/differentiate";
    public static final String FUNCTION_POINTS = FUNCTIONS + "/{functionId}/points";
    public static final String FUNCTION_OPERATIONS = FUNCTIONS + "/operations";
    public static final String FUNCTION_SEARCH = FUNCTIONS + "/search";
    
    public static final String POINTS = API_BASE + "/points";
    public static final String POINT_BY_ID = POINTS + "/{id}";
    
    public static final String COMPOSITE_FUNCTIONS = API_BASE + "/composite-functions";
    public static final String COMPOSITE_FUNCTION_BY_ID = COMPOSITE_FUNCTIONS + "/{id}";
    
    public static final String PARAM_SORT = "sort";
    public static final String PARAM_SORT_NAME_ASC = "name_asc";
    public static final String PARAM_SORT_NAME_DESC = "name_desc";
    public static final String PARAM_SORT_X_FROM_ASC = "x_from_asc";
    public static final String PARAM_SORT_TYPE_NAME = "type_name";
    public static final String PARAM_USER_ID = "userId";
    public static final String PARAM_FUNCTION_ID = "functionId";
    public static final String PARAM_X = "x";
    public static final String PARAM_ALGORITHM = "algorithm";
}

