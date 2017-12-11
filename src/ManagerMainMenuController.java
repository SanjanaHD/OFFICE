import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Timer;

public class ManagerMainMenuController {

    @FXML
    TableView<MeetingRow> meetingsTable = new TableView<>();

    @FXML
    TableColumn<MeetingRow, LocalDate> dateColumn = new TableColumn();

    @FXML
    TableColumn<MeetingRow, String> topicColumn = new TableColumn();

    public void initialize() throws UnknownHostException{
        populateTable();
    }

    public void populateTable() {

        ObservableList<MeetingRow> pendingMeetings = FXCollections.observableArrayList();

        ArrayList<MeetingRow> allMeetings = getMeetings();
        if (allMeetings.size() == 0){
            meetingsTable.setPlaceholder(new Label("No meetings scheduled"));
        } else {
            for (MeetingRow m : allMeetings){
                if (!m.date.isBefore(LocalDate.now())){
                    pendingMeetings.add(m);
                }
            }
        }
        meetingsTable.getItems().clear();
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        topicColumn.setCellValueFactory(new PropertyValueFactory("topic"));
        meetingsTable.getItems().addAll(pendingMeetings);
    }


    public ArrayList<MeetingRow> getMeetings() {
        ArrayList<MeetingRow> meetingRows = new ArrayList<>();
        MeetingController mc = new MeetingController();
        try {
            DBCursor dbc = mc.retrieveAllMeetings();
            while (dbc.hasNext()){
                DBObject obj = dbc.next();
                obj.markAsPartialObject();


                MeetingRow newMeeting = new MeetingRow();
                if (obj.containsField("_id")) newMeeting.id = obj.get("_id").toString();
                if (obj.containsField("topic")) newMeeting.topic = (String)obj.get("topic");
                if (obj.containsField("date")){
                    String dateString = (String)obj.get("date");
                    LocalDate dateLD = LocalDate.parse(dateString);
                    newMeeting.date = dateLD;
                }
                meetingRows.add(newMeeting);
            }
        }
        catch (UnknownHostException uhe){

        }
        return meetingRows;
    }


    public void addContact(ActionEvent e) throws IOException{
        Stage addContactStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddContact.fxml"));
        Scene scene = new Scene(root);
        addContactStage.setTitle("Add New Contact");
        addContactStage.setScene(scene);
        addContactStage.setResizable(false);
        addContactStage.show();
    }

    public void editContact(ActionEvent e) throws IOException{
        Stage searchContactStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("EditContact.fxml"));
        Scene scene = new Scene(root);
        searchContactStage.setTitle("Search & Edit Contact");
        searchContactStage.setScene(scene);
        searchContactStage.setResizable(false);
        searchContactStage.show();
    }

    public void deleteContact(ActionEvent e) throws IOException{
        Stage deleteContactStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DeleteContact.fxml"));
        Scene scene = new Scene(root);
        deleteContactStage.setTitle("Search & Delete Contact");
        deleteContactStage.setScene(scene);
        deleteContactStage.setResizable(false);
        deleteContactStage.show();
    }

    public void manageMeetings(ActionEvent e) throws IOException{
        Stage manageMeetingsStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ManageMeeting.fxml"));
        Scene scene = new Scene(root);
        manageMeetingsStage.setTitle("Manage Meetings");
        manageMeetingsStage.setScene(scene);
        manageMeetingsStage.setResizable(false);

        manageMeetingsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                populateTable();
            }
        });

        manageMeetingsStage.show();
    }
}
