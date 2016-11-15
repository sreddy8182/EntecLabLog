package lablog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lablog.tables.Student;

public class Main extends Application {
    // rename stage to window
    static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        // create layout from parent
        Parent layout = FXMLLoader.load(getClass().getResource("lablog.fxml"));

        // set the window title
        window.setTitle("Entec Lab Log");

        // give window a scene
        window.setScene(new Scene(layout, 335, 415));

        // settings
        window.setResizable(false);

        // show the window
        window.show();
    }

    // change to register scene
    public void registerScene() throws Exception {

    }


    public static void main(String[] args) {
        launch(args);
    }
}
