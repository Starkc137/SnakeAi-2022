

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import za.ac.wits.snake.DevelopmentAgent;

public class MyAgent extends DevelopmentAgent {
    private static char[][] board;
    private static int width;
    private static int height;
    private final Random random = new Random();
    Snake mySnake = new Snake((Point)null, (Point)null, 0);
    ArrayList<Snake> opponents = new ArrayList();
    Point apple = new Point();
    private static final ArrayList<Zombie> zombieHeads = new ArrayList();
    private static final ArrayList<ArrayList<Point>> snakes = new ArrayList();
    private static final int[] row = new int[]{-1, 0, 0, 1};
    private static final int[] col = new int[]{0, -1, 1, 0};

    public MyAgent() {
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            try {
                String initString = br.readLine();
                String[] temp = initString.split(" ");
                int nSnakes = Integer.parseInt(temp[0]);
                width = Integer.parseInt(temp[1]);
                height = Integer.parseInt(temp[2]);
                board = new char[width][height];
                double appleValue = 5.0;
                Point oldApple = new Point();

                label137:
                while(true) {
                    while(true) {
                        String line = br.readLine();
                        if (line != null) {
                            if (line.contains("Game Over")) {
                                break label137;
                            }

                            this.apple = this.readApple(line);

                            int mySnakeNum;
                            for(mySnakeNum = 0; mySnakeNum < 6; ++mySnakeNum) {
                                String zombieLine = br.readLine();
                                this.readZombies(zombieLine);
                            }

                            mySnakeNum = Integer.parseInt(br.readLine());

                            int i;
                            for(i = 0; i < nSnakes; ++i) {
                                String strSnake = br.readLine();
                                this.readSnake(strSnake, mySnakeNum, i);
                            }

                            for(i = 0; i < snakes.size(); ++i) {
                                drawSnake(i + 1, (ArrayList)snakes.get(i));
                            }

                            Iterator var18 = zombieHeads.iterator();

                            while(var18.hasNext()) {
                                Zombie z = (Zombie)var18.next();
                                if (z.getNearestSnakeNum() == mySnakeNum) {
                                }
                            }

                            Cell cell;
                            Point next;
                            if (isAppleWorthIt(Math.ceil(appleValue)) && !isAppleTrapped(this.apple)) {
                                cell = findPath(this.mySnake.head, this.apple);
                                if (cell == null) {
                                    next = this.getMaxSpaceMove(this.mySnake);
                                    System.out.println(this.makeMove(this.mySnake.head, next));
                                } else {
                                    this.mySnake.setBfsDistanceToApple(cell.getDistance());
                                    var18 = this.opponents.iterator();

                                    while(var18.hasNext()) {
                                        Snake opponent = (Snake)var18.next();
                                        Cell opponentCell;
                                        if (opponent.getClass() == Snake.class) {
                                            opponentCell = findPath(opponent.head, this.apple);
                                            if (opponentCell != null) {
                                                opponent.setBfsDistanceToApple(opponentCell.getDistance());
                                            }
                                        } else {
                                            opponentCell = findPath(opponent.head, this.mySnake.head);
                                            if (opponentCell != null) {
                                                board[opponentCell.point.y][opponentCell.point.x] = '\b';
                                            }
                                        }
                                    }

                                    boolean isSafer = this.isSafer(this.mySnake, this.opponents);
                                    if (isSafer) {
                                        System.out.println(this.makeMove(this.mySnake.head, this.getNextPoint(getPath(cell))));
                                    } else {
                                        cell = findPath(this.mySnake.head, this.randomPoint(this.apple));
                                        if (cell != null) {
                                            next = this.getNextPoint(getPath(cell));
                                            if (this.canCauseHeadCollision(next, this.opponents, zombieHeads)) {
                                                next = this.avoidHeadCollisionMove(this.mySnake, next, this.opponents, zombieHeads);
                                            }

                                            int spaceAvailableAtNext = calculateSpace(next);
                                            if (spaceAvailableAtNext < this.mySnake.length) {
                                                next = this.getMaxSpaceMove(this.mySnake);
                                            }
                                        } else {
                                            next = this.getMaxSpaceMove(this.mySnake);
                                        }

                                        System.out.println(this.makeMove(this.mySnake.head, next));
                                    }
                                }
                            } else {
                                board[this.apple.y][this.apple.x] = '\b';
                                cell = findPath(this.mySnake.head, this.randomPoint(this.apple));
                                if (cell != null) {
                                    next = this.getNextPoint(getPath(cell));
                                    if (this.canCauseHeadCollision(next, this.opponents, zombieHeads)) {
                                        next = this.avoidHeadCollisionMove(this.mySnake, next, this.opponents, zombieHeads);
                                    }

                                    int spaceAvailableAtNext = calculateSpace(next);
                                    if (spaceAvailableAtNext < this.mySnake.length) {
                                        next = this.getMaxSpaceMove(this.mySnake);
                                    }
                                } else {
                                    next = this.getMaxSpaceMove(this.mySnake);
                                }

                                System.out.println(this.makeMove(this.mySnake.head, next));
                            }

                            snakes.clear();
                            this.opponents.clear();
                            board = new char[width][height];
                            if (isAppleRespawned(this.apple, oldApple)) {
                                appleValue = 5.0;
                            }

                            oldApple = this.apple;
                        } else {
                            System.out.println("log Line is null");
                        }
                    }
                }
            } catch (Throwable var15) {
                try {
                    br.close();
                } catch (Throwable var14) {
                    var15.addSuppressed(var14);
                }

                throw var15;
            }

            br.close();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        MyAgent agent = new MyAgent();
        start(agent, args);
    }

    private void readZombies(String str) {
        String[] arrZombies = str.split(" ");
        String[] tempHead = arrZombies[0].split(",");
        Point ZHead = new Point(Integer.parseInt(tempHead[0]), Integer.parseInt(tempHead[1]));
        String[] tempTail = arrZombies[arrZombies.length - 1].split(",");
        Point ZTail = new Point(Integer.parseInt(tempTail[0]), Integer.parseInt(tempTail[1]));
        Zombie z1 = new Zombie(ZHead, ZTail);
        zombieHeads.add(z1);
        this.opponents.add(z1);
        String[] temp = arrZombies[0].split(",");

        for(int i = 0; i < arrZombies.length - 1; ++i) {
            String[] comp = arrZombies[i + 1].split(",");
            this.draw(temp, comp);
            temp = comp;
        }

    }

    private void draw(String[] temp, String[] comp) {
        int y2 = Integer.parseInt(temp[1]);
        int i;
        if (temp[0].equals(comp[0])) {
            int y1 = Math.min(Integer.parseInt(temp[1]), Integer.parseInt(comp[1]));
            y2 = Math.max(Integer.parseInt(temp[1]), Integer.parseInt(comp[1]));

            for(i = y1; i <= y2; ++i) {
                board[i][Integer.parseInt(temp[0])] = '\b';
            }
        } else {
            int x1 = Math.min(Integer.parseInt(temp[0]), Integer.parseInt(comp[0]));
            int x2 = Math.max(Integer.parseInt(comp[0]), Integer.parseInt(temp[0]));

            for(i = x1; i <= x2; ++i) {
                board[Integer.parseInt(temp[1])][i] = '\b';
            }
        }

    }

    private Point readApple(String line) {
        String[] arrApple = line.split(" ");
        int xApple = Integer.parseInt(arrApple[0]);
        int yApple = Integer.parseInt(arrApple[1]);
        board[yApple][xApple] = 7;
        return new Point(xApple, yApple);
    }

    private void readSnake(String strSnake, int mySnakeNum, int i) {
        String[] snakeInput = strSnake.split(" ");
        if (!snakeInput[0].equals("dead")) {
            ArrayList<Point> arrListSnake = new ArrayList();
            int snakeLength = Integer.parseInt(snakeInput[1]);
            String[] tempHead = snakeInput[3].split(",");
            Point Head = new Point(Integer.parseInt(tempHead[0]), Integer.parseInt(tempHead[1]));
            String[] tempTail = snakeInput[snakeInput.length - 1].split(",");
            Point Tail = new Point(Integer.parseInt(tempTail[0]), Integer.parseInt(tempTail[1]));
            Iterator var11 = zombieHeads.iterator();

            while(var11.hasNext()) {
                Zombie zombie = (Zombie)var11.next();
                if (this.manhattanDistance(zombie.head, Head) < zombie.getManhattanDistanceToHead()) {
                    zombie.setManhattanDistanceToHead(this.manhattanDistance(zombie.head, Head));
                    zombie.setNearestSnakeNum(i);
                }
            }

            if (mySnakeNum == i) {
                this.mySnake.head = Head;
                this.mySnake.tail = Tail;
                this.mySnake.length = snakeLength;
            } else {
                this.opponents.add(new Snake(Head, Tail, snakeLength));
            }

            for(int j = 3; j < snakeInput.length; ++j) {
                String[] indexBody = snakeInput[j].split(",");
                Point bend = new Point(Integer.parseInt(indexBody[0]), Integer.parseInt(indexBody[1]));
                board[bend.y][bend.x] = (char)i;
                arrListSnake.add(bend);
            }

            snakes.add(arrListSnake);
        }
    }

    private static void drawLine(int snakeNum, Point one, Point two) {
        int start;
        int end;
        int i;
        if (one.sameX(two)) {
            start = one.y;
            end = two.y;
            if (one.greaterThanByY(two)) {
                start = two.y;
                end = one.y;
            }

            for(i = start; i <= end; ++i) {
                board[i][one.x] = (char)snakeNum;
            }
        } else if (one.sameY(two)) {
            start = one.x;
            end = two.x;
            if (one.greaterThanByX(two)) {
                start = two.x;
                end = one.x;
            }

            for(i = start; i <= end; ++i) {
                board[one.y][i] = (char)snakeNum;
            }
        }

    }

    private static void drawSnake(int snakeNum, ArrayList<Point> snake) {
        if (snake.size() >= 2) {
            for(int i = 0; i < snake.size() - 1; ++i) {
                drawLine(snakeNum, (Point)snake.get(i), (Point)snake.get(i + 1));
            }
        }

    }

    private int manhattanDistance(Point one, Point two) {
        return Math.abs(one.x - two.x) + Math.abs(one.y - two.y);
    }

    private Point nextFurthestPoint(Point head, Point destination) {
        int furthestDistance = 0;
        Point furthestPoint = head;

        for(int i = 0; i < 4; ++i) {
            Point point = new Point(head.x + row[i], head.y + col[i]);
            int distance = this.manhattanDistance(point, destination);
            if (isFree(point) && distance > furthestDistance) {
                furthestPoint = point;
                furthestDistance = distance;
            }
        }

        return furthestPoint;
    }

    private static Cell findPath(Point s, Point d) {
        Queue<Cell> q = new ArrayDeque();
        Cell src = new Cell(s.x, s.y, (Cell)null);
        q.add(src);
        Set<String> visited = new HashSet();
        String key = s.x + "," + s.y;
        visited.add(key);

        while(!q.isEmpty()) {
            Cell curr = (Cell)q.poll();
            int i = curr.point.x;
            int j = curr.point.y;
            if (i == d.x && j == d.y) {
                return curr;
            }

            for(int k = 0; k < 4; ++k) {
                Point temp = new Point(i + row[k], j + col[k]);
                key = temp.x + "," + temp.y;
                if (!visited.contains(key) && isFree(temp)) {
                    Cell next = new Cell(temp.x, temp.y, curr);
                    q.add(next);
                    visited.add(key);
                }
            }
        }

        return null;
    }

    private static ArrayList<Point> getPath(Cell cell) {
        ArrayList path;
        for(path = new ArrayList(); cell != null; cell = cell.parent) {
            path.add(cell.point);
        }

        return path;
    }

    private static boolean isValid(Point p) {
        return p.y >= 0 && p.y < width && p.x >= 0 && p.x < height;
    }

    private static boolean isFree(Point p) {
        return isValid(p) && (board[p.y][p.x] == 0 || board[p.y][p.x] == 7);
    }

    private int makeMove(Point h, Point n) {
        if (h.x == n.x) {
            if (h.y > n.y) {
                return 0;
            }

            if (h.y < n.y) {
                return 1;
            }
        }

        if (h.y == n.y) {
            if (h.x > n.x) {
                return 2;
            }

            if (h.x < n.x) {
                return 3;
            }
        }

        System.out.println("log Straight Move");
        return 5;
    }

    private boolean isSafer(Snake mySnake, ArrayList<Snake> opponents) {
        Iterator var3 = opponents.iterator();

        while(var3.hasNext()) {
            Snake opponent = (Snake)var3.next();
            if (opponent.getClass() == Snake.class) {
                if (opponent.getBfsDistanceToApple() - mySnake.getBfsDistanceToApple() < 1) {
                    return false;
                }
            } else if (this.manhattanDistance(opponent.head, this.apple) < this.manhattanDistance(mySnake.head, this.apple)) {
                return false;
            }
        }

        return true;
    }

    private Point randomPoint(Point apple) {
        int tries = 50;
        new ArrayList();

        while(tries >= 0) {
            Point point = new Point(this.random.nextInt(width), this.random.nextInt(height));
            if (isFree(point)) {
                return point;
            }

            --tries;
        }

        return apple;
    }

    private static boolean isAppleRespawned(Point apple, Point oldApple) {
        return apple.x != oldApple.x || apple.y != oldApple.y;
    }

    private static boolean isAppleWorthIt(double appleValue) {
        return appleValue >= 1.0;
    }

    private static boolean isAppleTrapped(Point apple) {
        ArrayList<Point> neighbors = getNeighbors(apple);
        int numObstacles = 4;
        Iterator var3 = neighbors.iterator();

        while(var3.hasNext()) {
            Point neighbor = (Point)var3.next();
            if (isFree(neighbor)) {
                --numObstacles;
            }
        }

        return numObstacles >= 3;
    }

    private static int calculateSpace(Point point) {
        int space = 0;
        if (board[point.y][point.x] == 0) {
            board[point.y][point.x] = '\b';
        }

        Queue<Point> q = new ArrayDeque();
        q.add(point);
        Set<String> visited = new HashSet();
        visited.add(point.toString());

        while(!q.isEmpty()) {
            Point current = (Point)q.poll();
            ArrayList<Point> neighbors = getNeighbors(current);
            Iterator var6 = neighbors.iterator();

            while(var6.hasNext()) {
                Point neighbor = (Point)var6.next();
                if (!visited.contains(neighbor.toString()) && isFree(neighbor)) {
                    q.add(neighbor);
                    visited.add(neighbor.toString());
                    ++space;
                }
            }
        }

        board[point.y][point.x] = 0;
        return space;
    }

    private Point getMaxSpaceMove(Snake mySnake) {
        ArrayList<Point> neighbors = getNeighbors(mySnake.head);
        int bestSpace = 0;
        Point bestPoint = new Point();
        Iterator var5 = neighbors.iterator();

        while(var5.hasNext()) {
            Point neighbor = (Point)var5.next();
            if (isFree(neighbor)) {
                int space = calculateSpace(neighbor);
                if (space > bestSpace) {
                    bestPoint = neighbor;
                    bestSpace = space;
                }
            }
        }

        return bestPoint;
    }

    private Point getNextPoint(ArrayList<Point> path) {
        Point point = new Point();
        if (path.size() >= 2) {
            point = (Point)path.get(path.size() - 2);
        } else if (path.size() == 1) {
            point = (Point)path.get(0);
        }

        return point;
    }

    private static ArrayList<Point> getNeighbors(Point point) {
        ArrayList<Point> neighbors = new ArrayList();

        for(int i = 0; i < 4; ++i) {
            Point neighbor = new Point(point.x + row[i], point.y + col[i]);
            neighbors.add(neighbor);
        }

        return neighbors;
    }

    private boolean canCauseHeadCollision(Point point, ArrayList<Snake> opponents, ArrayList<Zombie> zombieHeads) {
        ArrayList<Point> neighbors = getNeighbors(point);
        Iterator var5 = neighbors.iterator();

        Point neighbor;
        do {
            if (!var5.hasNext()) {
                return false;
            }

            neighbor = (Point)var5.next();
            if (this.isSnakeHead(neighbor, opponents)) {
                return true;
            }
        } while(!this.isZombieHead(neighbor, zombieHeads));

        return true;
    }

    private boolean isSnakeHead(Point point, ArrayList<Snake> opponents) {
        Iterator var3 = opponents.iterator();

        Snake opponent;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            opponent = (Snake)var3.next();
        } while(!point.equals(opponent.head));

        return true;
    }

    private boolean isZombieHead(Point point, ArrayList<Zombie> zombieHeads) {
        Iterator var3 = this.opponents.iterator();

        Snake opponent;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            opponent = (Snake)var3.next();
        } while(!point.equals(opponent.head));

        return true;
    }

    private Point avoidHeadCollisionMove(Snake mySnake, Point defaultPoint, ArrayList<Snake> opponents, ArrayList<Zombie> zombieHeads) {
        ArrayList<Point> neighbors = getNeighbors(mySnake.head);
        Iterator var6 = neighbors.iterator();

        Point neighbor;
        do {
            if (!var6.hasNext()) {
                return defaultPoint;
            }

            neighbor = (Point)var6.next();
        } while(!isFree(neighbor) || this.canCauseHeadCollision(neighbor, opponents, zombieHeads));

        return neighbor;
    }
}
