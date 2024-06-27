package managersystem;
public class Staff {
    private int id;
    private String realName;
    private String phoneNumber;
    private String facilityName; // 新增字段

    public Staff(int id, String realName, String phoneNumber, String facilityName) {
        this.id = id;
        this.realName = realName;
        this.phoneNumber = phoneNumber;
        this.facilityName = facilityName;
    }
    public int getId() {
        return id;
    }
    public String getRealName() {
        return realName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getFacilityName() {
        return facilityName;
    }
}
