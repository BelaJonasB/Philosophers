import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Monitor {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";
    synchronized void dropForks(Philosoph p, Circle c, Label l) {
        p.dropLeftFork();
        p.dropRightFork();
        notify();
        Platform.runLater(() -> {
            l.setText("thinking");
            c.setStyle("-fx-fill: red");
        });
        System.out.println(p.who+ANSI_RED+" has dropped his forks"+ANSI_RESET);
    }

    synchronized void waitRightFork(Philosoph p, Circle c, Label l) {
        while(p.pRight.hasLeftFork()) {
            try {
                wait();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        if(p.pickRightFork()) {
            Platform.runLater(() -> {
                l.setText("right Fork");
                c.setStyle("-fx-fill: dodgerblue");
            });
            System.out.println(p.who+ANSI_BLUE+" has picked up right fork"+ANSI_RESET);
        }
    }
    synchronized void waitLeftFork(Philosoph p, Circle c, Label l) {
        while(p.pLeft.hasRightFork()) {
            try {
                wait();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        if(p.pickLeftFork()) {
            Platform.runLater(() -> {
                l.setText("Left fork");
                c.setStyle("-fx-fill: yellow");
            });
            System.out.println(p.who+ANSI_YELLOW+" picked up left fork"+ANSI_RESET);
        }
    }
    synchronized void waitBothForks(Philosoph p) {
        while(p.pLeft.hasRightFork()||p.pRight.hasLeftFork()) {
            try {
                wait();
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        p.pickBothForks();
    }
    public void takeBothForks(Philosoph p){
        p.pickBothForks();
    }
}
