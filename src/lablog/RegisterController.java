package lablog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lablog.tables.Student;

// register class controller, holds the logic for registering a student
public class RegisterController {
    // views
    public Button btnRegister;
    public Button btnCancel;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtId;
    public TextField txtCourse;
    public TextField txtProfessor;
    public Label lblInfo;

    // constructor
    RegisterController() { }

    // register btn action
    private void register() {
        // check to make sure no fields are empty
        if (checkFields()) {
            // create student and clean input
            Student student = new Student();
            student.setFirstName(txtFirstName.getText().replaceAll("\\s+","").toUpperCase());
            student.setLastName(txtLastName.getText().replaceAll("\\s+","").toUpperCase());
            student.setStrId(txtId.getText().replaceAll("\\s+","").toUpperCase());
            student.setSubject(txtCourse.getText().replaceAll("\\s+","").toUpperCase());
            student.setProfessor(txtProfessor.getText().toUpperCase());

            // perform actions
            boolean registered = Main.dbConnect.registerStudent(student);
            boolean loggedIn = Main.dbConnect.logIn(student.getStrId()) == 1;

            // check if actions where successful
            if (registered && loggedIn) {
                // change back to lablog
                back();

                // inform user
                Main.labLogController.infoMessage("Registered & Logged in Successfully", Color.GREEN);

                // load combo box
                Main.labLogController.loadLoggedIn();
            } else if (registered) {
                // change back to lablog
                back();

                // inform user
                Main.labLogController.infoMessage("Registered Successfully", Color.GREEN);
            }
        }
    }

    // cancel btn action
    private void back() {
        // go back to lablog scene
        // make loader and set controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("lablog.fxml"));
        loader.setController(Main.labLogController);

        // declare layout
        Parent layout;

        // attempt to load fxml
        try {
            // create layout from parent
            layout = loader.load();
        } catch (Exception e) {
            // log error
            System.out.println("Error: " + e.getMessage());

            // exit
            return;
        }

        // set it to the stage
        Main.window.setScene(new Scene(layout, 335, 415));

        // refresh the list in combo box
        Main.labLogController.loadLoggedIn();
    }

    // check if string has all letters
    private boolean allLetters(String string) {
        // iterate through each char
        for (int i = 0, length = string.length(); i < length; i++) {
            if (!Character.isLetter(string.charAt(i)) && !Character.isWhitespace(string.charAt(i)))
                return false;
        }

        // has all letters
        return true;
    }


    // check txt fields
    private boolean checkFields() {
        // check for empty fields
        if (txtFirstName.getText().isEmpty() || txtLastName.getText().isEmpty() ||
                txtCourse.getText().isEmpty() || txtProfessor.getText().isEmpty()) {
            lblInfo.setText("All Fields Are Required");
            return false;
        }

        // check lengths fields
        // first name
        if (txtFirstName.getText().length() > 50 || !allLetters(txtFirstName.getText())) {
            lblInfo.setText("Invalid First Name Field");
            txtFirstName.setText(""); // clear field
            return false;
        }
        // last name
        if (txtLastName.getText().length() > 50 || !allLetters(txtLastName.getText())) {
            lblInfo.setText("Invalid Last Name Field");
            txtLastName.setText(""); // clear field
            return false;
        }
        // course
        if (txtCourse.getText().length() > 7){
            lblInfo.setText("Invalid Course");
            txtCourse.setText(""); // clear field
            return false;
        }
        // professor
        if (txtProfessor.getText().length() > 100 || !allLetters(txtProfessor.getText())) {
            lblInfo.setText("Invalid Professor Field");
            txtProfessor.setText(""); // clear field
            return false;
        }

        // check format
        boolean wrong = false;
        String course = txtCourse.getText(); // get course

        // make sure its 3 letters followed by 4 numbers
        for (int i = 0, length = course.length(); i < length; i++) {
            // first 3 chars
            if (i < 3 && !Character.isLetter(course.charAt(i)))
                wrong = true;
            // last 4 chars
            else if (i >= 3 && !Character.isDigit(course.charAt(i)))
                wrong = true;
        }

        // check if format wrong
        if (wrong) {
            lblInfo.setText("Invalid Course");
            txtCourse.setText(""); // clear field
            return false;
        }

        // no wrong input success
        return true;
    }

    // setup views
    void setupViews() {
        // editing id is not allowed
        txtId.setDisable(true);

        // give btn action
        btnRegister.setOnAction(e -> register());
        btnCancel.setOnAction(e -> back());
    }
}
