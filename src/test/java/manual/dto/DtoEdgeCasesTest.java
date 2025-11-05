package manual.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DtoEdgeCasesTest {

    @Test
    void testCreateUserRequestEdgeCases() {
        CreateUserRequest dto = new CreateUserRequest();
        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
        assertNull(dto.getEmail());

        dto.setUsername("");
        dto.setPassword("");
        dto.setEmail("");
        assertEquals("", dto.getUsername());
        assertEquals("", dto.getPassword());
        assertEquals("", dto.getEmail());

        CreateUserRequest dto2 = new CreateUserRequest(null, null, null);
        assertNull(dto2.getUsername());
        assertNull(dto2.getPassword());
        assertNull(dto2.getEmail());
    }

    @Test
    void testFunctionResponseEdgeCases() {
        FunctionResponse dto = new FunctionResponse();
        assertNull(dto.getFunctionId());
        assertNull(dto.getUserId());
        assertNull(dto.getFunctionName());
        assertNull(dto.getFunctionType());
        assertNull(dto.getFunctionExpression());
        assertNull(dto.getXFrom());
        assertNull(dto.getXTo());
        assertNull(dto.getCreatedAt());

        dto.setXFrom(Double.MAX_VALUE);
        dto.setXTo(Double.MIN_VALUE);
        assertEquals(Double.MAX_VALUE, dto.getXFrom());
        assertEquals(Double.MIN_VALUE, dto.getXTo());
    }

    @Test
    void testPointRequestBoundaryValues() {
        CreatePointRequest dto = new CreatePointRequest(Long.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getFunctionId());
        assertEquals(Double.MAX_VALUE, dto.getXValue());
        assertEquals(Double.MIN_VALUE, dto.getYValue());

        CreatePointRequest dto2 = new CreatePointRequest(0L, 0.0, -0.0);
        assertEquals(0L, dto2.getFunctionId());
        assertEquals(0.0, dto2.getXValue());
        assertEquals(-0.0, dto2.getYValue());
    }

    @Test
    void testCompositeFunctionResponseNullFields() {
        CompositeFunctionResponse dto = new CompositeFunctionResponse();
        assertNull(dto.getCompositeId());
        assertNull(dto.getUserId());
        assertNull(dto.getCompositeName());
        assertNull(dto.getFirstFunctionId());
        assertNull(dto.getSecondFunctionId());
        assertNull(dto.getCreatedAt());

        dto.setCompositeName("   ");
        assertEquals("   ", dto.getCompositeName());
    }
}