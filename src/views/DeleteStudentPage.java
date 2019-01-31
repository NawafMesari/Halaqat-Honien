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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Abdul
 */
public class DeleteStudentPage extends javax.swing.JFrame {

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
    public DeleteStudentPage() {
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

    
    
    private void displayAllStudents(){
        
        try{
            String q = "select * from Student where Flag=1";
            preparedStatement = connect.prepareStatement(q);
            resultSet = preparedStatement.executeQuery();
            
            studentsTable.setModel(DbUtils.resultSetToTableModel(resultSet));
            
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, e);
        }
    }
    
    private void fillCombo(){
            comboBox.addItem("كل الطلاب");
            comboBox.addItem("الابتدائي");
            comboBox.addItem("المتوسط");
            comboBox.addItem("الثانوي");      
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
    
    private void delete(int id)
    {
      String q = "update Student set Flag=0 where S_id="+id;
      
      try{
            connect.prepareStatement(q).executeUpdate();
            
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
        deleteBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

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

        studentsTable.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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
        jScrollPane1.setViewportView(studentsTable);

        deleteBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/erase-icon.png"))); // NOI18N
        deleteBtn.setText("حذف  ");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setText("اختر الحلقة :");

        comboBox.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N

        jButton1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        jButton1.setText("تحديث   ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("اختر الطالب الذي تريد حذفه ثم اضغط : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1164, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(17, 17, 17)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(156, 156, 156)
                .addComponent(exitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
       //idField.setText("");
       
       deleteOldDateOnJTable();
       comboBox.setSelectedIndex(0);
       
       if(LoginPage.pageNumber == 1)
           mainPage.setVisible(true);
        else if (LoginPage.pageNumber == 0)
           subMainPage.setVisible(true);
       
       deletePage.setVisible(false);
    }//GEN-LAST:event_exitBtnActionPerformed

   // boolean RefereshClicked = false;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //showStudents();
        
    //    if(RefereshClicked)
           deleteOldDateOnJTable();
        
        displayStudentsOnJTable();
          
    //    RefereshClicked = true;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        try{        
            if(studentsTable.getSelectedRow() != -1)
            {
                
              // before any JOptionPane write these lines of code to set the window size and font bigger  
              UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
              JLabel label = new JLabel("تأكيد الحذف ؟", JLabel.CENTER);
              label.setFont(new Font("Arial", Font.BOLD, 20));  
                
              int yesOrNo = JOptionPane.showConfirmDialog(null, label, "", JOptionPane.YES_NO_OPTION);
              if(yesOrNo == 0)
              {
              
             
               
                int column = 11;
                int row = studentsTable.getSelectedRow();
                int id = Integer.parseInt(studentsTable.getModel().getValueAt(row, column).toString());

                delete(id);
                
                ////////////////////////////////////// from referesh button
                
                deleteOldDateOnJTable();
                displayStudentsOnJTable();
                
                ///////////////////////////////////////
                
                
                 // before any JOptionPane write these lines of code to set the window size and font bigger  
                 UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                 label = new JLabel("تم حذف الطالب بنجاح", JLabel.CENTER);
                 label.setFont(new Font("Arial", Font.BOLD, 20));
                
                 JOptionPane.showMessageDialog(null, label, "", JOptionPane.INFORMATION_MESSAGE);
                
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
           JOptionPane.showMessageDialog(null, e);
        }

        
        
        
    }//GEN-LAST:event_deleteBtnActionPerformed

    
//    try{
//           Integer.parseInt(idField.getText());
//        }catch(NumberFormatException ex) {
//          
//        }
//    
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
            java.util.logging.Logger.getLogger(DeleteStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DeleteStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DeleteStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DeleteStudentPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new DeleteStudentPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton exitBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable studentsTable;
    // End of variables declaration//GEN-END:variables
}
