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
    public static Stage window;
    public static LabLogController labLogController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        // create instance of controller
        labLogController = new LabLogController();

        // make loader and set controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lablog.fxml"));
        loader.setController(labLogController);

        // create layout from parent
        Parent layout = loader.load();

        // set the window title
        window.setTitle("Entec Lab Log");

        // give window a scene
        window.setScene(new Scene(layout, 335, 415));

        // settings
        window.setResizable(false);
        window.setOnCloseRequest(e -> {
            // close db connection
            labLogController.dbConnect.closeConnection();
        });

        // show the window
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
