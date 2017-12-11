public class Address {
    private String propNo;
    private String streetName;
    private String apartmentNo;
    private String City;
    private String State;

    Address createAddress(String propNo, String streetName, String aptNo, String city, String state){
        Address newAdd = new Address();

        newAdd.setPropNo(propNo);
        newAdd.setStreetName(streetName);
        newAdd.setApartmentNo(aptNo);
        newAdd.setCity(city);
        newAdd.setState(state);
        return newAdd;
    }

    public String getPropNo() {
        return propNo;
    }

    public void setPropNo(String propNo) {
        this.propNo = propNo;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(String apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
