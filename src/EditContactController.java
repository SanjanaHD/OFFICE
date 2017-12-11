import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;


public class EditContactController {
    @FXML
    Button searchBtn = new Button();

    @FXML
    TextField firstnamefield = new TextField();

    @FXML
    TextField lastnamefield = new TextField();

    @FXML
    TextField companyfield = new TextField();

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

    HashMap<Contact, String> ids;

    public void initialize() {
        searchBtn.setDisable(true);
        searchResultsTable.setPlaceholder(new Label("Search Contacts to edit!"));
        searchResultsTable.setRowFactory( tv -> {
            TableRow<Contact> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Contact rowData = row.getItem();
                    String id = ids.get(rowData);
                    Stage addContactStage = new Stage();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditSelectedContact.fxml"));
                        //Parent root = loader.load(getClass().getResource("EditSelectedContact.fxml"));
                        Scene scene = new Scene(loader.load());
                        addContactStage.setTitle("Update Contact");
                        addContactStage.setScene(scene);
                        addContactStage.setResizable(false);
                        EditSelectedContactController controller = loader.getController();
                        controller.initData(id, rowData, addContactStage);
                        addContactStage.showAndWait();
                        searchByFieldsProvided();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
        ids = new HashMap<>();
    }

    public void keyReleasedProperty()
    {
        // only after all texts are entered, the button is enabled
        String firstnameFieldText = firstnamefield.getText();
        String lastnameFieldText = lastnamefield.getText();
        String companyFieldText = companyfield.getText();

        boolean isDisabled = (!firstnameFieldText.trim().isEmpty())||(!lastnameFieldText.trim().isEmpty())||(!companyFieldText.trim().isEmpty());
        searchBtn.setDisable(!isDisabled);
    }

    public void searchByFieldsProvided() throws UnknownHostException{
        String firstname = firstnamefield.getText().trim();
        String lastname = lastnamefield.getText().trim();
        String company = companyfield.getText().trim();

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

            matchingContacts.add(newContact);
            ids.put(newContact, obj.get("_id").toString());
        }

        return matchingContacts;
    }
}