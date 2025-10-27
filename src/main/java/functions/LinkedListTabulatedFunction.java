package functions;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;
import java.util.logging.Logger;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    private static final Logger logger = Logger.getLogger(LinkedListTabulatedFunction.class.getName());
    private static final long serialVersionUID = -938374055443160808L;

    private static class Node implements Serializable {
        public Node next;
        public Node prev;
        public double x;
        public double y;
    }

    private Node head;
    private int count;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        logger.info("Создание LinkedListTabulatedFunction из массивов");

        if (xValues.length < 2) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Length is less than minimum");
        }
        AbstractTabulatedFunction.checkLengthIsTheSame(xValues, yValues);
        AbstractTabulatedFunction.checkSorted(xValues);
        for (int i = 0; i < xValues.length; i++) {
            addNode(xValues[i], yValues[i]);
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        logger.info("Создание LinkedListTabulatedFunction из математической функции");

        if (count < 2) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Length is less than minimum");
        }
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        if (xFrom == xTo) {
            double y = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                addNode(xFrom, y);
            }
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                double x = xFrom + i * step;
                addNode(x, source.apply(x));
            }
        }
    }

    public double getX(int index) {
        if (index < 0 || index >= count) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Index out of bounds");
        }
        return getNode(index).x;
    }

    public double getY(int index) {
        if (index < 0 || index >= count) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Index out of bounds");
        }
        return getNode(index).y;
    }

    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Index out of bounds");
        }
        getNode(index).y = value;
    }

    protected int floorIndexOfX(double x) {
        if (x < head.x) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("X is less than left bound");
        }
        if (x > head.prev.x) return count;

        Node current = head;
        for (int i = 0; i < count - 1; i++) {
            if (x < current.next.x) return i;
            current = current.next;
        }
        return count - 1;
    }

    protected Node floorNodeOfX(double x) {
        if (x < head.x) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("X is less than left bound");
        }
        Node current = head;
        while (x >= current.next.x) {
            current = current.next;
        }
        return current;
    }

    public void remove(int index) {
        logger.fine("Удаление точки из LinkedListTabulatedFunction по индексу: " + index);

        if (index < 0 || index >= count) {
            logger.severe("Ошибка в LinkedListTabulatedFunction: неверный индекс");
            throw new IllegalArgumentException("Index out of bounds");
        }
        if (count == 1) {
            head = null;
            count = 0;
            return;
        }

        Node nodeToRemove = getNode(index);

        if (nodeToRemove == head) {
            head = head.next;
        }

        nodeToRemove.prev.next = nodeToRemove.next;
        nodeToRemove.next.prev = nodeToRemove.prev;
        count--;
    }

    private void addNode(double x, double y) {
        Node newNode = new Node();
        newNode.x = x;
        newNode.y = y;

        if (head == null) {
            head = newNode;
            newNode.next = newNode;
            newNode.prev = newNode;
        } else {
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        }
        count++;
    }

    public void insert(double x, double y) {
        logger.fine("Вставка точки в LinkedListTabulatedFunction: x=" + x + ", y=" + y);

        if (head == null) {
            addNode(x, y);
            return;
        }

        Node current = head;
        for (int i = 0; i < count; i++) {
            if (current.x == x) {
                current.y = y;
                return;
            }
            current = current.next;
        }

        Node newNode = new Node();
        newNode.x = x;
        newNode.y = y;

        if (x < head.x) {
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode;
        } else {
            current = head;
            while (x > current.next.x && current.next != head) {
                current = current.next;
            }
            newNode.next = current.next;
            newNode.prev = current;
            current.next.prev = newNode;
            current.next = newNode;
        }
        count++;
    }

    private Node getNode(int index) {
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    public int getCount() {
        return count;
    }

    public double leftBound() {
        return head.x;
    }

    public double rightBound() {
        return head.prev.x;
    }

    public int indexOfX(double x) {
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (current.x == x) return i;
            current = current.next;
        }
        return -1;
    }

    public int indexOfY(double y) {
        Node current = head;
        for (int i = 0; i < count; i++) {
            if (current.y == y) return i;
            current = current.next;
        }
        return -1;
    }

    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            Node current = head;
            for (int i = 0; i < count; i++) {
                if (current.x == x) return current.y;
                current = current.next;
            }

            Node floorNode = floorNodeOfX(x);
            return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
        }
    }

    protected double extrapolateLeft(double x) {
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    protected double extrapolateRight(double x) {
        Node last = head.prev;
        Node prevLast = last.prev;
        return interpolate(x, prevLast.x, last.x, prevLast.y, last.y);
    }

    protected double interpolate(double x, int floorIndex) {
        Node node = getNode(floorIndex);
        return interpolate(x, node.x, node.next.x, node.y, node.next.y);
    }

    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            private Node current = head;
            private int returnedCount = 0;

            public boolean hasNext() {
                return returnedCount < count;
            }

            public Point next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Point point = new Point(current.x, current.y);
                current = current.next;
                returnedCount++;
                return point;
            }
        };
    }
}