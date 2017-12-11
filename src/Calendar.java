import com.mongodb.*;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Calendar {
    DBManager dbm;
    Calendar(){
        dbm  = new DBManager();
    }

    void scheduleMeetingInCalendar(String topic, String notes, ArrayList<String> emailsAddrToAddToMeeting, LocalDate ld) throws UnknownHostException{

        Meeting meetingEvent = new Meeting();
        meetingEvent.setTopic(topic);
        meetingEvent.setMeetingDate(ld);
        meetingEvent.setNotes(notes);
        meetingEvent.setAttendees(emailsAddrToAddToMeeting);

        dbm.mongoMeetingInsert(meetingEvent);
    }

    DBCursor getAllMeetingsInCalendar() throws UnknownHostException {
        DBCursor dbc = dbm.mongoMeetingsRetrieve();
        return dbc;
    }

    Meeting getMeetingFromCalendar(String id){
        Meeting m = dbm.mongoGetMeeting(id);
        return m;
    }

    void removeMeetingFromCalendar(String id){
        dbm.mongoMeetingDelete(id);
    }

    void updateMeetingInCalendar(String id , Meeting newMeetingEvent){
        dbm.mongoMeetingUpdate(id, newMeetingEvent);
    }

    ArrayList<String> getPeople(ArrayList<String> id) throws UnknownHostException{
        ArrayList<String> names = dbm.getPeopleFromEmailList(id);
        return names;
    }

}
