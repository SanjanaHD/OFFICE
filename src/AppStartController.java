import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class AppStartController {
    static String  usernameLoggedIn;

    @FXML
    private TextField usernameinput;

    @FXML
    private PasswordField passwordinput;

    @FXML
    private Label loginstatusLabel;

    @FXML
    CheckBox employeeCheckbox = new CheckBox();

    @FXML
    GridPane rootPane;

    public void login(ActionEvent event) throws IOException{

        String usernameEntered = usernameinput.getText().trim();
        String passwordEntered = passwordinput.getText().trim();
        Boolean isEmployee = employeeCheckbox.isSelected();


        DBManager dbm = new DBManager();
        boolean successfulLogin = dbm.mongoLogin(usernameEntered, passwordEntered, isEmployee);


        if (successfulLogin){
            System.out.println("Correct password..");
            loginstatusLabel.setText("Logging in..");

            if (usernameEntered.equals("mgr") & isEmployee == false){
                AnchorPane MainMenuPane = FXMLLoader.load(getClass().getResource("ManagerMainMenu.fxml"));
                rootPane.getChildren().setAll(MainMenuPane);
            } else if (isEmployee == true){
                usernameLoggedIn = usernameEntered;

                AnchorPane employeeMainMenuPane = FXMLLoader.load(getClass().getResource("EmployeeMainMenu.fxml"));
                rootPane.getChildren().setAll(employeeMainMenuPane);
            }
        } else {
            loginstatusLabel.setText("Incorrect credentials..");
        }
    }
}
