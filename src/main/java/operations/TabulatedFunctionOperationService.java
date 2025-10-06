package operations;

import functions.Point;
import functions.TabulatedFunction;
import java.util.Iterator;

public class TabulatedFunctionOperationService {

    public static Point[] asPoints(TabulatedFunction tabulatedFunction) {
        Point[] points = new Point[tabulatedFunction.getCount()];
        Iterator<Point> iterator = tabulatedFunction.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            points[i] = iterator.next();
            i++;
        }
        return points;
    }
}