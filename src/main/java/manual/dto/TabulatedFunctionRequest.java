package manual.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

public class TabulatedFunctionRequest {
    private String type; // "ARRAY"/ "LINKED_LIST"
    
    @JsonProperty("xValues")
    private double[] xValues;
    
    @JsonProperty("yValues")
    private double[] yValues;
    
    @JsonProperty("xFrom")
    private Double xFrom;
    
    @JsonProperty("xTo")
    private Double xTo;
    
    private Integer pointsCount;
    private String mathFunctionType; // "SQR"/ "IDENTITY"/ "CONSTANT"/ "UNIT"/ "ZERO"
    private Double constantValue;

    public TabulatedFunctionRequest() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double[] getXValues() { return xValues; }
    public void setXValues(double[] xValues) { this.xValues = xValues; }

    public double[] getYValues() { return yValues; }
    public void setYValues(double[] yValues) { this.yValues = yValues; }

    public Double getXFrom() { return xFrom; }
    public void setXFrom(Double xFrom) { this.xFrom = xFrom; }

    public Double getXTo() { return xTo; }
    public void setXTo(Double xTo) { this.xTo = xTo; }

    public Integer getPointsCount() { return pointsCount; }
    public void setPointsCount(Integer pointsCount) { this.pointsCount = pointsCount; }

    public String getMathFunctionType() { return mathFunctionType; }
    public void setMathFunctionType(String mathFunctionType) { this.mathFunctionType = mathFunctionType; }

    public Double getConstantValue() { return constantValue; }
    public void setConstantValue(Double constantValue) { this.constantValue = constantValue; }

    @Override
    public String toString() {
        return "TabulatedFunctionRequest{" +
                "type='" + type + '\'' +
                ", xValues=" + Arrays.toString(xValues) +
                ", yValues=" + Arrays.toString(yValues) +
                ", xFrom=" + xFrom +
                ", xTo=" + xTo +
                ", pointsCount=" + pointsCount +
                ", mathFunctionType='" + mathFunctionType + '\'' +
                ", constantValue=" + constantValue +
                '}';
    }
}