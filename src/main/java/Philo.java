import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Philo extends Main implements Initializable {
    @FXML
    Circle ph1,ph2,ph3,ph4,ph5;
    @FXML
    Label p1l,p2l,p3l,p4l,p5l;
    Philosoph p1,p2,p3,p4,p5;
    List<Label> l = new ArrayList<>();
    List<Circle> c = new ArrayList<>();
    @FXML
    ComboBox<String> algo;
    int options;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        algo.getItems().addAll("Basic","NoDead","NoDeadNoStarve");
        algo.getSelectionModel().selectFirst();

        l.add(p1l);
        l.add(p2l);
        l.add(p3l);
        l.add(p4l);
        l.add(p5l);
        c.add(ph1);
        c.add(ph2);
        c.add(ph3);
        c.add(ph4);
        c.add(ph5);
        startThreads(0);
        algo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("Basic")){
                options = 0;
                restart();
            } else if (newValue.equals("NoDead")){
                options = 1;
                restart();
            } else {
                options = 2;
                restart();
            }
        });
        primar.setOnCloseRequest(event -> {
            p1.stopMe();
            p2.stopMe();
            p3.stopMe();
            p4.stopMe();
            p5.stopMe();
        });
    }
    public void restart() {
        p1.interrupt();
        p2.interrupt();
        p3.interrupt();
        p4.interrupt();
        p5.interrupt();
        for(Label l : l) {
            l.setText("thinking");
        }
        for(Circle c : c) {
            c.setStyle("-fx-fill: red");
        }
        startThreads(options);
    }

    private void startThreads(int option) {
        Monitor m = new Monitor();
        Object one = new Object(),two= new Object(),three= new Object(),four= new Object(),five= new Object();

        p1 = new Philosoph("1",m,10,ph1,p1l,one,five,option);
        p2 = new Philosoph("   2",m,10,ph2,p2l,two,one,option);
        p3 = new Philosoph("      3",m,10,ph3,p3l,three,two,option);
        p4 = new Philosoph("         4",m,10,ph4,p4l,four,three,option);
        p5 = new Philosoph("            5",m,10,ph5,p5l,five,four,option);
        p1.assignPhilos(p2,p5);
        p2.assignPhilos(p3,p1);
        p3.assignPhilos(p4,p2);
        p4.assignPhilos(p5,p3);
        p5.assignPhilos(p1,p4);
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
    }
}
