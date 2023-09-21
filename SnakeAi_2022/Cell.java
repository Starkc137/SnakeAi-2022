//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Cell {
    Point point;
    Cell parent;
    private int distance = Integer.MAX_VALUE;

    Cell(int x, int y, Cell parent) {
        this.point = new Point(x, y);
        this.parent = parent;
        if (parent != null) {
            this.distance = parent.distance + 1;
        }

    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Cell getParent() {
        return this.parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String toString() {
        return "(" + this.point.x + ", " + this.point.y + ")";
    }
}
