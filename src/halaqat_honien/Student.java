package halaqat_honien;

import java.sql.Date;


public class Student {
    
    private int id;
    private String F_name;
    private String S_name;
    private String L_name;
    private int age;
    private Date birthDate;
    private String landLine;
    private int memChapter;
    private int halaqa_id;
    private int guardian_id;
    private boolean flag;

    public Student(int id, String F_name, String S_name, String L_name, int age, Date birthDate, String landLine, int memChapter, int halaqa_id,int guardian_id, boolean flag) {
        this.id = id;
        this.F_name = F_name;
        this.S_name = S_name;
        this.L_name = L_name;
        this.age = age;
        this.birthDate = birthDate;
        this.landLine = landLine;
        this.memChapter = memChapter;
        this.halaqa_id = halaqa_id;
        this.guardian_id = guardian_id;
        this.flag = flag;
    }
    
    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setF_name(String F_name) {
        this.F_name = F_name;
    }

    public void setS_name(String S_name) {
        this.S_name = S_name;
    }

    public void setL_name(String L_name) {
        this.L_name = L_name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setLandLine(String landLine) {
        this.landLine = landLine;
    }

    public void setMemChapter(int memChapter) {
        this.memChapter = memChapter;
    }

    public void setHalaqa_id(int halaqa_id) {
        this.halaqa_id = halaqa_id;
    }

    public void setGuardian_id(int guardian_id) {
        this.guardian_id = guardian_id;
    }
    
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    // Getters
    
    public int getId() {
        return id;
    }

    public String getF_name() {
        return F_name;
    }

    public String getS_name() {
        return S_name;
    }

    public String getL_name() {
        return L_name;
    }

    public int getAge() {
        return age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getLandLine() {
        return landLine;
    }

    public int getMemChapter() {
        return memChapter;
    }

    public int getHalaqa_id() {
        return halaqa_id;
    }

    public int getGuardian_id() {
        return guardian_id;
    }
    
    public boolean getFlag() {
        return flag;
    }
   
    
    public void print() {
        
        System.out.println("id = " + getId());
        System.out.println("first name = " + getF_name());
        System.out.println("second name = " + getS_name());
        System.out.println("last name = " + getL_name());
        System.out.println("age = " + getAge());
        System.out.println("birth date = " + getBirthDate().toString());
        System.out.println("land line number = " + getLandLine());
        System.out.println("memorized chapter = " + getMemChapter());
        System.out.println("halaqa id = " + getHalaqa_id());
        System.out.println("guardian id = " + getGuardian_id());
        System.out.println("flag = " + getFlag());
        System.out.println();
    }
    
}
