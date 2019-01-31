/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import halaqat_honien.Guardian;
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
public class AllGuardiansPage extends javax.swing.JFrame {

    Connection connect = null;
    Statement statement = null;
    PreparedStatement preparedStatement  = null;
    ResultSet resultSet = null;

    int colomsNumberOnJTable = 3;
    
    
    
//     DefaultTableModel model = new DefaultTableModel();
     
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
    

    
    // save the id for the selected previous guardian .. as public variable to be accessable from the (insertStudentPage)
    public String G_id;
    
    
    /**
     * Creates new form AllGuardiansPage
     */
    public AllGuardiansPage() {
        initComponents();
        
        
        try {
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/haneen_halaqat", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        
        guardiansTable.setModel(model);
        
        
        setColomnsNamesOnTable();
    }

    
    // set header colomns names in JTable
    private void setColomnsNamesOnTable(){
    
        Object [] TableColomnNames = new Object[colomsNumberOnJTable];

         TableColomnNames[2] = "الرقم";
         TableColomnNames[1] = "الاسم";
         TableColomnNames[0] = "رقم الجوال";

         model.setColumnIdentifiers(TableColomnNames);
         guardiansTable.setModel(model);
         
         // set header background color to light gray
         guardiansTable.getTableHeader().setBackground(Color.LIGHT_GRAY);        

         // change the fonts on both header and data
         guardiansTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 20));
         guardiansTable.setFont(new Font("Serif", Font.PLAIN, 20));
    }
    
    
    
    private ArrayList<Guardian> getGuardiansList(String query){
        
        ArrayList<Guardian> guardiansList = new ArrayList<Guardian>();
            
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
            
            Guardian guardian;
            while(resultSet.next()){
                guardian = new Guardian(resultSet.getInt("G_id"), resultSet.getString("G_name"), resultSet.getString("Phone_num")); 
                guardiansList.add(guardian);
            }
            
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        return guardiansList;
    }
    
    
    // Display Students on JTable in GUI
    public void displayGuardiansOnJTable(){
        
        String query = "select * from guardian";
                      
        ArrayList<Guardian> list = getGuardiansList(query);
        
        ///////////////////////////////////////////////////////////////////////////////////////  to show data on each cell on right side
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
        for(int i=0; i<colomsNumberOnJTable; i++)
        {
          guardiansTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        ///////////////////////////////////////////////////////////////////////////////////////
        
       
        Object [] rowItem = new Object[colomsNumberOnJTable];
        for(int i=0; i<list.size(); i++)
        {
          rowItem[2] = list.get(i).getId();
          rowItem[1] = list.get(i).getName();
          rowItem[0] = list.get(i).getPhoneNum();
 
          model.addRow(rowItem);
        }
   }
        
    private void deleteOldDateOnJTable(){
        
        DefaultTableModel model = (DefaultTableModel) guardiansTable.getModel(); 
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

        jScrollPane1 = new javax.swing.JScrollPane();
        guardiansTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        back = new javax.swing.JButton();
        addGuardInfo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        guardiansTable.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        guardiansTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        guardiansTable.setRowHeight(30);
        guardiansTable.setRowMargin(3);
        jScrollPane1.setViewportView(guardiansTable);

        jLabel1.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("اختيار ولي أمر سابق");

        back.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/back-icon(2).png"))); // NOI18N
        back.setText("رجوع    ");
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        addGuardInfo.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        addGuardInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/accept-icon.png"))); // NOI18N
        addGuardInfo.setText("إضافة إلى بيانات الطالب  ");
        addGuardInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGuardInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addGuardInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addContainerGap(120, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addGuardInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed

      // delete the shown date from the guardian table
      deleteOldDateOnJTable();
    
      allGuardiansPage.setVisible(false);
      
      insertPage.setVisible(true);
      insertPage.setEnabled(true);
    }//GEN-LAST:event_backActionPerformed

    private void addGuardInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGuardInfoActionPerformed
          
        if(guardiansTable.getSelectedRow() != -1)
        {
        
            int row = guardiansTable.getSelectedRow();

            int id_column = 2;   // take the id for the selected quardian and save ot to public variable G_id
            this.G_id = guardiansTable.getModel().getValueAt(row, id_column).toString(); 

            int name_colom = 1;  // take the name for the selected quardian
            String G_name = guardiansTable.getModel().getValueAt(row, name_colom).toString();

            int G_phoneNum_colom = 0; // take the phone number for the selected quardian
            String G_phoneNum = guardiansTable.getModel().getValueAt(row, G_phoneNum_colom).toString();

            // update the flag that identify wether the quardian in new or exsist before
            insertPage.thereIsPreGuardian = true;
            
            // copy the selected quardian info and pasted in student insert page at quardian fields 
            insertPage.setGuardianInfoFromAllGuardiansTable(G_name, G_phoneNum);

            
            // before closing all guardians window change the selected row to none
            guardiansTable.setSelectionMode(0);


            // delete the shown date from the guardian table
            deleteOldDateOnJTable();
            
            allGuardiansPage.setVisible(false);
            insertPage.setVisible(true);  
            insertPage.setEnabled(true);

        }
        else
        {
            // before any JOptionPane write these lines of code to set the window size and font bigger  
            UIManager.put("OptionPane.minimumSize",new Dimension(350,150));   
            JLabel label = new JLabel("الرجاء اختيار ولي أمر !!", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            
            JOptionPane.showMessageDialog(null, label, "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    }//GEN-LAST:event_addGuardInfoActionPerformed

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
            java.util.logging.Logger.getLogger(AllGuardiansPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllGuardiansPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllGuardiansPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllGuardiansPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AllGuardiansPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addGuardInfo;
    private javax.swing.JButton back;
    private javax.swing.JTable guardiansTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
