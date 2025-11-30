package manual.servlet;

import manual.dto.CalculationRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "FunctionOperationServlet", urlPatterns = {"/api/functions/operations"})
public class FunctionOperationServlet extends BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        setRequestPath(request, response);
        try {
            CalculationRequest req = parseJsonRequest(request, CalculationRequest.class);
            Map<String, Object> result = new HashMap<>();
            result.put("operation", req.getOperation());
            result.put("status", "processed");
            sendSuccess(request, response, result);
        } catch (IOException e) {
            sendError(request, response, HttpServletResponse.SC_BAD_REQUEST, "Invalid");
        }
    }
}
