import java.time.LocalDate;
import java.util.ArrayList;

public class Meeting {
    String topic;
    String notes;
    LocalDate meetingDate;
    ArrayList<Contact> attendees;
    ArrayList<String> attendeesEmailList;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public void setAttendees(ArrayList<String> attendees){
        this.attendeesEmailList = attendees;
    }

    public ArrayList<String> getAttendees(){
        return attendeesEmailList;
    }

}
