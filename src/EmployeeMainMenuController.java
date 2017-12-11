import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeMainMenuController {
    String email;

    @FXML
    TableView<MeetingRow> meetingsTable = new TableView<>();

    @FXML
    TableColumn<MeetingRow, LocalDate> dateColumn = new TableColumn();

    @FXML
    TableColumn<MeetingRow, String> topicColumn = new TableColumn();

    public void initData(String email) throws  java.net.UnknownHostException {
        this.email = email;
    }

    public void initialize() throws UnknownHostException {
        //todo: get email here from

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppStart.fxml"));
        AppStartController controller = loader.getController();

        String user = controller.usernameLoggedIn;

        ArrayList<MeetingRow> allMeetings = getMeetings(user);
        if (allMeetings.size() == 0) {
            meetingsTable.setPlaceholder(new Label("No meetings scheduled"));
        } else {
            ArrayList<MeetingRow> pendingMeetings = new ArrayList<>();
            for (MeetingRow m : allMeetings) {
                if (!m.date.isBefore(LocalDate.now())) {
                    pendingMeetings.add(m);
                }
            }
            populateTable(pendingMeetings);
        }
    }

    public void populateTable(ArrayList<MeetingRow> rows){

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        topicColumn.setCellValueFactory(new PropertyValueFactory("topic"));
        meetingsTable.getItems().addAll(rows);
    }

    public ArrayList<MeetingRow> getMeetings(String email) throws UnknownHostException{

        MeetingController mc = new MeetingController();
        DBCursor dbc = mc.retrieveAllMeetings();
        ArrayList<MeetingRow> meetingRows = new ArrayList<>();
        while (dbc.hasNext()){
            DBObject obj = dbc.next();
            obj.markAsPartialObject();

            MeetingRow newMeeting = new MeetingRow();
            String allAttendeesEmail="";
            if (obj.containsField("_id")) newMeeting.id = obj.get("_id").toString();
            if (obj.containsField("topic")) newMeeting.topic = (String)obj.get("topic");
            if (obj.containsField("date")){
                String dateString = (String)obj.get("date");
                LocalDate dateLD = LocalDate.parse(dateString);
                newMeeting.date = dateLD;
            }
            if (obj.containsField("attendeesEmail")) {
                allAttendeesEmail = obj.get("attendeesEmail").toString();

            }
            if ((!allAttendeesEmail.equals("")) & allAttendeesEmail.contains(email)){
                meetingRows.add(newMeeting);
            }

        }
        return meetingRows;
    }
}
