import com.mongodb.*;
import org.bson.types.ObjectId;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBManager {

    Boolean mongoLogin(String usernameEntered, String passwordEntered, Boolean isEmployee) throws UnknownHostException{

        MongoClient mongo = new MongoClient("localhost", 27017);
        DB mydb = mongo.getDB("mydb");

        if (isEmployee == true){
            DBCollection authcoll = mydb.getCollection("employeeAuth");
            BasicDBObject authObj = new BasicDBObject("username", usernameEntered);
            authObj.put("password", passwordEntered);
            DBCursor authCursor = authcoll.find(authObj);
            if (authCursor.size() == 1){
                return true;
            } else {
                return false;
            }
        } else {
            DBCollection authcoll = mydb.getCollection("auth");
            BasicDBObject authObj = new BasicDBObject("username", usernameEntered);
            authObj.put("password", passwordEntered);
            DBCursor authCursor = authcoll.find(authObj);
            if (authCursor.size() == 1){
                return true;
            } else {
                return false;
            }
        }
    }

    void mongoContactInsert(Contact newContact){
        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB mydb = mongo.getDB("mydb");
            DBCollection coll = mydb.getCollection("contacts");

            BasicDBObject newContactDoc = new BasicDBObject();
            newContactDoc.put("title", newContact.getTitle());
            newContactDoc.put("firstname", newContact.getFirstname());
            newContactDoc.put("lastname", newContact.getLastname());
            newContactDoc.put("email", newContact.getEmail());
            newContactDoc.put("skypeid", newContact.getSkypeid());
            newContactDoc.put("company", newContact.getCompany());
            newContactDoc.put("managername", newContact.getManagername());
            newContactDoc.put("emergencynum", newContact.getEmergencyno());
            newContactDoc.put("phone", newContact.getPhone());
            newContactDoc.put("extn", newContact.getExtn());
            newContactDoc.put("propno", newContact.getAddress().getPropNo());
            newContactDoc.put("streetname", newContact.getAddress().getStreetName());
            newContactDoc.put("aptno", newContact.getAddress().getApartmentNo());
            newContactDoc.put("city", newContact.getAddress().getCity());
            newContactDoc.put("state", newContact.getAddress().getState());

            coll.insert(newContactDoc);

        }
        catch (Exception uhe){
            System.out.println("UnknownHostException in mongoContactInsert.java ");
        }
    }

    void mongoContactUpdate(Contact newContact, String id){
        try {
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB mydb = mongo.getDB("mydb");
            DBCollection coll = mydb.getCollection("contacts");

            BasicDBObject query = new BasicDBObject().append("_id", new ObjectId(id));

            BasicDBObject update = new BasicDBObject();
            update.put("title", newContact.getTitle());
            update.put("firstname", newContact.getFirstname());
            update.put("lastname", newContact.getLastname());
            update.put("email", newContact.getEmail());
            update.put("skypeid", newContact.getSkypeid());
            update.put("company", newContact.getCompany());
            update.put("managername", newContact.getManagername());
            update.put("emergencynum", newContact.getEmergencyno());
            update.put("phone", newContact.getPhone());
            update.put("extn", newContact.getExtn());

            Address address = newContact.getAddress();
            update.put("propno", address.getPropNo());
            update.put("streetname", address.getStreetName());
            update.put("aptno", address.getApartmentNo());
            update.put("city", address.getCity());
            update.put("state", address.getState());

            coll.update(query, update);

        }
        catch (Exception uhe){
            System.out.println("UnknownHostException in mongoContactInsert.java ");
        }
    }

    DBCursor mongoContactSearch(String firstname, String lastname, String company) throws UnknownHostException {

        MongoClient mongo = new MongoClient("localhost", 27017);
        DB mydb = mongo.getDB("mydb");
        DBCollection coll = mydb.getCollection("contacts");
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        if (!firstname.equals("")){
            obj.add(new BasicDBObject("firstname", firstname));
        }
        if (!lastname.equals("")){
            obj.add(new BasicDBObject("lastname", lastname));
        }
        if (!company.equals("")){
            obj.add(new BasicDBObject("company", company));
        }
        andQuery.put("$and", obj);

        System.out.println(andQuery.toString());

        DBCursor cursor = coll.find(andQuery);

        return cursor;
    }

    void mongoContactDelete(String id) throws UnknownHostException{
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB mydb = mongo.getDB("mydb");
        DBCollection coll = mydb.getCollection("contacts");

        BasicDBObject newContactDoc = new BasicDBObject();
        newContactDoc.put("email", id);
        coll.remove(newContactDoc);
    }

    void mongoMeetingInsert(Meeting newMeetingEvent) throws UnknownHostException {
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB mydb = mongo.getDB("mydb");
        DBCollection coll = mydb.getCollection("meetings");

        BasicDBObject newMeetingDoc = new BasicDBObject();

        LocalDate meetingDate = newMeetingEvent.getMeetingDate();
        String topic = newMeetingEvent.getTopic();
        String notes = newMeetingEvent.getNotes();
        String attendeesEmailString = newMeetingEvent.getAttendees().toString();

        System.out.println(meetingDate.toString());

        newMeetingDoc.put("date", meetingDate.toString());
        newMeetingDoc.put("topic", topic);
        newMeetingDoc.put("notes", notes);
        newMeetingDoc.put("attendeesEmail", attendeesEmailString);

        coll.insert(newMeetingDoc);
    }

    DBCursor mongoMeetingsRetrieve() throws UnknownHostException {
        MongoClient mongo = new MongoClient("localhost", 27017);

        DB mydb = mongo.getDB("mydb");
        DBCollection coll = mydb.getCollection("meetings");

        DBCursor cursor = coll.find();
        return cursor;
    }


    public Meeting mongoGetMeeting(String id){
        Meeting retrievedMeeting = new Meeting();

        DBCursor retCursor;
        try{
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB mydb = mongo.getDB("mydb");
            DBCollection coll = mydb.getCollection("meetings");
            BasicDBObject findMeetingObj = new BasicDBObject("_id", new ObjectId(id));
            retCursor = coll.find(findMeetingObj);
            while (retCursor.hasNext()){
                DBObject foundObj = retCursor.next();

                if (foundObj.containsField("topic")) retrievedMeeting.setTopic(foundObj.get("topic").toString());
                if (foundObj.containsField("notes")) retrievedMeeting.setNotes(foundObj.get("notes").toString());

                if (foundObj.containsField("date")){
                    String dateString = foundObj.get("date").toString();
                    LocalDate ld = LocalDate.parse(dateString);
                    retrievedMeeting.setMeetingDate(ld);
                }

                if (foundObj.containsField("attendeesEmail")){
                    String emails = foundObj.get("attendeesEmail").toString();
                    String emailsModified = emails.substring(1, emails.length()-1);
                    ArrayList<String> emailsList = new ArrayList<>(Arrays.asList(emailsModified.split(",")));
                    retrievedMeeting.setAttendees(emailsList);
                }
            }
        }
        catch (UnknownHostException uhe){
            System.out.println("uhe in mongoMeetingSearch");
        }
        return retrievedMeeting;
    }

    void mongoMeetingDelete(String id){
        try{
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB mydb = mongo.getDB("mydb");
            DBCollection coll = mydb.getCollection("meetings");

            BasicDBObject deleteMeetingObj = new BasicDBObject("_id", new ObjectId(id));
            coll.remove(deleteMeetingObj);

        }
        catch (UnknownHostException uhe){
            System.out.println("uhe in exception");
        }

    }

    String getPersonFromEmail(String email)throws UnknownHostException{
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB mydb = mongo.getDB("mydb");
        DBCollection coll = mydb.getCollection("contacts");
        BasicDBObject emailQuery = new BasicDBObject("email", email);
        String fullname = "";

        DBCursor cursor = coll.find(emailQuery);
        while (cursor.hasNext()){
            DBObject dbo = cursor.next();
            String firstname = dbo.get("firstname").toString();
            String lastname = dbo.get("lastname").toString();
            fullname = firstname + " " + lastname;
        }
        cursor.close();
        return fullname;
    }

    ArrayList<String> getPeopleFromEmailList(ArrayList<String> emailList) throws UnknownHostException{

        ArrayList<String> fullnamesList = new ArrayList<>();

        for (String email : emailList){
            String personfullname = getPersonFromEmail(email);
            fullnamesList.add(personfullname);
        }
        return fullnamesList;
    }

    void mongoMeetingUpdate(String id, Meeting meetingEvent){
        try{
            MongoClient mongo = new MongoClient("localhost", 27017);
            DB mydb = mongo.getDB("mydb");
            DBCollection coll = mydb.getCollection("meetings");

            BasicDBObject findMeetingObj = new BasicDBObject("_id", new ObjectId(id));

            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("topic", meetingEvent.getTopic());
            updateObj.put("notes", meetingEvent.getNotes());
            updateObj.put("date", meetingEvent.getMeetingDate().toString());

            coll.findAndModify(findMeetingObj, updateObj);
        }
        catch(UnknownHostException uhe){

        }
    }
}
