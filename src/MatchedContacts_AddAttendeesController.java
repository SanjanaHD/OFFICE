import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;


public class MatchedContacts_AddAttendeesController {

	@FXML
	TextField firstnameField = new TextField();

	@FXML
	TextField lastnameField = new TextField();

	@FXML
	TextField companyField = new TextField();

	@FXML
	Button searchBtn = new Button();
	
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

//	@FXML TableColumn skypeidCol = new TableColumn();

	@FXML
	TableColumn phoneCol = new TableColumn();

	@FXML
	TableColumn extnCol = new TableColumn();

//	@FXML TableColumn emergencyCol = new TableColumn();

	@FXML
	TableColumn companyCol = new TableColumn();

	@FXML
	TableColumn managerCol = new TableColumn();

	@FXML
	Button addAttendeeBtn = new Button();

	Contact contactToAdd;

	private final ReadOnlyObjectWrapper<Contact> contactToAccess = new ReadOnlyObjectWrapper<>();

	public ReadOnlyObjectProperty<Contact> currentContactProperty() {
		return contactToAccess.getReadOnlyProperty();
	}

	public void initialize() {
		searchBtn.setDisable(true);
		searchResultsTable.setPlaceholder(new Label("Search Contacts to Add to Meeting!"));
		searchResultsTable.setOnMouseClicked( event ->  {
			Contact selectedContact = searchResultsTable.getSelectionModel().getSelectedItem();
			contactToAdd = selectedContact;
		});
	}

	public void keyReleasedProperty() {
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
		DBCursor dbc =  cc.searchContacts(firstname, lastname, company);

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
		//skypeidCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("skypeid"));
		companyCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("company"));
		phoneCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
		extnCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("extn"));
		//emergencyCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("emergencyno"));
		managerCol.setCellValueFactory(new PropertyValueFactory<Contact, String>("managername"));

		searchResultsTable.getItems().addAll(contacts);

	}

	public ArrayList<Contact> getMatchingContacts(DBCursor dbc){
		ArrayList<Contact> matchingContacts = new ArrayList<>();

		while (dbc.hasNext()){
			DBObject dbContactObj = dbc.next();
			dbContactObj.markAsPartialObject();
			System.out.println(dbContactObj.toString());

			Contact newContact = new Contact();

			newContact.setFirstname(dbContactObj.get("firstname").toString());
			newContact.setLastname(dbContactObj.get("lastname").toString());
			newContact.setEmail(dbContactObj.get("email").toString());
			newContact.setPhone(dbContactObj.get("phone").toString());

			if (dbContactObj.containsField("title")) newContact.setTitle(dbContactObj.get("title").toString());
			if (dbContactObj.containsField("skypeid")) newContact.setSkypeid(dbContactObj.get("skypeid").toString());
			if (dbContactObj.containsField("company")) newContact.setCompany(dbContactObj.get("company").toString());
			if (dbContactObj.containsField("managername")) newContact.setManagername(dbContactObj.get("managername").toString());
			if (dbContactObj.containsField("emergencynum")) newContact.setEmergencyno(dbContactObj.get("emergencynum").toString());
			if (dbContactObj.containsField("extn")) newContact.setExtn(dbContactObj.get("extn").toString());

			Address add = new Address();
			if (dbContactObj.containsField("propno")) add.setPropNo(dbContactObj.get("propno").toString());
			if (dbContactObj.containsField("streetname")) add.setStreetName(dbContactObj.get("streetname").toString());
			if (dbContactObj.containsField("aptno")) add.setApartmentNo(dbContactObj.get("aptno").toString());
			if (dbContactObj.containsField("city")) add.setState(dbContactObj.get("city").toString());
			if (dbContactObj.containsField("state")) add.setCity(dbContactObj.get("state").toString());
			newContact.setAddress(add);

			matchingContacts.add(newContact);
		}
		return matchingContacts;
	}

	public void addAttendee(){
		System.out.println("Clicked on add attendee");
		contactToAccess.set(contactToAdd);
	}

}
