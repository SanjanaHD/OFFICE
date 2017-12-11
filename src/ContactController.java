import com.mongodb.DBCursor;

import java.net.UnknownHostException;

public class ContactController {

    DBManager dbm;
    Phonebook ph = new Phonebook();
    ContactController(){
        dbm = new DBManager();
    }

    public void addContact(String title, String firstname, String lastname, String email, String skypeid, String company, String managername, String emergencyno, String phone, String extn, String propNo, String streetName, String aptNo, String city, String state){

        Address newAdd = ph.createAddress(propNo, streetName, aptNo, city, state);
        ph.storeInPhonebook(title, firstname, lastname, email, skypeid, company, managername, emergencyno, phone, extn, newAdd);

    }


    public void updateContact(String id, String title, String firstname, String lastname, String email, String skypeid, String company, String managername, String emergencyno, String phone, String extn, String propNo, String streetName, String aptNo, String city, String state){

        Address newAdd = ph.createAddress(propNo, streetName, aptNo, city, state);
        ph.updatePhonebookById(id, title, firstname, lastname, email, skypeid, company, managername, emergencyno, phone, extn, newAdd);

    }


    public DBCursor searchContacts(String firstname, String lastname, String company) throws UnknownHostException{
        DBCursor dbc = ph.lookupPhonebook(firstname, lastname, company);
        return dbc;

    }

    public void deleteContact(String id) throws UnknownHostException{
        ph.removeFromPhonebook(id);

    }
}
