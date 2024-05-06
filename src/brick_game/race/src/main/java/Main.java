import edu.school21.racelogic.FiniteStateMachine.FiniteStateMachine;
import edu.school21.racelogic.MapMatrix.MapMatrix;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        MapMatrix matrix = new MapMatrix();
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            String input = scanner.next();
//            if (input.equals("w")) {
//                matrix.userInput(MapMatrix.Action.Up);
//            } else if (input.equals("s")) {
//                matrix.userInput(MapMatrix.Action.Down);
//            } else if (input.equals("a")) {
//                matrix.userInput(MapMatrix.Action.Left);
//                matrix.renderMap();
//            } else if (input.equals("d")) {
//                matrix.userInput(MapMatrix.Action.Right);
//                matrix.renderMap();
//            } else if (input.equals("b")) {
//                if (matrix.getState() != FiniteStateMachine.State.PLAYING) {
//                    matrix.bootUp();
//                    Thread thread = new Thread(() -> update(matrix));
//                    thread.start();
//                }
//            }
//        }
    }

//    private static void update(MapMatrix mapMatrix) {
//        while (mapMatrix.getState() == FiniteStateMachine.State.PLAYING) {
//            mapMatrix.renderMap();
//            System.out.println();
//            System.out.println();
//            mapMatrix.updateState();
//            try {
//                Thread.sleep(mapMatrix.getSpeed());
//            } catch (InterruptedException e) {}
//        }
//    }

}
