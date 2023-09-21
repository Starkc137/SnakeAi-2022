
public class Snake {
    Point head;
    Point tail;
    int bfsDistanceToApple = Integer.MAX_VALUE;
    int length;

    public int getBfsDistanceToApple() {
        return this.bfsDistanceToApple;
    }

    public void setBfsDistanceToApple(int bfsDistanceToApple) {
        this.bfsDistanceToApple = bfsDistanceToApple;
    }

    Snake(Point head, Point tail, int length) {
        this.head = head;
        this.tail = tail;
        this.length = length;
    }

    Snake() {
    }
}
