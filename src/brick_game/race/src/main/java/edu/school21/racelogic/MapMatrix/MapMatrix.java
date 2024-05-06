package edu.school21.racelogic.MapMatrix;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;
import edu.school21.racelogic.FiniteStateMachine.FiniteStateMachine;

import java.util.*;

public class MapMatrix {
    public enum Action
    {
        Start,
        Pause,
        Terminate,
        Left,
        Right,
        Up,
        Down,
        Action
    }
    private Random random;
    private static class Car {
        private int x;
        private int y;
        public Car(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    private int borderRound = -1;
    private int carsCount;
    private Queue<Car> carsList;
    private int lastCarCoordinate;
    private int playerCar;
    private final FiniteStateMachine stateMachine;
    private boolean[][] map;
    private final boolean[][] nextMap;
    private int speed;
    private int baseSpeed = 150;
    private int highSpeed = 75;
    private int lowSpeed = 300;
    private boolean isPaused = false;
    private boolean isNewScore = false;
    private boolean isSpeeding = false;
    private boolean isStalling = false;
    private long score;
    private long highScore = 0;
    private int level;

    public int getLevel() {
        return level;
    }

    public FiniteStateMachine.State getState() {
        return stateMachine.getCurrentState();
    }
    public void pause() {
        isPaused = !isPaused;
    }
    public boolean[][] getMap() {
        return map;
    }
    public boolean getPaused() {
        return isPaused;
    }

    public long getScore() {
        return score;
    }

    public long getHighScore() {
        return highScore;
    }
    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }

    public int getSpeed() {
        return speed;
    }
    public boolean isNewHighScore() {
        return isNewScore;
    }

    public MapMatrix() {
        map = new boolean[13][30];
        nextMap = new boolean[13][30];
        stateMachine = new FiniteStateMachine();
    }
    private void activateTile(int i, int j, boolean[][] map, boolean isFromPlayerCar) {
        if (isFromPlayerCar && map[i][j]) {
            stateMachine.collide();
        }
        map[i][j] = true;
    }
    private void drawBorders(int i, boolean[][] map) {
        if (i == 2) {
            activateTile(11, 0, map, false);
            activateTile(1, 0, map, false);
            activateTile(11, 29, map, false);
            activateTile(1, 29, map, false);
        }
        while(i < 29) {
            activateTile(1, i, map, false);
            activateTile(11, i, map, false);
            activateTile(1, i + 1, map, false);
            activateTile(11, i + 1, map, false);
            i += 3;
        }
    }

    private void redrawMap() {
        cleanMap(nextMap);
        drawBorders(borderRound, nextMap);
        if (carsCount > 0) {
            for (Car car : carsList) {
                drawCar(car);
            }
        }
        drawPlayerCar(nextMap);
        map = nextMap;
    }
    public void bootUp() {
        if (isPaused) {
            pause();
            return;
        } else if (getState() == FiniteStateMachine.State.PLAYING) {
            return;
        }
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 30; j++) {
                map[i][j] = false;
                nextMap[i][j] = false;
            }
        }
        random = new Random();
        carsList = new LinkedList<>();
        carsCount = 0;
        lastCarCoordinate = -5;
        score = 0;
        level = 1;
        baseSpeed = 150;
        highSpeed = 75;
        lowSpeed = 300;
        speed = baseSpeed;
        drawBorders(0, map);
        playerCar = 6;
        drawPlayerCar(map);
        stateMachine.reset();
        stateMachine.startRace();
    }

    public void renderMap() {
        System.out.flush();
        ColoredPrinter printer = new ColoredPrinter();
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 13; j++) {
                if (map[j][i]) {
                    printer.print(" ", Ansi.Attribute.NONE, Ansi.FColor.NONE, Ansi.BColor.BLUE);
                } else {
                    printer.print(" ", Ansi.Attribute.NONE, Ansi.FColor.NONE, Ansi.BColor.BLACK);
                }
            }
            if (i != 29) {
                System.out.println();
            }
        }
    }
    private int getCarLane() {
        return random.nextInt(3) + 1;
    }

    public void userInput(Action action) {
        if (isPaused && action != Action.Pause && action != Action.Terminate) {
            return;
        }
        switch (action) {
            case Up:
                speedUp();
                break;
            case Down:
                slowDown();
                break;
            case Left:
                if (playerCar > 3) {
                    playerCar--;
                    redrawMap();
                }
                break;
            case Right:
                if (playerCar < 9) {
                    playerCar++;
                    redrawMap();
                }
                break;
            case Pause:
                pause();
                break;
            case Start:
                bootUp();
                break;
            case Terminate:
                stateMachine.collide();
                break;
            case Action:
                break;
        }
    }

    private void drawPlayerCar(boolean[][] map) {
        activateTile(playerCar, 29, map, true);
        activateTile(playerCar, 28, map, true);
        activateTile(playerCar, 27, map, true);
        activateTile(playerCar, 26, map, true);
        activateTile(playerCar - 1, 29, map, true);
        activateTile(playerCar + 1, 29, map, true);
        activateTile(playerCar - 1, 27, map, true);
        activateTile(playerCar + 1, 27, map, true);
    }
    private void drawOrNot(int x, int y) {
        try {
            activateTile(x, y, nextMap, false);
        } catch (ArrayIndexOutOfBoundsException e) {}
    }

    private void drawCar(Car car) {
        drawOrNot(car.x, car.y + 4);
        drawOrNot(car.x + 1, car.y + 3);
        drawOrNot(car.x - 1, car.y + 3);
        drawOrNot(car.x, car.y + 3);
        drawOrNot(car.x, car.y + 2);
        drawOrNot(car.x, car.y + 1);
        drawOrNot(car.x + 1, car.y + 1);
        drawOrNot(car.x - 1, car.y + 1);
        drawOrNot(car.x, car.y);
    }
    public void updateState() {
        if (isPaused)
            return;
        boolean isNewCar = random.nextBoolean();
        if (carsCount < 3 && isNewCar && (carsCount == 0 || lastCarCoordinate > 0)) {
            carsList.offer(new Car(getCarLane() * 3, -5));
            lastCarCoordinate = -5;
            carsCount++;
        }
        moveDownByOne();
        map = nextMap;
    }

    private void moveDownByOne() {
        cleanMap(nextMap);
        borderRound++;
        borderRound %= 3;
        drawBorders(borderRound, nextMap);
        if (carsCount > 0) {
            lastCarCoordinate++;
            for (Car car : carsList) {
                car.y += 1;
            }
            if (carsList.peek().y == 30) {
                carsList.poll();
                carsCount--;
                score += 1;
                checkLevel();
            }
            for (Car car : carsList) {
                drawCar(car);
            }
        }
        drawPlayerCar(nextMap);
    }

    private void cleanMap(boolean[][] map) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 30; j++) {
                map[i][j] = false;
            }
        }
    }

    private void checkLevel() {
        if (score % 5 == 0 && score != 0) {
            levelUp();
        }
        if (score > highScore) {
            isNewScore = true;
            highScore = score;
        }
    }
    private void levelUp() {
        if (level < 10) {
            level++;
            adjustSpeed();
        }
    }

    private void speedUp() {
        if (isStalling) {
            speed = baseSpeed;
            isStalling = false;
        } else {
            speed = highSpeed;
            isSpeeding = true;
        }
    }

    private void slowDown() {
        if (isSpeeding) {
            speed = baseSpeed;
            isSpeeding = false;
        } else {
            speed = lowSpeed;
            isStalling = true;
        }
    }

    private void adjustSpeed() {
        baseSpeed -= 10;
        lowSpeed -= 25;
        highSpeed -= 5;
        if (isSpeeding)
            speed = highSpeed;
        else if (isStalling)
            speed = lowSpeed;
        else
            speed = baseSpeed;
    }
}
