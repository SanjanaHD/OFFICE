import com.mongodb.DBCursor;

import java.net.UnknownHostException;

public class Phonebook {
    DBManager dbm;
    Phonebook(){
        dbm = new DBManager();
    }

    Address createAddress(String propNo, String streetName, String aptNo, String city, String state){
        Address newAdd = new Address();

        newAdd.setPropNo(propNo);
        newAdd.setStreetName(streetName);
        newAdd.setApartmentNo(aptNo);
        newAdd.setCity(city);
        newAdd.setState(state);
        return newAdd;
    }

    void storeInPhonebook(String title, String firstname, String lastname, String email, String skypeid, String company, String managername, String emergencyno, String phone, String extn, Address newAdd){
        Contact newContact = new Contact();
        newContact.setTitle(title);
        newContact.setFirstname(firstname);
        newContact.setLastname(lastname);
        newContact.setEmail(email);
        newContact.setSkypeid(skypeid);
        newContact.setCompany(company);
        newContact.setManagername(managername);
        newContact.setEmergencyno(emergencyno);
        newContact.setPhone(phone);
        newContact.setExtn(extn);
        newContact.setAddress(newAdd);

        dbm.mongoContactInsert(newContact);
    }

    void storeInPhonebook(String title, String firstname, String lastname, String email, String skypeid, String company, String managername, String emergencyno, String phone, String extn, String propNo, String streetName, String aptNo, String city, String state){
        Address add = new Address();
        add.createAddress(propNo, streetName, aptNo, city, state);

        Contact newContact = new Contact();
        newContact.setTitle(title);
        newContact.setFirstname(firstname);
        newContact.setLastname(lastname);
        newContact.setEmail(email);
        newContact.setSkypeid(skypeid);
        newContact.setCompany(company);
        newContact.setManagername(managername);
        newContact.setEmergencyno(emergencyno);
        newContact.setPhone(phone);
        newContact.setExtn(extn);
        newContact.setAddress(add);

        dbm.mongoContactInsert(newContact);
    }

    void updatePhonebookById(String id, String title, String firstname, String lastname, String email, String skypeid, String company, String managername, String emergencyno, String phone, String extn, Address add){
//        Address add = new Address();
//        add.createAddress(propNo, streetName, aptNo, city, state);

        Contact newContact = new Contact();
        newContact.setTitle(title);
        newContact.setFirstname(firstname);
        newContact.setLastname(lastname);
        newContact.setEmail(email);
        newContact.setSkypeid(skypeid);
        newContact.setCompany(company);
        newContact.setManagername(managername);
        newContact.setEmergencyno(emergencyno);
        newContact.setPhone(phone);
        newContact.setExtn(extn);
        newContact.setAddress(add);

        dbm.mongoContactUpdate(newContact, id);
    }

    DBCursor lookupPhonebook(String firstname, String lastname, String company) throws UnknownHostException{
        return dbm.mongoContactSearch(firstname, lastname, company);
    }

    public void removeFromPhonebook(String id) throws UnknownHostException{
        dbm.mongoContactDelete(id);
    }


}
