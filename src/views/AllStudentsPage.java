/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static halaqat_honien.Halaqat_Honien.*;

import halaqat_honien.Student;
import java.awt.Color;
import java.awt.Font;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Abdul
 */
public class AllStudentsPage extends javax.swing.JFrame {

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
     * Creates new form AllStudents
     */
    public AllStudentsPage() {
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
    
    private void fillCombo(){
            comboBox.addItem("كل الطلاب");
            comboBox.addItem("الابتدائي");
            comboBox.addItem("المتوسط");
            comboBox.addItem("الثانوي");      
    }
    
    private ArrayList<Student> getStudentsList(String query){
        
        ArrayList<Student> studentsList = new ArrayList<Student>();
        
        
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            Student student;
            while(resultSet.next()){
                student = new Student(resultSet.getInt("S_id"), resultSet.getString("F_name"), resultSet.getString("S_name"), resultSet.getString("L_name"), resultSet.getInt("Age"), resultSet.getDate("Birth_date"), resultSet.getString("Landline_num"), resultSet.getInt("Memorized_chapters"), resultSet.getInt("Halaqah_id"), resultSet.getInt("quardian_id") ,resultSet.getBoolean("Flag")); 
                studentsList.add(student);
            }
            
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        return studentsList;
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
    
    // Display Students on JTable in GUI
    private void displayStudentsOnJTable()
    {
        
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
        
        ///////////////////////////////////////////////////////////////////////////////////////  to show data on each colom on right side
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        refreshBtn = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        studentsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        backBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        refreshBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        refreshBtn.setText("تحديث  ");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });
        getContentPane().add(refreshBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(801, 42, 180, 50));

        studentsTable.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        studentsTable.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        studentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        studentsTable.setToolTipText("");
        studentsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        studentsTable.setGridColor(new java.awt.Color(204, 204, 204));
        studentsTable.setRowHeight(30);
        studentsTable.setRowMargin(3);
        jScrollPane2.setViewportView(studentsTable);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 123, 1393, 240));

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("اختر الحلقة :");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1348, 48, 101, 38));

        comboBox.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActionPerformed(evt);
            }
        });
        getContentPane().add(comboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(999, 42, 337, 50));

        backBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        backBtn.setText("رجوع   ");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });
        getContentPane().add(backBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 623, 193, 51));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //boolean RefereshClicked = false;
    
    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        //showStudents();
        
       // if(RefereshClicked)
           deleteOldDateOnJTable();
        
        displayStudentsOnJTable();
          
    //    RefereshClicked = true;
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        
        if(LoginPage.pageNumber == 1)
           mainPage.setVisible(true);
        else if (LoginPage.pageNumber == 0)
           subMainPage.setVisible(true);
        
        allStudentsPage.setVisible(false);
        
        deleteOldDateOnJTable();
        comboBox.setSelectedIndex(0);
    }//GEN-LAST:event_backBtnActionPerformed

    private void comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxActionPerformed

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
            java.util.logging.Logger.getLogger(AllStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AllStudentsPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTable studentsTable;
    // End of variables declaration//GEN-END:variables
}
