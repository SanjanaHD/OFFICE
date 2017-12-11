import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class AddContactController {
    @FXML
    TextField titlefield = new TextField();

    @FXML
    TextField firstnamefield = new TextField();

    @FXML
    TextField lastnamefield = new TextField();

    @FXML
    TextField emailfield = new TextField();

    @FXML
    TextField skypeidfield = new TextField();

    @FXML
    TextField companyfield = new TextField();

    @FXML
    TextField managernamefield = new TextField();

    @FXML
    TextField emergencynofield = new TextField();

    @FXML
    TextField phonefield = new TextField();

    @FXML
    TextField extnfield = new TextField();

    @FXML
    TextField propnofield = new TextField();

    @FXML
    TextField streetnamefield = new TextField();

    @FXML
    TextField aptnofield = new TextField();

    @FXML
    TextField cityfield = new TextField();

    @FXML
    TextField statefield = new TextField();

    @FXML
    Button addcontactbtn = new Button();

    boolean addContactBtnDisableProp;

    public void initialize(){
        // initially add contact button is disabled
        addContactBtnDisableProp = true;
        addcontactbtn.setDisable(addContactBtnDisableProp);
    }

    private boolean isEmailValid(String inputEmailAddress){
        Pattern pattern;
        Matcher matcher;

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(inputEmailAddress);
        boolean emailPatternValid = matcher.matches();
        return emailPatternValid;
    }

    private boolean isFirstNameValid(String inputFirstName){
        if (inputFirstName.length() < 2){
            return false;
        } else {
            return true;
        }
    }

    private  boolean isLastNameValid(String inputLastName){
        if (inputLastName.length() < 2){
            return false;
        } else {
            return true;
        }
    }

    private void generateAlertBox(String message){
        Alert al = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        al.show();
    }

    public void keyReleasedProperty()
    {
        // only after all texts are entered, the button is enabled
        String firstnamefieldText = firstnamefield.getText();
        String lastnamefieldText = lastnamefield.getText();
        String emailfieldText = emailfield.getText();
        String phonefieldText = phonefield.getText();

        boolean isDisabled = (firstnamefieldText.trim().isEmpty())||(lastnamefieldText.trim().isEmpty())||(emailfieldText.trim().isEmpty())||(phonefieldText.trim().isEmpty());
        addcontactbtn.setDisable(isDisabled);
    }

    public void saveDetails() throws IOException{

        String title = titlefield.getText().trim();
        String firstname = firstnamefield.getText().trim();
        String lastname = lastnamefield.getText().trim();
        String email = emailfield.getText().trim();
        String skypeid = skypeidfield.getText().trim();
        String company = companyfield.getText().trim();
        String managername = managernamefield.getText().trim();
        String emergencyno = emergencynofield.getText().trim();
        String phone = phonefield.getText().trim();
        String extn = extnfield.getText().trim();
        String propNo = propnofield.getText().trim();
        String streetName = streetnamefield.getText().trim();
        String aptNo = aptnofield.getText().trim();
        String city = cityfield.getText().trim();
        String state = statefield.getText().trim();


        if (isEmailValid(email) == false){
            generateAlertBox("Invalid Email Address Format.");
        } else if (isFirstNameValid(firstname) == false){
            generateAlertBox("First Name must be at least 2 letters");
        } else if (isLastNameValid(lastname) == false){
            generateAlertBox("Last Name must be at least 2 letters");
        } else {

            ContactController cc = new ContactController();
            cc.addContact(title, firstname, lastname, email, skypeid, company, managername, emergencyno, phone, extn, propNo, streetName, aptNo, city, state);

            // clear fields
            titlefield.clear();
            firstnamefield.clear();
            lastnamefield.clear();
            emailfield.clear();
            skypeidfield.clear();
            companyfield.clear();
            managernamefield.clear();
            emergencynofield.clear();
            phonefield.clear();
            extnfield.clear();
            propnofield.clear();
            streetnamefield.clear();
            aptnofield.clear();
            cityfield.clear();
            statefield.clear();
        }
    }
}