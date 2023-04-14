
package ManageSuppliers;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.sql.*;
import UtiltiyClasses.DatabaseConnectivity;
import UtiltiyClasses.Validater;

public class AddNewSupplier extends JFrame implements ActionListener {
    private JPanel backPnl,formPnl,container;
    private JButton backBtn,addBtn,updateBtn;
    private JLabel sId,sName,sAddress,sContact,idLbl;
    private JTextField sNameF,sAddressF,sContactF;
    private Border bdr;
    private ResultSet rs;
    private int id;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public AddNewSupplier(int id){
        super("Add New Supplier");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        this.id=id;
        
        backPnl=new JPanel();
        backPnl.setLayout(new FlowLayout(0));
        backBtn=new JButton("Back");
        backBtn.addActionListener(this);
        backPnl.add(backBtn);
        add(backPnl,BorderLayout.NORTH);
        
        formPnl=new JPanel(new GridBagLayout());
        GridBagConstraints gc=new GridBagConstraints();
        gc.insets=new Insets(5,10,5,5);
        gc.anchor=GridBagConstraints.LINE_END;
        
        sId=new JLabel("Supplier Id:");
        idLbl=new JLabel();
        sName=new JLabel("Supplier Name:");
        sAddress=new JLabel("Address:");
        sContact=new JLabel("Contact:");
        
        sNameF=new JTextField(20);
        sAddressF=new JTextField(20);
        sContactF=new JTextField(20);
        
        addBtn=new JButton("Add");
        addBtn.addActionListener(this);
        updateBtn=new JButton("Update");
        updateBtn.addActionListener(this);
        
        gc.gridx=gc.gridy=0;
        formPnl.add(sId,gc);
        gc.gridy++;
        formPnl.add(sName,gc);
        gc.gridy++;
        formPnl.add(sAddress,gc);
        gc.gridy++;
        formPnl.add(sContact,gc);
        
        String sql="SELECT MAX(`Supplier Id`) FROM supplier_records";
        rs=connect.returnResult(sql);
        try{
            while(rs.next())
                idLbl.setText(rs.getString(1));
        }catch(SQLException e){
            System.out.println("Supplier id erroe "+e);
        }
                
        gc.gridy=0;
        gc.gridx++;
        gc.anchor=GridBagConstraints.LINE_START;
        formPnl.add(idLbl,gc);
        gc.gridy++;
        formPnl.add(sNameF,gc);
        gc.gridy++;
        formPnl.add(sAddressF,gc);
        gc.gridy++;
        formPnl.add(sContactF,gc);
        gc.gridy++;
        if(id>0){
            getDataForEdit(id);
            formPnl.add(updateBtn,gc);
        }else
            formPnl.add(addBtn,gc);
        
        bdr=BorderFactory.createTitledBorder(bdr, "Please Fill the Following Details",0,0,new Font("Tahoma",1,14), Color.RED);
        formPnl.setBorder(bdr);
        add(formPnl,BorderLayout.CENTER);
        
    }
    /**gets the data from textField
     * @param id
     */
    public void onAddBtnClicked(int id){
        String sql,what="Add New Supplier";
        if(!(Validater.compare(sAddressF.getText(),"a"))){
            JOptionPane.showMessageDialog(null,"Address cannot be null and must be non numeric","Add Supplier",0);
            sAddress.requestFocus();
        }
         if(!(Validater.compare(sContactF.getText(),"N"))){
            JOptionPane.showMessageDialog(null,"Contact cannot be null and must be numeric","Add Supplier",0);
            sContact.requestFocus();
        }
        else{
            String sname=sNameF.getText();
            String sadd=sAddressF.getText();
            String scont=sContactF.getText();
            if(id==0)
                sql="INSERT INTO supplier_records VALUES(null,'"+sname+"','"+sadd+"','"+scont+"')";
            else{
                sql=" UPDATE supplier_records SET `Supplier Name`='"+sname+"',Address='"+sadd+"',Contact='"+scont+"' WHERE `Supplier Id`="+id;
                what="Update Supplier";
            }
            if(connect.executeSql(sql,what)){
                new AddNewSupplier(0);
                dispose();
            }
        }
    }
    /**Retrieves data from database and sets to textField
     * @param thatId
     */
    public void getDataForEdit(int thatId){
        ArrayList<String> al=new ArrayList();
        String sq="SELECT * FROM supplier_records WHERE `Supplier Id`="+thatId;
        rs=connect.returnResult(sq);
        try{
            int i=1;
            while(rs.next()){
                for(int j=1;j<=4;j++)
                    al.add(rs.getString(j));
                i++;
            }
            idLbl.setText(""+thatId);
            sNameF.setText(al.get(1));
            sAddressF.setText(al.get(2));
            sContactF.setText(al.get(3));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Update Supplier",JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
//    public static void main(String[] args){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new AddNewSupplier(0);
//            }
//        });
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp=(JButton)e.getSource();
        if(temp==addBtn)
            onAddBtnClicked(this.id);
        if(temp==backBtn)
            dispose();
        if(temp==updateBtn)
            onAddBtnClicked(this.id);
    }
    
}
