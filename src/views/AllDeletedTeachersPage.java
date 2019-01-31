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
public class AllDeletedTeachersPage extends javax.swing.JFrame {

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
     * Creates new form AllDeletedTeachersPage
     */
    public AllDeletedTeachersPage() {
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
    
     private void deleteOldDateOnJTable(){
        
        DefaultTableModel model = (DefaultTableModel) teachersTable.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i);  
        }
        
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
        
        String query = "select * from Teacher where Flag=0";
            
        ArrayList<Teacher> list = getTeachersList(query);
        
        ///////////////////////////////////////////////////////////////////////////////////////  to show data on each colom on right
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
    
    private void استرداد(int id)
    {
      String q = "update Teacher set Flag=1 where T_id="+id;
      
      try{
            connect.prepareStatement(q).executeUpdate();
            
           
        }catch(Exception e)
        {
           JOptionPane.showMessageDialog(null, e);
        }
    }
    
     private String teacherStatus(boolean Flag){
        
       if(Flag == true)
           return "موجود";
       else
           return "محذوف";
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        refereshBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        teachersTable = new javax.swing.JTable();
        backBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        refereshBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        refereshBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/refresh-icon.png"))); // NOI18N
        refereshBtn.setText("تحديث  ");
        refereshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refereshBtnActionPerformed(evt);
            }
        });

        teachersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        teachersTable.setRowHeight(30);
        teachersTable.setRowMargin(3);
        jScrollPane1.setViewportView(teachersTable);

        backBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        backBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        backBtn.setText("رجوع   ");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        deleteBtn.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        deleteBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/undo-icon.png"))); // NOI18N
        deleteBtn.setText("استرداد  ");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("اختر مدرس إذا كنت تريد استرداده ثم اضغط :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(refereshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel2))))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(refereshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addComponent(backBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refereshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refereshBtnActionPerformed

        
        deleteOldDateOnJTable();

        displayTeachersOnJTable();
    }//GEN-LAST:event_refereshBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        mainPage.setVisible(true);
        allDeletedTeachersPage.setVisible(false);
    }//GEN-LAST:event_backBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        try{

            if(teachersTable.getSelectedRow() != -1)
            {
                
                // before any JOptionPane write these lines of code to set the window size and font bigger  
                UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                JLabel label = new JLabel("تأكيد الاسترداد ؟", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 20));
                
                int yesOrNo = JOptionPane.showConfirmDialog(null, label, "", JOptionPane.YES_NO_OPTION);
                if(yesOrNo == 0)
                {
                    
                    int column = 5;
                    int row = teachersTable.getSelectedRow();
                    int id = Integer.parseInt(teachersTable.getModel().getValueAt(row, column).toString());

                    استرداد(id);
                    ////////////////////////////////////// from referesh button  
                    deleteOldDateOnJTable();

                    displayTeachersOnJTable();
                    ///////////////////////////////////////

                    
                    // before any JOptionPane write these lines of code to set the window size and font bigger  
                    UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                    label = new JLabel( "تم استرداد المدرس بنجاح", JLabel.CENTER);
                    label.setFont(new Font("Arial", Font.BOLD, 20));
                    
                    JOptionPane.showMessageDialog(null, label, "", JOptionPane.INFORMATION_MESSAGE);

                }
            }else
            {
                
                // before any JOptionPane write these lines of code to set the window size and font bigger  
                UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
                JLabel label = new JLabel("الرجاء اختيار مدرس !!", JLabel.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 20));
                
                JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_deleteBtnActionPerformed

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
            java.util.logging.Logger.getLogger(AllDeletedTeachersPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllDeletedTeachersPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllDeletedTeachersPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllDeletedTeachersPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AllDeletedTeachersPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refereshBtn;
    private javax.swing.JTable teachersTable;
    // End of variables declaration//GEN-END:variables
}
