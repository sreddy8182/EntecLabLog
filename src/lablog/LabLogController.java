package lablog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

// log class controller, holds the logic for signing in and out
public class LabLogController {
    // views
    public Label lblInfo;
    public Button btnSubmit;
    public TextField txtId;

    // fields
    private DBConnect dbConnect;

    // constants
    private static final int SIZE_ONE = 9;
    private static final int SIZE_TWO = 10;

    // constructor
    public LabLogController() {
        // establish connection
        dbConnect = new DBConnect();
    }

    // btnSubmit event handler
    public void logStudent() {
        // declare status
        int status = 0;

        // get id from label
        String strId = txtId.getText();

        // error check old and new id
        if ((strId.length() == SIZE_ONE && checkForm(strId, true)) || (strId.length() == SIZE_TWO && checkForm(strId, false))) {
            // attempt to log student in
            if ((status = dbConnect.logIn(strId)) == 0) {
                // show database error to user
                infoMessage("Database Error", Color.RED);
            } else if (status == -1) {
                if (!changeScene("register.fxml")) {
                    // show error to user
                    infoMessage("Cannot Open Registration", Color.RED);
                }
            } else if (status == -2) {
                // log the student out
                if (dbConnect.logOut(strId) == 1) {
                    // show logout confirmation
                    infoMessage("Logged out Successfully", Color.BLUE);
                } else {
                    // show database error to user
                    infoMessage("Database Error", Color.RED);
                }
            } else if (status == 1) {
                // show login confirmation
                infoMessage("Logged in Successfully", Color.GREEN);
            }
        } else {
            // show invalid input error to user
            infoMessage("Invalid ID", Color.RED);
        }

        // clear text field
        txtId.setText("");
    }

    // write info message
    private void infoMessage(String message, Paint paint) {
        // add message and color
        lblInfo.setText(message);
        lblInfo.setTextFill(paint);
    }

    // change scene
    private boolean changeScene(String fxml) {
        // declare layout
        Parent layout;

        // attempt to load fxml
        try {
            // create layout from parent
            layout = FXMLLoader.load(getClass().getResource(fxml));

            // change scene to registration
            Main.window.setScene(new Scene(layout, 335, 415));

            // return success
            return true;
        } catch (Exception e) {
            // log error
            System.out.println("Error: " + e.getMessage());

            // return error
            return false;
        }
    }

    // check status
    private boolean checkForm(String strId, boolean type) {
        // check based on type
        if (type) {
            // check for first char
            if (!Character.isLetter(strId.charAt(0)))
                return false;

            // iterate through each char
            for (int i = 1; i < strId.length(); i++) {
                // check for digits
                if (!Character.isDigit(strId.charAt(i)))
                    return false;
            }
        } else {
            // iterate through each char
            for (int i = 0; i < strId.length(); i++) {
                // check for ten digits
                if (!Character.isDigit(strId.charAt(i)))
                    return false;
            }
        }

        // in the form
        return true;
    }

}