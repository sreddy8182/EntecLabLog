package lablog;

import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    static Stage window; // rename stage to window
    static LabLogController labLogController; // make a lab controller
    static DBConnect dbConnect; // make a db connection object

    @Override
    public void start(Stage primaryStage) throws Exception {
        // establish connection to database
        dbConnect = new DBConnect();

        // hold reference to main stage
        window = primaryStage;

        // set icon for the stage
        window.getIcons().add(new Image(getClass().getResourceAsStream("res/logo_log.png")));

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
        Scene scene = new Scene(layout, 335, 415);
        window.setScene(scene);

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
        // FlatterFX.style();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
