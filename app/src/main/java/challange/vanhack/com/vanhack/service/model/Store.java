package challange.vanhack.com.vanhack.service.model;

public class Store {
    int id;
    String name;
    String address;
    int cousineId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCousineId() {
        return cousineId;
    }

    public void setCousineId(int cousineId) {
        this.cousineId = cousineId;
    }
}
