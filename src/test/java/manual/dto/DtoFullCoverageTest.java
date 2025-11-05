package manual.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DtoFullCoverageTest {

    @Test
    void testCreatePointRequestAllMethods() {
        CreatePointRequest dto = new CreatePointRequest();
        assertNull(dto.getFunctionId());
        assertNull(dto.getXValue());
        assertNull(dto.getYValue());

        dto.setFunctionId(1L);
        dto.setXValue(2.0);
        dto.setYValue(3.0);

        assertEquals(1L, dto.getFunctionId());
        assertEquals(2.0, dto.getXValue());
        assertEquals(3.0, dto.getYValue());

        CreatePointRequest dto2 = new CreatePointRequest(4L, 5.0, 6.0);
        assertEquals(4L, dto2.getFunctionId());
        assertEquals(5.0, dto2.getXValue());
        assertEquals(6.0, dto2.getYValue());
    }

    @Test
    void testCreateCompositeFunctionRequestAllMethods() {
        CreateCompositeFunctionRequest dto = new CreateCompositeFunctionRequest();
        assertNull(dto.getUserId());
        assertNull(dto.getCompositeName());
        assertNull(dto.getFirstFunctionId());
        assertNull(dto.getSecondFunctionId());

        dto.setUserId(1L);
        dto.setCompositeName("composite");
        dto.setFirstFunctionId(2L);
        dto.setSecondFunctionId(3L);

        assertEquals(1L, dto.getUserId());
        assertEquals("composite", dto.getCompositeName());
        assertEquals(2L, dto.getFirstFunctionId());
        assertEquals(3L, dto.getSecondFunctionId());

        CreateCompositeFunctionRequest dto2 = new CreateCompositeFunctionRequest(4L, "comp2", 5L, 6L);
        assertEquals(4L, dto2.getUserId());
        assertEquals("comp2", dto2.getCompositeName());
        assertEquals(5L, dto2.getFirstFunctionId());
        assertEquals(6L, dto2.getSecondFunctionId());
    }

    @Test
    void testPointResponseAllMethods() {
        PointResponse dto = new PointResponse();
        assertNull(dto.getPointId());
        assertNull(dto.getFunctionId());
        assertNull(dto.getXValue());
        assertNull(dto.getYValue());
        assertNull(dto.getComputedAt());

        LocalDateTime now = LocalDateTime.now();
        dto.setPointId(1L);
        dto.setFunctionId(2L);
        dto.setXValue(3.0);
        dto.setYValue(4.0);
        dto.setComputedAt(now);

        assertEquals(1L, dto.getPointId());
        assertEquals(2L, dto.getFunctionId());
        assertEquals(3.0, dto.getXValue());
        assertEquals(4.0, dto.getYValue());
        assertEquals(now, dto.getComputedAt());

        PointResponse dto2 = new PointResponse(5L, 6L, 7.0, 8.0, now);
        assertEquals(5L, dto2.getPointId());
        assertEquals(6L, dto2.getFunctionId());
        assertEquals(7.0, dto2.getXValue());
        assertEquals(8.0, dto2.getYValue());
        assertEquals(now, dto2.getComputedAt());
    }

    @Test
    void testCompositeFunctionResponseAllMethods() {
        CompositeFunctionResponse dto = new CompositeFunctionResponse();
        assertNull(dto.getCompositeId());
        assertNull(dto.getUserId());
        assertNull(dto.getCompositeName());
        assertNull(dto.getFirstFunctionId());
        assertNull(dto.getSecondFunctionId());
        assertNull(dto.getCreatedAt());

        LocalDateTime now = LocalDateTime.now();
        dto.setCompositeId(1L);
        dto.setUserId(2L);
        dto.setCompositeName("composite");
        dto.setFirstFunctionId(3L);
        dto.setSecondFunctionId(4L);
        dto.setCreatedAt(now);

        assertEquals(1L, dto.getCompositeId());
        assertEquals(2L, dto.getUserId());
        assertEquals("composite", dto.getCompositeName());
        assertEquals(3L, dto.getFirstFunctionId());
        assertEquals(4L, dto.getSecondFunctionId());
        assertEquals(now, dto.getCreatedAt());

        CompositeFunctionResponse dto2 = new CompositeFunctionResponse(5L, 6L, "comp2", 7L, 8L, now);
        assertEquals(5L, dto2.getCompositeId());
        assertEquals(6L, dto2.getUserId());
        assertEquals("comp2", dto2.getCompositeName());
        assertEquals(7L, dto2.getFirstFunctionId());
        assertEquals(8L, dto2.getSecondFunctionId());
        assertEquals(now, dto2.getCreatedAt());
    }
}