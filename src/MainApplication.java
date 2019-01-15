import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    
    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        Pong pong = new Pong(primaryStage);
    }
}
