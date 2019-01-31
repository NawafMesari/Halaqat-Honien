
package halaqat_honien;


import com.mysql.jdbc.Connection;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import javax.swing.JFrame;

import views.*;


public class Halaqat_Honien {

    public static StartPage startPage = new StartPage();
    public static LoginPage loginPage = new LoginPage();
    public static MainPage mainPage = new MainPage();
    public static SubMainPage subMainPage = new SubMainPage();
    public static AllStudentsPage allStudentsPage = new AllStudentsPage();
    public static InsertStudentPage insertPage = new InsertStudentPage();
    public static DeleteStudentPage deletePage = new DeleteStudentPage();
    public static UpdateStudentPage updatePage = new UpdateStudentPage();
    public static AllTeachersPage allTeachersPage = new AllTeachersPage();
    public static InsertTeacherPage insertTeacherPage = new InsertTeacherPage();
    public static DeleteTeacherPage deleteTeacherPage = new DeleteTeacherPage();
    public static UpdateTeacherPage updateTeacherPage = new UpdateTeacherPage();
    public static AllGuardiansPage allGuardiansPage = new AllGuardiansPage();
    public static AllDeletedStudentsPage allDeletedStudentsPage = new AllDeletedStudentsPage();
    public static AllDeletedTeachersPage allDeletedTeachersPage =  new AllDeletedTeachersPage();
    
    static void setPages()
    {
       startPage.setBounds(0, 0, 1500, 880);
       loginPage.setBounds(0, 0, 1000, 600);
       mainPage.setBounds(0, 0, 1500, 880);
       subMainPage.setBounds(0, 0, 1500, 880);
       allStudentsPage.setBounds(0, 0, 1500, 750);
       insertPage.setBounds(0, 0, 1200, 800);
       updatePage.setBounds(0, 0, 1500, 850);
       deletePage.setBounds(0, 0, 1500, 750);
       allTeachersPage.setBounds(0, 0, 1300, 750);
       insertTeacherPage.setBounds(0, 0, 1200, 650);
       deleteTeacherPage.setBounds(0, 0, 1300, 750);
       updateTeacherPage.setBounds(0, 0, 1500, 850);
       allGuardiansPage.setBounds(128, 189, 640, 596);
       allDeletedStudentsPage.setBounds(0, 0, 1500, 750);
       allDeletedTeachersPage.setBounds(0, 0, 1300, 700);
       
       startPage.setLocationRelativeTo(null);
       loginPage.setLocationRelativeTo(null);
       mainPage.setLocationRelativeTo(null);
       subMainPage.setLocationRelativeTo(null);
       allStudentsPage.setLocationRelativeTo(null);
       insertPage.setLocationRelativeTo(null);
       updatePage.setLocationRelativeTo(null);
       deletePage.setLocationRelativeTo(null);
       allTeachersPage.setLocationRelativeTo(null);
       insertTeacherPage.setLocationRelativeTo(null);
       deleteTeacherPage.setLocationRelativeTo(null);
       updateTeacherPage.setLocationRelativeTo(null);
       allDeletedStudentsPage.setLocationRelativeTo(null);
       allDeletedTeachersPage.setLocationRelativeTo(null);
       
       startPage.setResizable(false);
       loginPage.setResizable(false);
       mainPage.setResizable(false);
       subMainPage.setResizable(false);
       allStudentsPage.setResizable(false);
       insertPage.setResizable(false);
       updatePage.setResizable(false);
       deletePage.setResizable(false);
       allTeachersPage.setResizable(false);
       insertTeacherPage.setResizable(false);
       deleteTeacherPage.setResizable(false);
       updateTeacherPage.setResizable(false);
       allDeletedStudentsPage.setResizable(false);
       allDeletedTeachersPage.setResizable(false);
       
    }
    
    static String username = "root";
    static String password = "";
    static String url = "jdbc:mysql://localhost/haneen_halaqat";
    
    static Connection connect = null;
    static Statement statement = null;
    static PreparedStatement preparedStatement  = null;
    static ResultSet resultSet = null;
    
    
    public static void main(String[] args)throws SQLException {
        
        
        // -------------------------------------------------------------------------------------------------
        try{
     
             connect = (Connection) DriverManager.getConnection(url, username, password);
          
//           Write to DB
          
//           connect.prepareStatement("insert into Student (id, name, age) values ('4', 'Fofo', '19')").executeUpdate();
//           connect.prepareStatement("delete from Student where id=4").executeUpdate();
//           connect.prepareStatement("update Student set name='Nawaf' where id=1").executeUpdate();
//          

//           Read from DB
          
//           statement = connect.createStatement();
//           resultSet = statement.executeQuery("select * from Student");
//        
//           while(resultSet.next())
//           {
//              String id = resultSet.getString("S_id");
//              String Fname = resultSet.getString("F_name");
//              String Sname = resultSet.getString("S_name");
//              String Lname = resultSet.getString("L_name");
//              String age = resultSet.getString("Age");
//              System.out.println("id = " + id + ", name = " + Fname + " " + Sname + " " + Lname + ", age = " + age);
//           }
          

        
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        
        // -------------------------------------------------------------------------------------------------
        
        setPages();
        
        startPage.setVisible(true);
        startPage.getContentPane().setBackground(Color.white);
        startPage.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.black));
    }
    
}
