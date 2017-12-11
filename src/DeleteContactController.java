import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;


public class DeleteContactController {
    @FXML
    Button searchBtn = new Button();

    @FXML
    TextField firstnameField = new TextField();

    @FXML
    TextField lastnameField = new TextField();

    @FXML
    TextField companyField = new TextField();

    @FXML
    TableView<Contact> searchResultsTable = new TableView<>();

    @FXML
    TableColumn titleCol = new TableColumn();

    @FXML
    TableColumn firstnameCol = new TableColumn();

    @FXML
    TableColumn lastnameCol = new TableColumn();

    @FXML
    TableColumn emailCol = new TableColumn();

    @FXML
    TableColumn skypeidCol = new TableColumn();

    @FXML
    TableColumn phoneCol = new TableColumn();

    @FXML
    TableColumn extnCol = new TableColumn();

    @FXML
    TableColumn emergencyCol = new TableColumn();

    @FXML
    TableColumn companyCol = new TableColumn();

    @FXML
    TableColumn managerCol = new TableColumn();


    public void initialize(){
        searchBtn.setDisable(true);
        searchResultsTable.setPlaceholder(new Label("Search Contacts to delete!"));
    }

    public void deleteByFieldsProvided() throws UnknownHostException{

        Contact contact = searchResultsTable.getSelectionModel().getSelectedItem();

        String email = contact.getEmail();

        ContactController cc = new ContactController();
        cc.deleteContact(email);

        searchByFieldsProvided();

    }

    public void keyReleasedProperty()
    {
        // only after all texts are entered, the button is enabled
        String firstnameFieldText = firstnameField.getText();
        String lastnameFieldText = lastnameField.getText();
        String companyFieldText = companyField.getText();

        boolean isDisabled = (!firstnameFieldText.trim().isEmpty())||(!lastnameFieldText.trim().isEmpty())||(!companyFieldText.trim().isEmpty());
        searchBtn.setDisable(!isDisabled);
    }


    public void searchByFieldsProvided() throws UnknownHostException{
        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String company = companyField.getText().trim();

        searchResultsTable.getItems().clear();

        ContactController cc = new ContactController();
        DBCursor dbc = cc.searchContacts(firstname, lastname, company);

        ArrayList<Contact> matchingContacts = getMatchingContacts(dbc);
        if (matchingContacts.size() == 0){
            searchResultsTable.setPlaceholder(new Label("No Matching Contacts!"));
        } else {
            populateTable(matchingContacts);
        }
    }

    public void populateTable(ArrayList<Contact> contacts){


        titleCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("title"));
        firstnameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstname"));
        lastnameCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastname"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        skypeidCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("skypeid"));
        companyCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("company"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
        extnCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("extn"));
        emergencyCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("emergencyno"));
        managerCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("managername"));

        searchResultsTable.getItems().addAll(contacts);

    }

    public ArrayList<Contact> getMatchingContacts(DBCursor dbc){
        ArrayList<Contact> matchingContacts = new ArrayList<>();

        while (dbc.hasNext()){
            DBObject obj = dbc.next();
            obj.markAsPartialObject();
            System.out.println(obj.toString());

            Contact newContact = new Contact();

            newContact.setFirstname(obj.get("firstname").toString());
            newContact.setLastname(obj.get("lastname").toString());
            newContact.setEmail(obj.get("email").toString());
            newContact.setPhone(obj.get("phone").toString());

            if (obj.containsField("title")) newContact.setTitle(obj.get("title").toString());
            if (obj.containsField("skypeid")) newContact.setSkypeid(obj.get("skypeid").toString());
            if (obj.containsField("company")) newContact.setCompany(obj.get("company").toString());
            if (obj.containsField("managername")) newContact.setManagername(obj.get("managername").toString());
            if (obj.containsField("emergencynum")) newContact.setEmergencyno(obj.get("emergencynum").toString());
            if (obj.containsField("extn")) newContact.setExtn(obj.get("extn").toString());

            Address add = new Address();
            if (obj.containsField("propno")) add.setPropNo(obj.get("propno").toString());
            if (obj.containsField("streetname")) add.setStreetName(obj.get("streetname").toString());
            if (obj.containsField("aptno")) add.setApartmentNo(obj.get("aptno").toString());
            if (obj.containsField("city")) add.setState(obj.get("city").toString());
            if (obj.containsField("state")) add.setCity(obj.get("state").toString());
            newContact.setAddress(add);

            matchingContacts.add(newContact);
        }

        return matchingContacts;
    }

}
