package co.asad.canary;


import com.google.gson.Gson;

public class User {
    private String name;
    private String id;
    private EmergencyContact emergencyContact;

    public User(String name, String Uuid, String emergencyContactName, String emergencyContactNumber) {
        this.name = name;
        this.id = Uuid;
        this.emergencyContact = new EmergencyContact(emergencyContactName, emergencyContactNumber);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }


    private class EmergencyContact {
        EmergencyContact(String name, String number) {
            this.name = name;
            this.number = number;
        }

        String name;
        String number;

        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
