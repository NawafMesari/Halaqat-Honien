/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static halaqat_honien.Halaqat_Honien.*;
import halaqat_honien.Student;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Abdul
 */
public class UpdateStudentPage extends javax.swing.JFrame {

    Connection connect = null;
    Statement statement = null;
    PreparedStatement preparedStatement  = null;
    ResultSet resultSet = null;
    
    int colomsNumberOnJTable = 12;
    
   
    // DefaultTableModel model = new DefaultTableModel();
    /* Note : before you declare the model from class (DefaultTableModel) as above line .. override the function isCellEditable to make all cells unEditable   
              after that set the model of JTable to this model (in the constructor) */
    // --------------------------------------------------------------
    DefaultTableModel model = new DefaultTableModel() 
    {
        @Override
        public boolean isCellEditable(int row, int column) 
        {
           //all cells false
           return false;
        } 
    };
    // --------------------------------------------------------------
    
    
    /**
     * Creates new form mainPage
     */
    
    public UpdateStudentPage() {
        initComponents();
        
        try {
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/haneen_halaqat", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
      
        
        
        studentsTable.setModel(model);
        
        
        fillCombo();
        setColomnsNamesOnTable();
        
    }

    
    
    private void removeDataFields(){
        Fname.setText("");
        Sname.setText("");
        Lname.setText("");
        age.setText("");
        BirthDate.setText("");
        MemCh.setText("");
        combo.setSelectedIndex(0);
        LandLineNum.setText("");
        guardianName.setText("");
        guardianPhoneNum.setText("");
    }
    
    private void fillCombo(){
            comboBox.addItem("كل الطلاب");
            comboBox.addItem("الابتدائي");
            comboBox.addItem("المتوسط");
            comboBox.addItem("الثانوي");
            
            combo.addItem("");
            combo.addItem("الابتدائي");
            combo.addItem("المتوسط");
            combo.addItem("الثانوي");
    }
    
    private void showStudents(){
        try{   
            String q = null;
            int selected = comboBox.getSelectedIndex();
            switch(selected){
                case 0:
                    q = "select * from Student where Flag=1";
                    break;
                case 1:
                    q = "select * from Student where Halaqah_id=1 and Flag=1";
                    break;
                case 2:
                    q = "select * from Student where Halaqah_id=2 and Flag=1";
                    break;
                case 3:
                    q = "select * from Student where Halaqah_id=3 and Flag=1";
                    break;
            }
            
            preparedStatement = connect.prepareStatement(q);
            resultSet = preparedStatement.executeQuery();
            
            studentsTable.setModel(DbUtils.resultSetToTableModel(resultSet));
                      
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e); 
        }
        
    }  
    
    
    private String getHalaqaNameOfHalaqaId(int H_id){
        
        String q = "select H_name from Halaqat where H_id="+H_id;
        String name = "";
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(q);
            while(resultSet.next())
            {
              name = resultSet.getString("H_name");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return name;
    }
    
    private ArrayList<Student> getStudentsList(String query){
        
        ArrayList<Student> studentsList = new ArrayList<Student>();
        
        
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            Student student;
            while(resultSet.next()){
                student = new Student(resultSet.getInt("S_id"), resultSet.getString("F_name"), resultSet.getString("S_name"), resultSet.getString("L_name"), resultSet.getInt("Age"), resultSet.getDate("Birth_date"), resultSet.getString("Landline_num"), resultSet.getInt("Memorized_chapters"), resultSet.getInt("Halaqah_id"), resultSet.getInt("quardian_id"),resultSet.getBoolean("Flag")); 
                studentsList.add(student);
            }
            
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        return studentsList;
    }
    
    
    private String guardianNameOfID(int id){
        
        String q = "select G_name from guardian where G_id="+id;
        String name = "";
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(q);
            while(resultSet.next())
            {
              name = resultSet.getString("G_name");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return name;   
    }
    
    
    private String quardianPhoneNumOfID(int id){
        
        String q = "select Phone_num from guardian where G_id="+id;
        String phoneNum = "";
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(q);
            while(resultSet.next())
            {
              phoneNum = resultSet.getString("Phone_num");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return phoneNum;   
        
    }
    
    private String studentStatus(boolean Flag){
        
       if(Flag == true)
           return "موجود";
       else
           return "محذوف";
    }
    
    
    // set header colomns names in JTable
    private void setColomnsNamesOnTable(){
    
        Object [] TableColomnNames = new Object[colomsNumberOnJTable];
        
         TableColomnNames[11] = "الرقم";
         TableColomnNames[10] = "الاسم الأول";
         TableColomnNames[9] = "الاسم الثاني";
         TableColomnNames[8] = "الاسم الأخير";
         TableColomnNames[7] = "العمر";
         TableColomnNames[6] = "تاريخ الميلاد";
         TableColomnNames[5] = "مقدار الحفظ";
         TableColomnNames[4] = "هاتف المنزل";    
         TableColomnNames[3] = "اسم الحلقة";
         TableColomnNames[2] = "حالة الطالب";
         TableColomnNames[1] = "اسم ولي الأمر";
         TableColomnNames[0] = "جوال ولي الأمر";
         
         
         model.setColumnIdentifiers(TableColomnNames);
         studentsTable.setModel(model);
         
         studentsTable.getTableHeader().setBackground(Color.LIGHT_GRAY);   
         
         studentsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
         studentsTable.setFont(new Font("Serif", Font.PLAIN, 20));
    }
    
    // Display Students on JTable in GUI
    private void displayStudentsOnJTable(){
        
        String query = null;
        
            int selected = comboBox.getSelectedIndex();
            switch(selected){
                case 0:
                    query = "select * from Student where Flag=1";
                    break;
                case 1:
                    query = "select * from Student where Halaqah_id=1 and Flag=1";
                    break;
                case 2:
                    query = "select * from Student where Halaqah_id=2 and Flag=1";
                    break;
                case 3:
                    query = "select * from Student where Halaqah_id=3 and Flag=1";
                    break;
            }
                      
        ArrayList<Student> list = getStudentsList(query);
        
        
        ///////////////////////////////////////////////////////////////////////////////////////  to show data on each colom on right
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        for(int i=0; i<colomsNumberOnJTable; i++)
        {
          studentsTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        
        
        Object [] rowItem = new Object[colomsNumberOnJTable];
        for(int i=0; i<list.size(); i++)
        {
            
            rowItem[11] = list.get(i).getId();
            rowItem[10] = list.get(i).getF_name();
            rowItem[9] = list.get(i).getS_name();
            rowItem[8] = list.get(i).getL_name();
            rowItem[7] = list.get(i).getAge();
            rowItem[6] = list.get(i).getBirthDate().toString();
            rowItem[5] = list.get(i).getMemChapter();       
            rowItem[4] = list.get(i).getLandLine();

            rowItem[3] = getHalaqaNameOfHalaqaId(list.get(i).getHalaqa_id());

            rowItem[2] = studentStatus(list.get(i).getFlag());

            rowItem[1] = guardianNameOfID(list.get(i).getGuardian_id());
            rowItem[0] = quardianPhoneNumOfID(list.get(i).getGuardian_id());
            
          model.addRow(rowItem);
        }
        
    }
    
    private void deleteOldDateOnJTable(){
        
        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i);  
        }
    }
    
    private void excuteQuery(String q){
       
        try{
            
            connect.prepareStatement(q).executeUpdate();
            
        }catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, e.getStackTrace());
        }

    }
    
    private void updateInfo(){
          String query = null;
          
        int column = 11;  
        int row = studentsTable.getSelectedRow();
        int id = Integer.parseInt( studentsTable.getValueAt(row, column).toString() );
        
        String FN = Fname.getText();
        query = "update student set F_name='"+FN+"' where S_id="+id;
        excuteQuery(query);
        
        String SN = Sname.getText();
        query = "update student set S_name='"+SN+"' where S_id="+id;
        excuteQuery(query);
        
        String LN = Lname.getText();
        query = "update student set L_name='"+LN+"' where S_id="+id;
        excuteQuery(query);
        
        int Age = Integer.parseInt(age.getText().toString());
        query = "update student set Age="+Age+" where S_id="+id;
        excuteQuery(query);
        
        String BDate = BirthDate.getText().toString();
        query = "update student set Birth_date='"+BDate+"' where S_id="+id;
        excuteQuery(query);
        
        int MemChap = Integer.parseInt(MemCh.getText().toString());
        query = "update student set Memorized_chapters="+MemChap+" where S_id="+id;
        excuteQuery(query);
        
        int halaqaID = combo.getSelectedIndex();
        query = "update student set Halaqah_id="+halaqaID+" where S_id="+id;
        excuteQuery(query);
        
        String LLN = LandLineNum.getText();
        query = "update student set Landline_num='"+LLN+"' where S_id="+id;
        excuteQuery(query);
        
        String getGaudrdianID = "select quardian_id from student where S_id="+id;
        String gard_id = "";
                
        try{    
               statement = connect.createStatement();
               resultSet = statement.executeQuery(getGaudrdianID);
               
               while(resultSet.next())
               {
                  gard_id = resultSet.getString("quardian_id");
               }
               
            }catch(Exception e)
            {
               JOptionPane.showMessageDialog(null, e);
            }
        
       
        String GName = guardianName.getText();
        query = "update guardian set G_name='"+GName+"' where G_id="+gard_id;
        excuteQuery(query);
        
        String GPhoneNum = guardianPhoneNum.getText();
        query = "update guardian set Phone_num='"+GPhoneNum+"' where G_id="+gard_id;
        excuteQuery(query);
           
    }
    
    
    private void fillDataFields(){
        
        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
        int selectedRow = studentsTable.getSelectedRow();
        
        Fname.setText(model.getValueAt(selectedRow, 10).toString());
        Sname.setText(model.getValueAt(selectedRow, 9).toString());
        Lname.setText(model.getValueAt(selectedRow, 8).toString());
        age.setText(model.getValueAt(selectedRow, 7).toString());
        BirthDate.setText(model.getValueAt(selectedRow, 6).toString());
        MemCh.setText(model.getValueAt(selectedRow, 5).toString());
        
        LandLineNum.setText(model.getValueAt(selectedRow, 4).toString());
        
        String level = model.getValueAt(selectedRow, 3).toString();
        if(level.equals("الابتدائي"))
          combo.setSelectedIndex(1);
        else if(level.equals("المتوسط"))
          combo.setSelectedIndex(2);
        else if(level.equals("الثانوي"))
          combo.setSelectedIndex(3);  
          
        guardianName.setText(model.getValueAt(selectedRow, 1).toString());
        guardianPhoneNum.setText(model.getValueAt(selectedRow, 0).toString());   
    }
    
    
    private boolean isCompleteInput(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, int SelectedCombo)
    {    
        if(s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("") || s5.equals("") || s6.equals("") 
           || s7.equals("")|| s8.equals("")|| s9.equals("") || (SelectedCombo == 0) )
        {
          return false;   
        }
        
        return true;
    }
    
    private boolean isValidAge(int age){
        
        if(age < 5 || age > 25)
            return false;
        
        return true;
    }
    
    private boolean isValidMemCh(int memorized_ch){
        
        if(memorized_ch < 0 || memorized_ch > 30)
            return false;
        
        return true;
    }
    
    private boolean isValidDateFormat(String d){
        
        String datePattern1 = "\\d{4}-\\d{1}-\\d{1}";      
        String datePattern2 = "\\d{4}-\\d{1}-\\d{2}";
        String datePattern3 = "\\d{4}-\\d{2}-\\d{1}";
        String datePattern4 = "\\d{4}-\\d{2}-\\d{2}";
                
        String datePattern5 = "\\d{4}/\\d{1}/\\d{1}";
        String datePattern6 = "\\d{4}/\\d{1}/\\d{2}";
        String datePattern7 = "\\d{4}/\\d{2}/\\d{1}";
        String datePattern8 = "\\d{4}/\\d{2}/\\d{2}";

        
        String date = d;
               
                Boolean isDate = date.matches(datePattern1) || date.matches(datePattern2) || date.matches(datePattern3) || date.matches(datePattern4)
                              || date.matches(datePattern5) || date.matches(datePattern6) || date.matches(datePattern7) || date.matches(datePattern8);    
          
        return isDate;        
    }
    
    public boolean isValidDate(String date)
    {
      String dateParts [] = new String[3];
      
      if(date.contains("/"))
      {
         dateParts = date.split("/");
      }
      else if(date.contains("-"))
      {
         dateParts = date.split("-");
      }
      
      int year = Integer.parseInt(dateParts[0]);
      int month = Integer.parseInt(dateParts[1]);
      int day = Integer.parseInt(dateParts[2]);
      
      if(year > 1434 || year < 1414) // Year       // the age must be between 5 - 25  ..  beacause the student must be either elementay or intermediate or high (according the requirment)
      {
        return false;
      }
      
      if(month < 1 || month > 12)  // Month
      {
        return false;
      }
      
      if(day < 1 || day > 30)  // Day
      {
        return false;
      }
      
      return true;
    }
    
    private boolean isValidLandLine(String n)
    {
      if(n.length() > 10)
          return false;
      else
          return true;  
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        exitBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        studentsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        referechBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        Fname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Sname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Lname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        age = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        BirthDate = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        MemCh = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        combo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        LandLineNum = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        DFormat = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        guardianName = new javax.swing.JTextField();
        guardianPhoneNum = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1200, 700));

        exitBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        exitBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        exitBtn.setText("رجوع   ");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        studentsTable.setFont(new java.awt.Font("Tahoma", 0, 18));
        studentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }

        ));
        studentsTable.setRowHeight(30);
        studentsTable.setRowMargin(3);
        studentsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(studentsTable);

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("اختر الحلقة :");

        comboBox.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActionPerformed(evt);
            }
        });

        referechBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        referechBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        referechBtn.setText("تحديث  ");
        referechBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                referechBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel3.setText("الاسم الأول :");

        Fname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Fname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("الاسم الثاني :");

        Sname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Sname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Sname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnameActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel4.setText("الاسم الأخير :");

        Lname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Lname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel5.setText("العمر :");

        age.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        age.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        age.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel9.setText("تاريخ الميلاد :");

        BirthDate.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        BirthDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        BirthDate.setToolTipText("yyyy-mm-dd");
        BirthDate.setName(""); // NOI18N

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel8.setText("مقدار الحفظ :");

        MemCh.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        MemCh.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        MemCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MemChActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel7.setText("المرحلة الدراسية :");

        combo.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel6.setText("هاتف المنزل :");

        LandLineNum.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        LandLineNum.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jButton2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/floppy-icon.png"))); // NOI18N
        jButton2.setText("حفظ  ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        DFormat.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        DFormat.setForeground(new java.awt.Color(204, 204, 204));
        DFormat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DFormat.setText("YYYY-MM-DD");

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel10.setText("اسم ولي الأمر :");

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel11.setText("جوال ولي الأمر :");

        guardianName.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        guardianName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        guardianPhoneNum.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        guardianPhoneNum.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(referechBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1408, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1028, 1028, 1028))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(guardianPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(age, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(34, 34, 34)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(Lname, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(LandLineNum, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel6)
                                    .addGap(22, 22, 22)
                                    .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel4))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(MemCh, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(Sname, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(20, 20, 20))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(34, 34, 34)
                                        .addComponent(guardianName, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel10)
                                        .addGap(9, 9, 9)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(BirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(Fname, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(DFormat)
                                .addGap(165, 165, 165)))))
                .addGap(0, 34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(referechBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Fname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Sname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Lname, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(age, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(BirthDate, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(MemCh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(combo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(LandLineNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(guardianName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(guardianPhoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
       
       removeDataFields();
       
       deleteOldDateOnJTable();
       comboBox.setSelectedIndex(0);
       
       if(LoginPage.pageNumber == 1)
           mainPage.setVisible(true);
        else if (LoginPage.pageNumber == 0)
           subMainPage.setVisible(true);
       
       updatePage.setVisible(false);
    }//GEN-LAST:event_exitBtnActionPerformed

  //  boolean RefereshClicked = false;
    private void referechBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_referechBtnActionPerformed
        
        removeDataFields();
        
  //      if(RefereshClicked)
           deleteOldDateOnJTable();
        
        displayStudentsOnJTable();
          
  //      RefereshClicked = true;
    }//GEN-LAST:event_referechBtnActionPerformed

    private void SnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SnameActionPerformed

    private void MemChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MemChActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MemChActionPerformed

    private void studentsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentsTableMouseClicked

        fillDataFields();
    }//GEN-LAST:event_studentsTableMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try
        {
            if(studentsTable.getSelectedRow() != -1)
            {                  
                // get all data form feild to validate
                String FN = Fname.getText();
                String SN = Sname.getText();
                String LN = Lname.getText();
                String Age = age.getText();
                String BDate = BirthDate.getText();
                String memCh = MemCh.getText();
                String LLNum = LandLineNum.getText();  
                String GN = guardianName.getText();
                String GPhoneNum = guardianPhoneNum.getText();
                int selectedCombo = combo.getSelectedIndex();
                
                
                if(isCompleteInput(FN, SN, LN, Age, BDate, memCh, LLNum, GN, GPhoneNum, selectedCombo))
                {    
                    int S_age = Integer.parseInt(age.getText());
                    int S_memCh = Integer.parseInt(MemCh.getText());
                    
                    if(isValidAge(S_age))
                    {
                        if(isValidMemCh(S_memCh)) 
                        {   
                            if(isValidLandLine(LLNum))
                            {
                                if(isValidDateFormat(BirthDate.getText()))
                                {   
                                   if(isValidDate(BirthDate.getText()))
                                   {
                                        // before any JOptionPane write these lines of code to set the window size and font bigger  
                                        UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                                        JLabel label = new JLabel("تأكيد التحديث ؟", JLabel.CENTER);
                                        label.setFont(new Font("Arial", Font.BOLD, 20));

                                        int yesOrNo = JOptionPane.showConfirmDialog(null, label, "", JOptionPane.YES_NO_OPTION);
                                        if(yesOrNo == 0)
                                        { 
                                            updateInfo();

                                            ////////////////////////////////////// from referesh button
                                            deleteOldDateOnJTable();
                                            removeDataFields();
                                            displayStudentsOnJTable();
                                            ///////////////////////////////////////

                                            // before any JOptionPane write these lines of code to set the window size and font bigger  
                                            UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                                            label = new JLabel("تم تحديث البيانات بنجاح", JLabel.CENTER);
                                            label.setFont(new Font("Arial", Font.BOLD, 20));

                                            JOptionPane.showMessageDialog(null, label, "", JOptionPane.INFORMATION_MESSAGE);
                                        }
                                   }
                                   else
                                   {
                                        // before any JOptionPane write these lines of code to set the window size and font bigger  
                                        UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                                        JLabel label = new JLabel("خطأ في التاريخ !! \n ملاحظة يجب أن يكون العمر بين ( 5 - 25 )", JLabel.CENTER);
                                        label.setFont(new Font("Arial", Font.BOLD, 20));

                                        JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                                        DFormat.setForeground(java.awt.Color.RED);
                                   }
                                }
                                else
                                {
                                    // before any JOptionPane write these lines of code to set the window size and font bigger  
                                    UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                                    JLabel label = new JLabel("خطأ في شكل التاريخ !!", JLabel.CENTER);
                                    label.setFont(new Font("Arial", Font.BOLD, 20));

                                    JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                                    DFormat.setForeground(java.awt.Color.RED);
                                }
                            }
                            else
                            {
                                  
                                  // before any JOptionPane write these lines of code to set the window size and font bigger  
                                  UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                                  JLabel label = new JLabel("خطأ في رقم الهاتف ، يجب أن يكون أقل من عشرة أرقام  !!", JLabel.CENTER);
                                  label.setFont(new Font("Arial", Font.BOLD, 20));
                                
                                  JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                                  DFormat.setForeground(java.awt.Color.RED);
                            }
                        }
                        else
                        {
                            
                          // before any JOptionPane write these lines of code to set the window size and font bigger  
                          UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                          JLabel label = new JLabel("خطأ في مقدار الحفظ !!", JLabel.CENTER);
                          label.setFont(new Font("Arial", Font.BOLD, 20));  
                            
                          JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else
                    {
                      
                      // before any JOptionPane write these lines of code to set the window size and font bigger  
                      UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                      JLabel label = new JLabel("!! خطأ في العمر , يجب أن يكون العمر بين ( 5 - 25 )", JLabel.CENTER);
                      label.setFont(new Font("Arial", Font.BOLD, 20));  
                        
                      JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                    
                    // before any JOptionPane write these lines of code to set the window size and font bigger  
                    UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                    JLabel label = new JLabel("مدخلات غير مكتملة !!", JLabel.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 20)); 
                    
                    JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                
                // before any JOptionPane write these lines of code to set the window size and font bigger  
                UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                JLabel label = new JLabel("الرجاء اختيار طالب !!", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 20)); 
                
                JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch(Exception e){
            
                // before any JOptionPane write these lines of code to set the window size and font bigger  
                UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                JLabel label = new JLabel("مدخلات غير صحيحة !!", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 20)); 
            
                 JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxActionPerformed

    private void ageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ageActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UpdateStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateStudentPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BirthDate;
    private javax.swing.JLabel DFormat;
    private javax.swing.JTextField Fname;
    private javax.swing.JTextField LandLineNum;
    private javax.swing.JTextField Lname;
    private javax.swing.JTextField MemCh;
    private javax.swing.JTextField Sname;
    private javax.swing.JTextField age;
    private javax.swing.JComboBox<String> combo;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JButton exitBtn;
    private javax.swing.JTextField guardianName;
    private javax.swing.JTextField guardianPhoneNum;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton referechBtn;
    private javax.swing.JTable studentsTable;
    // End of variables declaration//GEN-END:variables
}
