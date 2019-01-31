/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import static halaqat_honien.Halaqat_Honien.*;
import halaqat_honien.Teacher;
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
public class UpdateTeacherPage extends javax.swing.JFrame {

    Connection connect = null;
    Statement statement = null;
    PreparedStatement preparedStatement  = null;
    ResultSet resultSet = null;
    
    int colomsNumOnJTable = 6;
    
   
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
     * Creates new form UpdateTeacherPage
     */
    public UpdateTeacherPage() {
        initComponents();
        
        try {
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/haneen_halaqat", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
         
        teachersTable.setModel(model);
         
         
        setColomnsNamesOnTable();
    }
    
    
    // set header colomns names in JTable
    private void setColomnsNamesOnTable(){
    
        Object [] TableColomnNames = new Object[colomsNumOnJTable];
        
         TableColomnNames[5] = "الرقم";
         TableColomnNames[4] = "الاسم";
         TableColomnNames[3] = "رقم الجوال";
         TableColomnNames[2] = "الراتب";
         TableColomnNames[1] = "اسم المشرف عليه";
         TableColomnNames[0] = "حالة المدرس";

  
         model.setColumnIdentifiers(TableColomnNames);
         teachersTable.setModel(model);
         
         teachersTable.getTableHeader().setBackground(Color.LIGHT_GRAY);        
       
         teachersTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
         teachersTable.setFont(new Font("Serif", Font.PLAIN, 20));
    }
    
    
    
     private String getSupervisorName(int id){
        
        String q = "select T_name from Teacher where T_id="+id;
        String name = "";
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(q);
            while(resultSet.next())
            {
              name = resultSet.getString("T_name");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return name;
    }
     
    private ArrayList<Teacher> getTeachersList(String query){
        
        ArrayList<Teacher> teachersList = new ArrayList<Teacher>();
        
        
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            Teacher teacher;
            while(resultSet.next()){
                teacher = new Teacher(resultSet.getInt("T_id"), resultSet.getString("T_name"), resultSet.getString("Phone_num"), resultSet.getInt("Salary"), resultSet.getInt("Maneger_id"), resultSet.getBoolean("Flag")); 
                teachersList.add(teacher);
            }
            
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        return teachersList;
    } 
     
    
    // Display Students on JTable in GUI
    private void displayTeachersOnJTable(){
        
        String query = "select * from Teacher where Flag=1";
            
        ArrayList<Teacher> list = getTeachersList(query); 
        
        ///////////////////////////////////////////////////////////////////////////////////////  to show data on each cell at right side
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        for(int i=0; i<colomsNumOnJTable; i++)
        {
          teachersTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        
        
        
        Object [] rowItem = new Object[colomsNumOnJTable];
        for(int i=0; i<list.size(); i++)
        {         
          rowItem[5] = list.get(i).getId();
          rowItem[4] = list.get(i).getName();
          rowItem[3] = list.get(i).getPhoneNum();
          rowItem[2] = list.get(i).getSalary();
          rowItem[1] = getSupervisorName(list.get(i).getManagerID());                                     
          rowItem[0] = teacherStatus(list.get(i).getFlag());
            
          model.addRow(rowItem);
        }
        
    }
    
    private void deleteOldDateOnJTable(){
        
        DefaultTableModel model = (DefaultTableModel) teachersTable.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i);  
        }
        
    }

    
     private ResultSet excuteQuery(String q){
       
        try{
            
            connect.prepareStatement(q).executeUpdate();
            
        }catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, e.getStackTrace());
        }
        
        return resultSet;
    }
    
    private void updateInfo(){
        String query = null;
          
        int column = 5;  
        int row = teachersTable.getSelectedRow();
        int id = Integer.parseInt(teachersTable.getValueAt(row, column).toString());
        
        String FullN = fullName.getText();
        query = "update teacher set T_name='"+FullN+"' where T_id="+id;
        excuteQuery(query);
        
        String phoneN = phoneNum.getText();
        query = "update teacher set Phone_num='"+phoneN+"' where T_id="+id;
        excuteQuery(query);
        
        int sal = Integer.parseInt(salary.getText().toString());
        query = "update teacher set Salary="+sal+" where T_id="+id;
        excuteQuery(query);
        
           
    }
    
    
    private void fillDataFields(){
        
        DefaultTableModel model = (DefaultTableModel) teachersTable.getModel();
        int selectedRow = teachersTable.getSelectedRow();
        
        fullName.setText(model.getValueAt(selectedRow, 4).toString());
        phoneNum.setText(model.getValueAt(selectedRow, 3).toString());
        salary.setText(model.getValueAt(selectedRow, 2).toString());
    }
    
    private void removeDataFields(){
        
        fullName.setText("");
        phoneNum.setText("");         
        salary.setText("");             
    }
    
    
    private String teacherStatus(boolean Flag){
        
       if(Flag == true)
           return "موجود";
       else
           return "محذوف";
    }
    
    
    private boolean isCompleteDate(String fn, String phNum, String sal)
    {
      if(fn.equals("") || phNum.equals("") || sal.equals(""))
        return false;
      
      return true;
    }
    
    private boolean isValidSalary(int sal)
    {
      if(sal < 0)
          return false;
      
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

        jScrollPane1 = new javax.swing.JScrollPane();
        teachersTable = new javax.swing.JTable();
        refereshBtn = new javax.swing.JButton();
        backBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fullName = new javax.swing.JTextField();
        phoneNum = new javax.swing.JTextField();
        salary = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        teachersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        teachersTable.setRowHeight(30);
        teachersTable.setRowMargin(3);
        teachersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teachersTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(teachersTable);

        refereshBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        refereshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        refereshBtn.setText("تحديث  ");
        refereshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refereshBtnActionPerformed(evt);
            }
        });

        backBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        backBtn.setText("رجوع   ");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("الاسم كاملاً : ");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("رقم الجوال :");

        jLabel3.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("الراتب :");

        fullName.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        fullName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        fullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullNameActionPerformed(evt);
            }
        });

        phoneNum.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        phoneNum.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        salary.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        salary.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        saveBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        saveBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/floppy-icon.png"))); // NOI18N
        saveBtn.setText("حفظ  ");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(641, 641, 641)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fullName, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
                                    .addComponent(phoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(93, 93, 93))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(salary, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addContainerGap())))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 1242, Short.MAX_VALUE)
                        .addComponent(refereshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1408, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(refereshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salary, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE))
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   // boolean RefereshClicked = false;
    
    private void refereshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refereshBtnActionPerformed

        removeDataFields();
        
        deleteOldDateOnJTable();

        displayTeachersOnJTable();

    }//GEN-LAST:event_refereshBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed

        removeDataFields();
        
        deleteOldDateOnJTable();

        mainPage.setVisible(true);
        updateTeacherPage.setVisible(false);
    }//GEN-LAST:event_backBtnActionPerformed

    private void fullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fullNameActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        try{
            if(teachersTable.getSelectedRow() != -1)
            {
                
                String full_name = fullName.getText();
                String phone_num = phoneNum.getText();
                String T_salary = salary.getText();
                
                if(isCompleteDate(full_name, phone_num, T_salary))
                {
                    
                    int sal = Integer.parseInt(salary.getText());
                    
                    if(isValidSalary(sal))
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
                            removeDataFields();
                            deleteOldDateOnJTable();
                            displayTeachersOnJTable();
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
                        JLabel label = new JLabel("الراتب غير صحيح !!", JLabel.CENTER);
                        label.setFont(new Font("Arial", Font.BOLD, 20)); 
                        
                        
                       JOptionPane.showMessageDialog(null, label, "", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else
                {
                   // before any JOptionPane write these lines of code to set the window size and font bigger  
                   UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                   JLabel label = new JLabel("بيانات غير مكتملة !!", JLabel.CENTER);
                   label.setFont(new Font("Arial", Font.BOLD, 20));  
                    
                   JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }
            else
            {
                // before any JOptionPane write these lines of code to set the window size and font bigger  
                UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                JLabel label = new JLabel("الرجاء اختيار مدرس !!", JLabel.CENTER);
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
    }//GEN-LAST:event_saveBtnActionPerformed

    private void teachersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teachersTableMouseClicked
         
        fillDataFields();
    }//GEN-LAST:event_teachersTableMouseClicked

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
            java.util.logging.Logger.getLogger(UpdateTeacherPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UpdateTeacherPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UpdateTeacherPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UpdateTeacherPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UpdateTeacherPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JTextField fullName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField phoneNum;
    private javax.swing.JButton refereshBtn;
    private javax.swing.JTextField salary;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable teachersTable;
    // End of variables declaration//GEN-END:variables
}
