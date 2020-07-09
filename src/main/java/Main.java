import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primar;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primar = primaryStage;
        Parent root;
        root = FXMLLoader.load(getClass().getResource("philo.fxml"));
        primar.setResizable(false);
        primar.setScene(new Scene(root, 500, 500));
        primar.setTitle("Philosophers");
        primar.show();
    }
}