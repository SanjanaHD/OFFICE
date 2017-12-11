import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddMeetingController {

    @FXML
    ListView attendeesListView = new ListView();

    @FXML
    TextField topic = new TextField();

    @FXML
    TextArea notes = new TextArea();

    @FXML
    Button searchBtn = new Button();

    @FXML
    DatePicker meetingDate = new DatePicker();

    ArrayList<String> emailAddrToAddToMeeting = new ArrayList<>();
    ArrayList<String>  attendeesList;

    public void initialize(){
        meetingDate.setValue(LocalDate.now());
    }

    private void generateAlertBox(String message){
        Alert al = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        al.show();
    }

    public void populateTable(ArrayList<String> attendeesList){

        ListProperty<String> listProperty = new SimpleListProperty<>();
        listProperty.set(FXCollections.observableArrayList(attendeesList));
        attendeesListView.itemsProperty().bind(listProperty);
    }

    public void saveMeeting() throws UnknownHostException{
        LocalDate ld = meetingDate.getValue();
        if (ld.isBefore(LocalDate.now())){
            generateAlertBox("Invalid Date. Setting Date to Today.");
            meetingDate.setValue(LocalDate.now());

        } else if(topic.getText().isEmpty()){
            generateAlertBox("Topic cannot be empty.");
        } else if (emailAddrToAddToMeeting.size() == 0){
            generateAlertBox("Meeting must have at least one person attending the meeting");
        } else {
            MeetingController mc = new MeetingController();
            mc.addMeeting(topic.getText(), notes.getText(), emailAddrToAddToMeeting, ld);
        }
    }

    public void searchAndAddAttendees(ActionEvent e) throws IOException {
        Stage searchContactStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MatchedContacts_AddAttendees.fxml"));
        Parent parent = fxmlLoader.load();

        Scene scene = new Scene(parent);
        searchContactStage.setTitle("Search & Add Attendee");
        searchContactStage.setScene(scene);
        searchContactStage.setResizable(false);
        searchContactStage.show();

        MatchedContacts_AddAttendeesController cntlr = fxmlLoader.getController();

        cntlr.currentContactProperty().addListener((obs, oldIncomingContact, incomingContact) -> {
            emailAddrToAddToMeeting.add(incomingContact.getEmail());
        });

        searchContactStage.setOnCloseRequest(windowEvent -> {

            MeetingController mc = new MeetingController();
            try {
                attendeesList = mc.getPeopleFromEmailList(emailAddrToAddToMeeting);
            }
            catch (Exception ex){
                System.out.println();
            }

            populateTable(attendeesList);
        });
    }
}