package manual.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DtoValidationTest {

    @Test
    void testCreateUserRequest() {
        CreateUserRequest dto = new CreateUserRequest();
        assertNull(dto.getUsername());

        dto.setUsername("user");
        dto.setPassword("pass");
        dto.setEmail("email@test.com");

        assertEquals("user", dto.getUsername());
        assertEquals("pass", dto.getPassword());
        assertEquals("email@test.com", dto.getEmail());

        CreateUserRequest dto2 = new CreateUserRequest("user2", "pass2", "email2@test.com");
        assertEquals("user2", dto2.getUsername());
        assertEquals("pass2", dto2.getPassword());
        assertEquals("email2@test.com", dto2.getEmail());
    }

    @Test
    void testUserResponse() {
        UserResponse dto = new UserResponse();
        assertNull(dto.getUserId());

        dto.setUserId(1L);
        dto.setUsername("user");
        dto.setEmail("email@test.com");

        assertEquals(1L, dto.getUserId());
        assertEquals("user", dto.getUsername());
        assertEquals("email@test.com", dto.getEmail());
    }

    @Test
    void testCreateFunctionRequest() {
        CreateFunctionRequest dto = new CreateFunctionRequest();
        assertNull(dto.getUserId());

        dto.setUserId(1L);
        dto.setFunctionName("func");
        dto.setFunctionType("SQR");
        dto.setFunctionExpression("x*x");
        dto.setXFrom(0.0);
        dto.setXTo(10.0);

        assertEquals(1L, dto.getUserId());
        assertEquals("func", dto.getFunctionName());
        assertEquals("SQR", dto.getFunctionType());
        assertEquals("x*x", dto.getFunctionExpression());
        assertEquals(0.0, dto.getXFrom());
        assertEquals(10.0, dto.getXTo());
    }

    @Test
    void testFunctionResponse() {
        FunctionResponse dto = new FunctionResponse();
        assertNull(dto.getFunctionId());

        dto.setFunctionId(1L);
        dto.setUserId(1L);
        dto.setFunctionName("func");
        dto.setFunctionType("SQR");

        assertEquals(1L, dto.getFunctionId());
        assertEquals(1L, dto.getUserId());
        assertEquals("func", dto.getFunctionName());
        assertEquals("SQR", dto.getFunctionType());
    }
}