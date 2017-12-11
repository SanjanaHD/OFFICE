import com.mongodb.DBCursor;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MeetingController {
    DBManager dbm;
    Calendar cal = new Calendar();

    MeetingController(){
        dbm = new DBManager();
    }


    void addMeeting(String topic, String notes, ArrayList<String> emailsAddrToAddToMeeting, LocalDate ld) throws UnknownHostException{
        cal.scheduleMeetingInCalendar(topic, notes, emailsAddrToAddToMeeting, ld);

    }

    DBCursor retrieveAllMeetings() throws UnknownHostException{
        DBCursor dbc = cal.getAllMeetingsInCalendar();
        return dbc;
    }

    Meeting getMeeting(String id){
        Meeting m = cal.getMeetingFromCalendar(id);
        return m;
    }

    void deleteMeeting(String id){
        cal.removeMeetingFromCalendar(id);
    }

    ArrayList<String> getPeopleFromEmailList (ArrayList<String> emailList) throws UnknownHostException{
//        ArrayList<String> names = dbm.getPeople(emailList);
//        return names;
        return cal.getPeople(emailList);
    }

    void updateMeetingById(String id , Meeting newMeetingEvent){
        cal.updateMeetingInCalendar(id, newMeetingEvent);
    }
}
