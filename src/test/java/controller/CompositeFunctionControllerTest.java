package controller;

import dto.*;
import service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompositeFunctionControllerTest {

    @Mock
    private CompositeFunctionService compositeFunctionService;

    @InjectMocks
    private CompositeFunctionController controller;

    private CompositeFunctionResponse testResponse;

    @BeforeEach
    void setUp() {
        testResponse = new CompositeFunctionResponse();
        testResponse.setCompositeId(1L);
        testResponse.setUserId(1L);
        testResponse.setCompositeName("f(g(x))");
        testResponse.setFirstFunctionId(1L);
        testResponse.setSecondFunctionId(2L);
        testResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateCompositeFunctionSuccess() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setUserId(1L);
        request.setCompositeName("composite");
        request.setFirstFunctionId(1L);
        request.setSecondFunctionId(2L);

        when(compositeFunctionService.createCompositeFunction(any(CompositeFunctionRequest.class)))
            .thenReturn(testResponse);

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.createCompositeFunction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
    }

    @Test
    void testCreateCompositeFunctionError() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();

        when(compositeFunctionService.createCompositeFunction(any(CompositeFunctionRequest.class)))
            .thenThrow(new RuntimeException("User not found"));

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.createCompositeFunction(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetCompositeFunctionByIdSuccess() {
        when(compositeFunctionService.getCompositeFunctionById(1L)).thenReturn(testResponse);

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.getCompositeFunctionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testGetCompositeFunctionByIdNotFound() {
        when(compositeFunctionService.getCompositeFunctionById(999L))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.getCompositeFunctionById(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testUpdateCompositeFunctionSuccess() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();
        request.setCompositeName("updated");

        when(compositeFunctionService.updateCompositeFunction(eq(1L), any(CompositeFunctionRequest.class)))
            .thenReturn(testResponse);

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.updateCompositeFunction(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testUpdateCompositeFunctionNotFound() {
        CompositeFunctionRequest request = new CompositeFunctionRequest();

        when(compositeFunctionService.updateCompositeFunction(eq(999L), any(CompositeFunctionRequest.class)))
            .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<ApiResponse<CompositeFunctionResponse>> response = 
            controller.updateCompositeFunction(999L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteCompositeFunctionSuccess() {
        doNothing().when(compositeFunctionService).deleteCompositeFunction(1L);

        ResponseEntity<ApiResponse<Void>> response = controller.deleteCompositeFunction(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
    }

    @Test
    void testDeleteCompositeFunctionNotFound() {
        doThrow(new RuntimeException("Not found")).when(compositeFunctionService)
            .deleteCompositeFunction(999L);

        ResponseEntity<ApiResponse<Void>> response = controller.deleteCompositeFunction(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

