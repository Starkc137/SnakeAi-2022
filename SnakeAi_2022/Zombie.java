//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class Zombie extends Snake {
    private int manhattanDistanceToHead = Integer.MAX_VALUE;
    private int nearestSnakeNum = 0;

    public int getManhattanDistanceToHead() {
        return this.manhattanDistanceToHead;
    }

    public void setManhattanDistanceToHead(int manhattanDistanceToHead) {
        this.manhattanDistanceToHead = manhattanDistanceToHead;
    }

    public int getNearestSnakeNum() {
        return this.nearestSnakeNum;
    }

    public void setNearestSnakeNum(int nearestSnakeNum) {
        this.nearestSnakeNum = nearestSnakeNum;
    }

    Zombie(Point head, Point tail) {
        this.head = head;
        this.tail = tail;
    }
}
