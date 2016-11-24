package lablog;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // rename stage to window
    static Stage window;
    static LabLogController labLogController;

    // fields
    static DBConnect dbConnect;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // establish connection to database
        dbConnect = new DBConnect();

        // hold reference to main stage
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
            dbConnect.closeConnection();
        });

        // show the window
        window.show();

        // initialize controller
        labLogController.init();

        // style form
        FlatterFX.style();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
