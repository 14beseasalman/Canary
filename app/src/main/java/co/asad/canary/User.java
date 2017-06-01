package co.asad.canary;


import com.google.gson.Gson;

public class User {
    private String name;
    private String id;
    private EmergencyContact emergencyContact;

    public User(String name, String Uuid, String emergencyContactName, String emergencyContactNumber,String emergencyContactEmail) {
        this.name = name;
        this.id = Uuid;
        this.emergencyContact = new EmergencyContact(emergencyContactName, emergencyContactNumber,emergencyContactEmail);
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
        EmergencyContact(String name, String number, String email) {
            this.name = name;
            this.number = number;
            this.email = email;
        }

        String name;
        String number;
        String email;
        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }

        public String getEmail() {
            return email;
        }
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
