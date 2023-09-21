//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Point() {
        this.x = -1;
        this.y = -1;
    }

    public String toString() {
        return this.x + "," + this.y;
    }

    public boolean greaterThanByX(Point other) {
        return this.x > other.x;
    }

    public boolean greaterThanByY(Point other) {
        return this.y > other.y;
    }

    public boolean sameX(Point other) {
        return this.x == other.x;
    }

    public boolean sameY(Point other) {
        return this.y == other.y;
    }

    public boolean equals(Point other) {
        return this.sameX(other) && this.sameY(other);
    }
}
