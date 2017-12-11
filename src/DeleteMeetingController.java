import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DeleteMeetingController {
    @FXML
    TableView<MeetingRow> meetingsTable = new TableView<>();

    @FXML
    TableColumn<MeetingRow, LocalDate> dateColumn = new TableColumn();

    @FXML
    TableColumn<MeetingRow, String> topicColumn = new TableColumn();

    @FXML
    Button deleteMeetingsBtn = new Button();

    String selectedMeetingId;


    public void initialize() throws UnknownHostException{
        populateTable();
        deleteMeetingsBtn.setDisable(true);

        meetingsTable.setOnMouseClicked(event -> {
            deleteMeetingsBtn.setDisable(false);
            MeetingRow row = meetingsTable.getSelectionModel().getSelectedItem();
            selectedMeetingId = row.id;
            deleteMeetingsBtn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {

                    MeetingController mc = new MeetingController();
                    mc.deleteMeeting(row.id);
                    populateTable();
                }
            });
        });
    }


    public void populateTable()  {

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
    try {
        MeetingController mc = new MeetingController();
        DBCursor dbc = mc.retrieveAllMeetings();
        //ArrayList<MeetingRow> meetingRows = new ArrayList<>();
        while (dbc.hasNext()){
            DBObject obj = dbc.next();
            obj.markAsPartialObject();
            //System.out.println(obj.toString());

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
}
