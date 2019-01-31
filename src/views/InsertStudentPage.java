/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import com.sun.prism.paint.Color;
import static halaqat_honien.Halaqat_Honien.*;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Abdul
 */
public class InsertStudentPage extends javax.swing.JFrame {

    Connection connect = null;
    Statement statement = null;
    PreparedStatement preparedStatement  = null;
    ResultSet resultSet = null;
    
    // when you add new student and his guardian exsist before .. so take the info from All guardian table and make the guardians field are not editable.
    // All guadian table will change the value for this boolean
    public boolean thereIsPreGuardian = false;
    
    /**
     * Creates new form InsertPage
     */
    public InsertStudentPage() {
        initComponents();
        
        try {
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/haneen_halaqat", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        Fname.requestFocusInWindow();
        
        GNameField.setEditable(false);
        GPhoneNumField.setEditable(false);
        
        fillCombo();
    }
    
    private void fillCombo(){
        combo.addItem("");
        combo.addItem("الابتدائي");
        combo.addItem("المتوسط");
        combo.addItem("الثانوي");
    }
    
    
    private void insert(){
        String FN, SN, LN, BDate, MemChap, LLNum, G_name, G_phoneNum;
        int HIndex, Age;
        
        FN = Fname.getText();
        SN = Sname.getText();
        LN = Lname.getText();
        Age = Integer.parseInt(age.getText());
        BDate = BirthDate.getText();
        MemChap = MemCh.getText();
        HIndex = combo.getSelectedIndex();
        LLNum = LandLineNum.getText();
        G_name = GNameField.getText();
        G_phoneNum = GPhoneNumField.getText();
        
        
        String quardianID = "";
        
        if(thereIsPreGuardian == false)
        {
            String gaurdianQuery = "insert into guardian (G_name, Phone_num) values ('"+G_name+"', '"+G_phoneNum+"')"; 
            String getTheQuardianID = "select G_id from guardian order by G_id DESC LIMIT 1"; 
        
            try
            {        
               // create new quardian
               connect.prepareStatement(gaurdianQuery).executeUpdate();


               // get the quardian id which is represents the numbers of quardian (the new quadian will be the last one);
               statement = connect.createStatement();
               resultSet = statement.executeQuery(getTheQuardianID);

               while(resultSet.next())
               {
                  quardianID = resultSet.getString("G_id");
               }

            }
            catch(Exception e)
            {
                 JOptionPane.showMessageDialog(null, e);
            }
            
            
        }
        else
        {
           quardianID = allGuardiansPage.G_id;  // this is id for the selected previous guardian
        }
        
         
        
        String studentQuery = "insert into Student (F_name, S_name, L_name, Age, Birth_date, Landline_num, Memorized_chapters, Halaqah_id, quardian_id) values ('"+FN+"', '"+SN+"', '"+LN+"', '"+Age+"', '"+BDate+"', '"+LLNum+"', '"+MemChap+"', '"+HIndex+"', '"+quardianID+"')";      
           
        try{ 
               Fname.setText("");
               Sname.setText("");
               Lname.setText("");
               age.setText("");
               BirthDate.setText("");
                             
               MemCh.setText("");
               combo.setSelectedIndex(0);
               LandLineNum.setText("");
               GNameField.setText("");
               GPhoneNumField.setText("");      
            
               connect.prepareStatement(studentQuery).executeUpdate();
               
               
               // before any JOptionPane write these lines of code to set the window size and font bigger  
               UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
               JLabel label = new JLabel("تمت إضافة الطالب بنجاح", JLabel.CENTER);
               label.setFont(new Font("Arial", Font.BOLD, 20)); 
               
               JOptionPane.showMessageDialog(null, label, "", JOptionPane.INFORMATION_MESSAGE);
               
            }catch(Exception e)
            {
               JOptionPane.showMessageDialog(null, e);
            }
        
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
    
    
    public void setGuardianInfoFromAllGuardiansTable(String G_n, String G_phone)
    {
       GNameField.setText(G_n);
       GPhoneNumField.setText(G_phone);
       
       if(thereIsPreGuardian == true)
       {
         GNameField.setEditable(false);
         GPhoneNumField.setEditable(false);
       }
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
    
    private boolean isValidLandLine(String n) // must be less than 10 digits
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

        jLabel1 = new javax.swing.JLabel();
        Fname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Sname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Lname = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        age = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        MemCh = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();
        addBtn = new javax.swing.JButton();
        combo = new javax.swing.JComboBox<>();
        BirthDate = new javax.swing.JTextField();
        LandLineNum = new javax.swing.JTextField();
        DFormat = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        GNameField = new javax.swing.JTextField();
        GPhoneNumField = new javax.swing.JTextField();
        choosePreQBtn = new javax.swing.JButton();
        addQBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setText("الاسم الأول :");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 38, 112, 40));

        Fname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Fname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(Fname, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 38, 442, 40));

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("الاسم الثاني :");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 92, 112, 40));

        Sname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Sname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        Sname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SnameActionPerformed(evt);
            }
        });
        getContentPane().add(Sname, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 92, 442, 40));

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel3.setText("الاسم الأخير :");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 150, 129, 40));

        Lname.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        Lname.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(Lname, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 150, 442, 40));

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel4.setText("العمر :");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 203, 68, 39));

        age.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        age.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(age, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 202, 442, 40));

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel5.setText("هاتف المنزل :");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 419, -1, 40));

        MemCh.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        MemCh.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        MemCh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MemChActionPerformed(evt);
            }
        });
        getContentPane().add(MemCh, new org.netbeans.lib.awtextra.AbsoluteConstraints(666, 313, 322, 40));

        jLabel7.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel7.setText("المرحلة الدراسية :");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 366, -1, 40));

        jLabel8.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel8.setText("مقدار الحفظ :");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 313, 129, 40));

        jLabel9.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel9.setText("تاريخ الميلاد (ه) :");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 255, 150, 41));

        backBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        backBtn.setText("رجوع   ");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        getContentPane().add(backBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 667, 162, 51));

        addBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        addBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/accept-icon.png"))); // NOI18N
        addBtn.setText("إضافة  ");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        getContentPane().add(addBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(228, 667, 162, 51));

        combo.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        getContentPane().add(combo, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 366, 442, 40));

        BirthDate.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        BirthDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(BirthDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 255, 442, 40));

        LandLineNum.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        LandLineNum.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(LandLineNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 419, 442, 40));

        DFormat.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        DFormat.setForeground(new java.awt.Color(204, 204, 204));
        DFormat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        DFormat.setText("YYYY-MM-DD");
        getContentPane().add(DFormat, new org.netbeans.lib.awtextra.AbsoluteConstraints(439, 256, -1, 40));

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("جزء /  أجزاء");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 318, 108, -1));

        jLabel10.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("اسم ولي الأمر :");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 535, -1, 40));

        jLabel11.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("جوال ولي الأمر :");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(1019, 588, -1, 40));

        GNameField.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        GNameField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        GNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GNameFieldActionPerformed(evt);
            }
        });
        getContentPane().add(GNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 535, 442, 40));

        GPhoneNumField.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        GPhoneNumField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(GPhoneNumField, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 588, 442, 40));

        choosePreQBtn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        choosePreQBtn.setText("اختيار ولي أمر سابق");
        choosePreQBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePreQBtnActionPerformed(evt);
            }
        });
        getContentPane().add(choosePreQBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(775, 477, 213, 40));

        addQBtn.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        addQBtn.setText("إضافة بيانات ولي أمر جديد");
        addQBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addQBtnActionPerformed(evt);
            }
        });
        getContentPane().add(addQBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(546, 477, 217, 40));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MemChActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MemChActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MemChActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        
        Fname.setText("");
        Sname.setText("");
        Lname.setText("");
        age.setText("");
        BirthDate.setText("");
        
        MemCh.setText("");
        combo.setSelectedIndex(0);
        LandLineNum.setText("");
        GNameField.setText("");
        GPhoneNumField.setText("");
        
        
        if(LoginPage.pageNumber == 1)
           mainPage.setVisible(true);
        else if (LoginPage.pageNumber == 0)
           subMainPage.setVisible(true);
           
        
        
        insertPage.setVisible(false);
    }//GEN-LAST:event_backBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
     
        boolean validFormat = true;
        
        try{
            
            // get all data form feild to validate
                String FN = Fname.getText();
                String SN = Sname.getText();
                String LN = Lname.getText();
                String Age = age.getText();
                String BDate = BirthDate.getText();
                String memCh = MemCh.getText();
                String LLNum = LandLineNum.getText();  
                String GN = GNameField.getText();
                String GPhoneNum = GPhoneNumField.getText();
                
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
                                    JLabel label = new JLabel("تأكيد الإضافة ؟", JLabel.CENTER);
                                    label.setFont(new Font("Arial", Font.BOLD, 20)); 

                                     int yesOrNo = JOptionPane.showConfirmDialog(null, label, "Error", JOptionPane.YES_NO_OPTION);
                                     if(yesOrNo == 0)
                                     {
                                           insert();
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
                 JLabel label = new JLabel("البيانات غير مكتملة !!", JLabel.CENTER);
                 label.setFont(new Font("Arial", Font.BOLD, 20));
                
                 JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
            }
            

        }catch(Exception e){
   
          // before any JOptionPane write these lines of code to set the window size and font bigger  
          UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
          JLabel label = new JLabel("البيانات غير صحيحية !!", JLabel.CENTER);
          label.setFont(new Font("Arial", Font.BOLD, 20)); 
            
          JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_addBtnActionPerformed

    private void SnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SnameActionPerformed

    
    private void choosePreQBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePreQBtnActionPerformed
        
        insertPage.setEnabled(false);
 
        allGuardiansPage.setVisible(true);
        
        allGuardiansPage.displayGuardiansOnJTable();
     
    }//GEN-LAST:event_choosePreQBtnActionPerformed

    private void GNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_GNameFieldActionPerformed

    private void addQBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addQBtnActionPerformed
        
        GNameField.setText("");
        GNameField.setEditable(true);
        GNameField.requestFocusInWindow();
        
        GPhoneNumField.setText("");
        GPhoneNumField.setEditable(true);
        
        thereIsPreGuardian = false;
    }//GEN-LAST:event_addQBtnActionPerformed

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
            java.util.logging.Logger.getLogger(InsertStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InsertStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InsertStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InsertStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InsertStudentPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField BirthDate;
    private javax.swing.JLabel DFormat;
    private javax.swing.JTextField Fname;
    private javax.swing.JTextField GNameField;
    private javax.swing.JTextField GPhoneNumField;
    private javax.swing.JTextField LandLineNum;
    private javax.swing.JTextField Lname;
    private javax.swing.JTextField MemCh;
    private javax.swing.JTextField Sname;
    private javax.swing.JButton addBtn;
    private javax.swing.JButton addQBtn;
    private javax.swing.JTextField age;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton choosePreQBtn;
    private javax.swing.JComboBox<String> combo;
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
    // End of variables declaration//GEN-END:variables
}
