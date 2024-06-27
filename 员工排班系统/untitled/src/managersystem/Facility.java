package managersystem;

class Facility {
    private int id;
    private String name;
    private int facilitySwitch;
    private int staffCount;

    public Facility(int id, String name, int facilitySwitch, int staffCount) {
        this.id = id;
        this.name = name;
        this.facilitySwitch = facilitySwitch;
        this.staffCount = staffCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSwitch() {
        return facilitySwitch;
    }

    public int getStaffCount() {
        return staffCount;
    }
}