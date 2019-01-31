
package halaqat_honien;


public class Teacher {
    
    private int id;
    private String name;
    private String phoneNum;
    private int salary;
    private int managerID;
    private boolean flag;

    public Teacher(int id, String name, String phoneNum, int salary, int managerID, boolean flag) {
        this.id = id;
        this.name = name;
        this.phoneNum = phoneNum;
        this.salary = salary;
        this.managerID = managerID;
        this.flag = flag;
    }

    // Setters
    
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    // Getters
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public int getSalary() {
        return salary;
    }

    public int getManagerID() {
        return managerID;
    }

    public boolean getFlag() {
        return flag;
    }
    
    
    
    
}
