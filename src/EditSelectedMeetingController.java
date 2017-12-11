import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class EditSelectedMeetingController {
    String id;

    @FXML
    TextField topic = new TextField();

    @FXML
    TextArea notes = new TextArea();

    @FXML
    DatePicker meetingDate = new DatePicker();

    @FXML
    Button saveChangesBtn = new Button();

    public void initData(String inputId) throws  java.net.UnknownHostException {
        this.id = inputId;

        MeetingController mc = new MeetingController();
         Meeting meetingEvent = mc.getMeeting(inputId);

        topic.setText(meetingEvent.getTopic());
        notes.setText(meetingEvent.getNotes());
        meetingDate.setValue(meetingEvent.getMeetingDate());
    }

    public void saveChanges(){

        String newTopic = topic.getText();
        String newNote = notes.getText();
        LocalDate newDate = meetingDate.getValue();

        System.out.println(newTopic);
        System.out.println(id);

        Meeting newMeetingEvent = new Meeting();
        newMeetingEvent.setTopic(newTopic);
        newMeetingEvent.setNotes(newNote);
        newMeetingEvent.setMeetingDate(newDate);

        MeetingController mc = new MeetingController();
        mc.updateMeetingById(id, newMeetingEvent);

    }
}
