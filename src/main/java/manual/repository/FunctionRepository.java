package manual.repository;

import manual.SqlFileReader;
import manual.entity.FunctionEntity;
import manual.entity.UserEntity;
import manual.dto.CreateFunctionRequest;
import manual.dto.FunctionResponse;
import manual.mapper.FunctionMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FunctionRepository {
    private Connection connection;
    private final String CREATE_SQL;
    private final String READ_SQL;
    private final String UPDATE_SQL;
    private final String DELETE_SQL;
    private final String READ_BY_USER_SQL;
    private final String READ_ALL_SQL;
    private final String READ_ALL_ORDER_NAME_ASC;
    private final String READ_ALL_ORDER_NAME_DESC;
    private final String READ_ALL_ORDER_XFROM_ASC;
    private final String READ_ALL_ORDER_TYPE_NAME;

    public FunctionRepository(Connection connection) {
        this.connection = connection;
        this.CREATE_SQL = SqlFileReader.readSqlFile("FunctionCreate");
        this.READ_SQL = SqlFileReader.readSqlFile("FunctionRead");
        this.UPDATE_SQL = SqlFileReader.readSqlFile("FunctionUpdate");
        this.DELETE_SQL = SqlFileReader.readSqlFile("FunctionDelete");
        this.READ_BY_USER_SQL = SqlFileReader.readSqlFile("FunctionReadByUserId");
        this.READ_ALL_SQL = "SELECT * FROM functions";
        this.READ_ALL_ORDER_NAME_ASC = "SELECT * FROM functions ORDER BY function_name ASC";
        this.READ_ALL_ORDER_NAME_DESC = "SELECT * FROM functions ORDER BY function_name DESC";
        this.READ_ALL_ORDER_XFROM_ASC = "SELECT * FROM functions ORDER BY x_from ASC";
        this.READ_ALL_ORDER_TYPE_NAME = "SELECT * FROM functions ORDER BY function_type, function_name";
    }

    public List<FunctionResponse> findAllOrderByNameAsc() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_ORDER_NAME_ASC)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public List<FunctionResponse> findAllOrderByNameDesc() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_ORDER_NAME_DESC)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public List<FunctionResponse> findAllOrderByXFromAsc() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_ORDER_XFROM_ASC)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public List<FunctionResponse> findAllOrderByTypeAndName() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_ORDER_TYPE_NAME)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public FunctionResponse create(CreateFunctionRequest request, UserEntity user) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(CREATE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, user.getUserId());
            stmt.setString(2, request.getFunctionName());
            stmt.setString(3, request.getFunctionType());
            stmt.setString(4, request.getFunctionExpression());
            stmt.setDouble(5, request.getXFrom());
            stmt.setDouble(6, request.getXTo());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                Long functionId = keys.getLong(1);
                FunctionEntity entity = findEntityById(functionId);
                return FunctionMapper.toResponse(entity);
            }
            throw new SQLException();
        }
    }

    public FunctionResponse findById(Long functionId) throws SQLException {
        FunctionEntity entity = findEntityById(functionId);
        return entity != null ? FunctionMapper.toResponse(entity) : null;
    }

    private FunctionEntity findEntityById(Long functionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_SQL)) {
            stmt.setLong(1, functionId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapToEntity(rs) : null;
        }
    }

    public List<FunctionResponse> findByUserId(Long userId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_BY_USER_SQL)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public List<FunctionResponse> findAll() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(READ_ALL_SQL)) {
            ResultSet rs = stmt.executeQuery();
            List<FunctionResponse> functions = new ArrayList<>();
            while (rs.next()) {
                FunctionEntity entity = mapToEntity(rs);
                functions.add(FunctionMapper.toResponse(entity));
            }
            return functions;
        }
    }

    public FunctionResponse update(Long functionId, CreateFunctionRequest request) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, request.getFunctionName());
            stmt.setString(2, request.getFunctionType());
            stmt.setString(3, request.getFunctionExpression());
            stmt.setDouble(4, request.getXFrom());
            stmt.setDouble(5, request.getXTo());
            stmt.setLong(6, functionId);

            boolean result = stmt.executeUpdate() > 0;

            if (result) {
                FunctionEntity entity = findEntityById(functionId);
                return FunctionMapper.toResponse(entity);
            }
            return null;
        }
    }

    public boolean delete(Long functionId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, functionId);
            boolean result = stmt.executeUpdate() > 0;
            return result;
        }
    }

    private FunctionEntity mapToEntity(ResultSet rs) throws SQLException {
        FunctionEntity entity = new FunctionEntity();
        entity.setFunctionId(rs.getLong("function_id"));
        entity.setFunctionName(rs.getString("function_name"));
        entity.setFunctionType(rs.getString("function_type"));
        entity.setFunctionExpression(rs.getString("function_expression"));
        entity.setXFrom(rs.getDouble("x_from"));
        entity.setXTo(rs.getDouble("x_to"));
        entity.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        UserEntity user = new UserEntity();
        user.setUserId(rs.getLong("user_id"));
        entity.setUser(user);

        return entity;
    }
}