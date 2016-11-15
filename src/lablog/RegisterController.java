package lablog;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;

// register class controller, holds the logic for registering a student
public class RegisterController {
    // fields
    private String strId;

    // views
    public Button btnRegister;
    public TextField txtFirstName;
    public TextField txtLastName;
    public TextField txtId;
    public TextField txtCourse;
    public TextField txtProfessor;

    // constructor
    public RegisterController() {

    }

    // btn action
    public void Register() {

    }

    // communicate with lablog
    public void setStrId(String strId) {
        this.strId = strId;
    }
}
