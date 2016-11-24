package lablog;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lablog.tables.Student;

import java.util.ArrayList;
import java.util.Timer;

/// log class controller, holds the logic for signing in and out
public class LabLogController {
    // views
    public ComboBox<String> lstStudents;
    public Button btnSubmit;
    public TextField txtId;
    public Label lblInfo;

    // alert views
    private Stage alertWindow;
    public Button btnYes;
    public Button btnNo;
    public Label lblMessage;

    // constants
    private static final int SIZE_ONE = 9;
    private static final int SIZE_TWO = 10;

    // controller fields
    Timer timer;

    // constructor
    LabLogController() {
    }

    // load logged in students
    boolean loadLoggedIn() {
        // declare array list
        ArrayList<Student> students;

        // attempt to get all logged in from database
        if ((students = Main.dbConnect.getLoggedIn()) == null){
            // its either empty or error
            // make combo empty
            lstStudents.setItems(FXCollections.observableList(new ArrayList<String>(0)));

            // return error
            return false;
        }

        // make list
        ArrayList<String> list = new ArrayList<>();

        // get all strId for every id
        for (int i = 0; i < students.size(); i++) {
            list.add(students.get(i).getFirstName() + " " + students.get(i).getLastName().charAt(0) + " - " + students.get(i).getStrId());
        }

        // make observable list
        ObservableList<String> oList = FXCollections.observableList(list);

        // add list to combo box
        lstStudents.setItems(oList);

        // success
        return true;
    }


    // btnSubmit event handler
    public void logStudent() {
        // declare status
        int status;

        // get id from label
        String strId = txtId.getText();

        // error check old and new id
        if ((strId.length() == SIZE_ONE && checkForm(strId, true)) ||
                (strId.length() == SIZE_TWO && checkForm(strId, false))) {
            // attempt to log student in
            // database error 0
            if ((status = Main.dbConnect.logIn(strId)) == 0) {
                // show database error to user
                infoMessage("Database Error", Color.RED);
            }

            // student not registered -1
            else if (status == -1) {
                // show new alert dialog
                if (!displayAlert("Information", "Would you like to register this student?")) {
                    // show error
                    infoMessage("Alert Error", Color.RED);
                }
            }

            // student is logged in -2
            else if (status == -2) {
                // log the student out
                if (Main.dbConnect.logOut(strId) == 1) {
                    // show logout confirmation
                    infoMessage("Logged out Successfully", Color.BLUE);

                    txtId.setText(""); // clear
                } else {
                    // show database error to user
                    infoMessage("Database Error", Color.RED);
                }
            }

            // student was logged in 0
            else if (status == 1) {
                // show login confirmation
                infoMessage("Logged in Successfully", Color.GREEN);

                txtId.setText(""); // clear
            }

            // load students to combo box
            loadLoggedIn();
        } else {
            // show invalid input error to user
            infoMessage("Invalid Format", Color.RED);

            txtId.setText(""); // clear
        }
    }

    // write info message
    void infoMessage(String message, Paint paint) {
        // add message and color
        lblInfo.setText(message);
        lblInfo.setTextFill(paint);

        // clear label after ten seconds
        // check to see if something is scheduled
        if (timer != null)
            timer.cancel(); // cancel previous timer
        timer = new Timer(); // make new one
        timer.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // clear on javafx ui thread
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                lblInfo.setText(""); // clear
                            }
                        });
                    }
                },
                10000 // 10 seconds
        );
    }

    // change scene
    private boolean register() {
        // declare layout and id
        Parent layout;
        String id = txtId.getText();

        // create controller
        RegisterController registerController = new RegisterController();

        // initialize loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
        loader.setController(registerController);

        // attempt to load fxml
        try {
            // create layout from parent
            layout = loader.load();

            // change scene to registration
            Main.window.setScene(new Scene(layout, 335, 415));

            // pass id to new scene and setup
            registerController.txtId.setText(id);
            registerController.setupViews();

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

    private boolean displayAlert(String title, String message) {
        // fields
        Parent layout;

        // make loader and set controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("alert.fxml"));
        loader.setController(Main.labLogController);

        // attempt to load fxml
        try {
            // create layout from parent
            layout = loader.load();
        } catch (Exception e) {
            // log error
            System.out.println("Error: " + e.getMessage());

            // exit with error
            return false;
        }

        // create stage
        alertWindow = new Stage();

        // set stage properties
        alertWindow.setScene(new Scene(layout, 330, 135));
        alertWindow.setResizable(false);
        alertWindow.setAlwaysOnTop(true);
        alertWindow.setTitle(title);

        // no other stage can not receive events
        alertWindow.initModality(Modality.APPLICATION_MODAL);

        // show window and request focus
        alertWindow.show();
        alertWindow.requestFocus();

        // set lbl message
        lblMessage.setText(message);

        // set btn actions
        btnYes.setOnAction(e -> yesAction());
        btnNo.setOnAction(e -> noAction());

        // return success
        return true;
    }

    // action to perform on yes
    private void yesAction() {
        // perform change
        if (!register()) {
            // show error to user
            infoMessage("Cannot Open Registration", Color.RED);
        }
        // kill alert window
        alertWindow.close();
    }

    // action to perform on no
    private void noAction() {
        // kill alert window
        alertWindow.close();

        // clear lbl and txt
        lblInfo.setText("");
        txtId.setText("");
    }
}