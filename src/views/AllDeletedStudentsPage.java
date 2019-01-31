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

/**
 *
 * @author Abdul
 */
public class AllDeletedStudentsPage extends javax.swing.JFrame {

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
     * Creates new form AllDeletedStudents
     */
    public AllDeletedStudentsPage() {
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

    
    private void fillCombo(){
            comboBox.addItem("كل الطلاب");
            comboBox.addItem("الابتدائي");
            comboBox.addItem("المتوسط");
            comboBox.addItem("الثانوي");      
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
    
    
    private void deleteOldDateOnJTable(){
        
        DefaultTableModel model = (DefaultTableModel) studentsTable.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i);  
        }
        
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
    
    
     // Display Students on JTable in GUI
    private void displayStudentsOnJTable()
    {
        
        String query = null;
        
            int selected = comboBox.getSelectedIndex();
            switch(selected){
                case 0:
                    query = "select * from Student where Flag=0";
                    break;
                case 1:
                    query = "select * from Student where Halaqah_id=1 and Flag=0";
                    break;
                case 2:
                    query = "select * from Student where Halaqah_id=2 and Flag=0";
                    break;
                case 3:
                    query = "select * from Student where Halaqah_id=3 and Flag=0";
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
    
    private void استرداد(int id)
    {
      String q = "update Student set Flag=1 where S_id="+id;
      
      try{
            connect.prepareStatement(q).executeUpdate();
            
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, e);
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

        jScrollPane2 = new javax.swing.JScrollPane();
        studentsTable = new javax.swing.JTable();
        refreshBtn = new javax.swing.JButton();
        comboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        refreshBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        refreshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        refreshBtn.setText("تحديث  ");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        comboBox.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("اختر الحلقة :");

        backBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        backBtn.setText("رجوع   ");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/undo-icon.png"))); // NOI18N
        jButton1.setText("استرداد  ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("اختر طالب إذا كنت تريد استرداده ثم اضغط : ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1393, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel2))))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        
        deleteOldDateOnJTable();
        displayStudentsOnJTable();
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed

        if(LoginPage.pageNumber == 1)
        mainPage.setVisible(true);
        else if (LoginPage.pageNumber == 0)
        subMainPage.setVisible(true);

        allDeletedStudentsPage.setVisible(false);

        deleteOldDateOnJTable();
        comboBox.setSelectedIndex(0);
    }//GEN-LAST:event_backBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        try{        
            if(studentsTable.getSelectedRow() != -1)
            {
                
              // before any JOptionPane write these lines of code to set the window size and font bigger  
              UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
              JLabel label = new JLabel("تأكيد الاسترداد ؟", JLabel.CENTER);
              label.setFont(new Font("Arial", Font.BOLD, 20));  
                
              int yesOrNo = JOptionPane.showConfirmDialog(null, label, "", JOptionPane.YES_NO_OPTION);
              if(yesOrNo == 0)
              {
              
             
               
                int column = 11;
                int row = studentsTable.getSelectedRow();
                int id = Integer.parseInt(studentsTable.getModel().getValueAt(row, column).toString());

                استرداد(id);
                ////////////////////////////////////// from referesh button
                deleteOldDateOnJTable();

                displayStudentsOnJTable();
                ///////////////////////////////////////
                
                 // before any JOptionPane write these lines of code to set the window size and font bigger  
                 UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                 label = new JLabel("تم استرداد الطالب بنجاح", JLabel.CENTER);
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
        
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(AllDeletedStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllDeletedStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllDeletedStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllDeletedStudentsPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AllDeletedStudentsPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JComboBox<String> comboBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JTable studentsTable;
    // End of variables declaration//GEN-END:variables
}
