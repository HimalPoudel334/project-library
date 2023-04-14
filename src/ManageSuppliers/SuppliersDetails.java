
package ManageSuppliers;


import ManageSuppliers.AddNewSupplier;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import UtiltiyClasses.DatabaseConnectivity;

public class SuppliersDetails extends JFrame implements ActionListener,KeyListener{
    private JPanel backPnl,container,searchPnl,btnPnl;
    private JButton backBtn,searchBtn,newSup,deleteSup,editSup;
    private JLabel searchLbl;
    private JTable table;
    private JScrollPane sp;
    private DefaultTableModel model;
    private JTextField searchField;
    private String sql;
    private ResultSet rs;
    private String temp,val;
    private int mid;
    DatabaseConnectivity connect = new DatabaseConnectivity();
    
    public SuppliersDetails(int type) {
        super("Manage Suppliers");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        
        backPnl=new JPanel();
        backPnl.setLayout(new FlowLayout(0));
        backBtn=new JButton("Back");
        backBtn.addActionListener(this);
        backPnl.add(backBtn);
        add(backPnl,BorderLayout.NORTH);
        
        searchPnl=new JPanel(new FlowLayout());
        searchLbl=new JLabel("Enter Supplier's Id or Name: ");
        searchField=new JTextField(30);
        searchField.addKeyListener(this);        
        searchBtn=new JButton("Search");
        searchBtn.addActionListener(this);
        searchPnl.add(searchLbl);
        searchPnl.add(searchField);
        searchPnl.add(searchBtn);
        
        String columns[]={"Supplier ID","Supplier Name","Address","Contact"};
        table=new JTable();
        model =new DefaultTableModel();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);//automatically sorts row
        //table.setCellSelectionEnabled(true);//enables selecting cells
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);//lets see
        model.setColumnIdentifiers(columns);
        sp=new JScrollPane(table);
        model.addTableModelListener(new TableModelListener() {//get the edited value 
            @Override
            public void tableChanged(TableModelEvent e) {
                if(table.isEditing()){
                    val=String.valueOf(table.getValueAt(table.getSelectedRow(),table.getSelectedColumn()));
                    temp=String.valueOf(table.getValueAt(table.getSelectedRow(),0));
                    mid=Integer.parseInt(temp);
                    temp=String.valueOf(table.getColumnName(table.getSelectedColumn()));
                    int n=JOptionPane.showConfirmDialog(null,"Are you sure you want to edit?","Edit Member",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                    if(n==JOptionPane.YES_OPTION){
                        String query="UPDATE supplier_records SET `"+temp+"`='"+val+"' WHERE `Supplier Id`="+mid;
                        connect.executeSql(query,"Update Member");
                    }
                }
            }
        });
                        
        btnPnl=new JPanel(new FlowLayout());
        newSup=new JButton("Add New Supplier");
        newSup.addActionListener(this);
        deleteSup=new JButton("Delete Supplier");
        deleteSup.addActionListener(this);
        editSup=new JButton("Edit Supplier");
        editSup.addActionListener(this);
        btnPnl.add(editSup);
        btnPnl.add(newSup);
        btnPnl.add(deleteSup);
        
        sql="SELECT * FROM supplier_records";
        rs=connect.returnResult(sql);
        try{
            sp.repaint();
            model.getDataVector().removeAllElements();
            while(rs.next())
                model.addRow(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)});
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Supplier Details",JOptionPane.ERROR_MESSAGE);
        }
        
        container=new JPanel(new BorderLayout());
        container.add(searchPnl,BorderLayout.NORTH);
        container.add(sp,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
        if(type==1)
            add(btnPnl,BorderLayout.SOUTH);
        
        
    }
//    public static void main(String[] args){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SuppliersDetails();
//            }
//        });
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp=(JButton)e.getSource();
        if(temp==backBtn)
            dispose();
        else if(temp==newSup){
            new AddNewSupplier(0);
            dispose();            
        }
        else if(temp==deleteSup){
            if(table.getSelectedRow()==-1)
                JOptionPane.showMessageDialog(null,"Please Select a Supplier First!","Delete Customer",JOptionPane.ERROR_MESSAGE);
            else{
                int val=JOptionPane.showConfirmDialog(null,"Are You Sure You Want to Delete Supplier?","Delete Supplier",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if(val==JOptionPane.YES_OPTION){
                    int id=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),0)));
                    String s="DELETE FROM supplier_records WHERE `Supplier Id`="+id;
                    connect.executeSql(s,"Delete Member");
                    model.removeRow(table.getSelectedRow());
                }
            }
        }
        else if(temp==editSup){
            if(table.getSelectedRow()==-1)
                JOptionPane.showMessageDialog(null,"Please Select a Supplier First!","Delete Customer",JOptionPane.ERROR_MESSAGE);
            else{
                int id=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),0)));
                new AddNewSupplier(id);
                dispose();
            }
        }
            
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        JTextField temp1=(JTextField)e.getSource();
        if(temp1==searchField){
            if(searchField.getText().length()!=0){
                sql=" SELECT * FROM supplier_records WHERE `Supplier Id` LIKE '"+searchField.getText()+"%'||`Supplier Name` LIKE '"+searchField.getText()+"%'";
            }else
                sql=" SELECT * FROM supplier_records";
            rs=connect.returnResult(sql);
            try{
                sp.repaint();
                model.getDataVector().removeAllElements();
                while(rs.next())
                    model.addRow(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)});
            }catch(SQLException ee){
                JOptionPane.showMessageDialog(null,ee.getMessage(),"Supplier Details",JOptionPane.ERROR_MESSAGE);
            }
        }
            
    }
        
}
