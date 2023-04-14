
package ManageUsers;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import UtiltiyClasses.DatabaseConnectivity;
import java.util.Vector;

public class ManageUser extends JFrame implements ActionListener,KeyListener {
    private JPanel backPnl,container,searchPnl,btnPnl;
    private JButton backBtn,addNewBtn,deleteBtn,editBtn,searchBtn;
    private JLabel searchLbl;
    private JTextField searchField;
    private JTable table;
    private JScrollPane sp;
    private DefaultTableModel model;
    private ResultSet rs;
    private Vector<String> cols;
    private String sql;
    private int iidd;
    DatabaseConnectivity connect = new DatabaseConnectivity();
            
    public ManageUser(int iid) {
        super("Manage User");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setSize(800,500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iidd=iid;
        backPnl=new JPanel();
        backPnl.setLayout(new FlowLayout(0));
        backBtn=new JButton("Back");
        backBtn.addActionListener(this);
        backPnl.add(backBtn);
        add(backPnl,BorderLayout.NORTH);
        
        searchPnl=new JPanel(new FlowLayout());
        searchLbl=new JLabel("Enter User's Id or Name: ");
        searchField=new JTextField(30);
        searchField.addKeyListener(this);        
        searchBtn=new JButton("Search");
        searchBtn.addActionListener(this);
        searchPnl.add(searchLbl);
        searchPnl.add(searchField);
        searchPnl.add(searchBtn);
        
        
        table=new JTable();
        model =new DefaultTableModel();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);//automatically sorts row
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);//lets see
        sp=new JScrollPane(table);
        
        btnPnl=new JPanel(new FlowLayout());
        addNewBtn=new JButton("Add New User");
        addNewBtn.addActionListener(this);
        editBtn=new JButton("Edit");
        editBtn.addActionListener(this);
        deleteBtn=new JButton("Delete");
        deleteBtn.addActionListener(this);
        btnPnl.add(editBtn);
        btnPnl.add(addNewBtn);
        btnPnl.add(deleteBtn);
        
        container=new JPanel(new BorderLayout());
        container.add(searchPnl,BorderLayout.NORTH);
        container.add(sp,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
        add(btnPnl,BorderLayout.SOUTH);
        
        sql="SELECT u.`uid`, `first_name`, mu.`Middle Name`,`last_name`, `Date Of Birth`,"
                + " `Gender`, `Nationality`, `Citizenship No`, `PCity`, `PMuncipal`, "
                + "`PWardNo`,ut.City,ut.Muncipal,ut.`Ward No`, `Contact`,ue.Email, "
                + "`Date Joined`, `username`, `password`, "
                + "`isadmin` FROM `user_login_details` u LEFT JOIN user_middle_name mu ON mu.uid=u.uid"
                + " LEFT JOIN user_temp_address ut ON ut.uid=u.uid LEFT JOIN user_email ue ON ue.uid=u.uid";
                  
        rs=connect.returnResult(sql);
        cols=connect.getColumnsHeading(sql, rs);
        try{
            sp.repaint();
            model.setColumnIdentifiers(cols);
            model.getDataVector().removeAllElements();
            while(rs.next()){
                Vector<String> v=new Vector<>();
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    v.add(rs.getString(i));
                model.addRow(v);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Supplier Details",JOptionPane.ERROR_MESSAGE);
        }
        
    }
//    public static void main(String[] args){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new ManageUser(0);
//            }
//        });
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn=(JButton)e.getSource();
        if(btn==backBtn)
            dispose();
        else if(btn==addNewBtn)
            new AddNewUser(0);
        else if(btn==deleteBtn){
            if(table.getSelectedRow()!=-1){
                int temp=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),0)));
                if(temp==iidd)
                    JOptionPane.showMessageDialog(null,"You Cannot Delete Yourself","Delete Member",JOptionPane.ERROR_MESSAGE);
                else{
                    int what=JOptionPane.showConfirmDialog(null,"Are you sure you want to delete a user!","Delete Member",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                    if(what==JOptionPane.YES_OPTION){
                        String sql="DELETE FROM user_login_details WHERE uid="+temp;
                        connect.executeSql(sql,"Delete User");
                        model.removeRow(table.getSelectedRow());
                    }
                }
            }else
                JOptionPane.showMessageDialog(null,"Please select a user first!","Delete Member",JOptionPane.ERROR_MESSAGE);
        }
        else if(btn==editBtn){
            if(table.getSelectedRow()!=-1){
                int temp=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),0)));
                new AddNewUser(temp);
            }
            else
                JOptionPane.showMessageDialog(null,"Select a user first","Edit User",JOptionPane.ERROR_MESSAGE);
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
        if(searchField.getText().length()!=0)
                    sql="SELECT u.`uid`, `first_name`, mu.`Middle Name`,`last_name`, `Date Of Birth`,"
                            + " `Gender`, `Nationality`, `Citizenship No`, `PCity`, `PMuncipal`, "
                            + "`PWardNo`,ut.City,ut.Muncipal,ut.`Ward No`, `Contact`,ue.Email,"
                            + " `Date Joined`, `username`, `password`, `isadmin` FROM `user_login_details` u LEFT JOIN"
                            + " user_middle_name mu ON mu.uid=u.uid LEFT JOIN"
                            + " user_temp_address ut ON ut.uid=u.uid LEFT JOIN user_email ue ON ue.uid=u.uid"
                            + " WHERE u.`first_name` LIKE '"+searchField.getText()+"%'||"
                            + " u.uid ='"+searchField.getText()+"'";
                else
                    sql="SELECT u.`uid`, `first_name`, mu.`Middle Name`,`last_name`, `Date Of Birth`,"
                            + " `Gender`, `Nationality`, `Citizenship No`, `PCity`, `PMuncipal`, "
                            + "`PWardNo`,ut.City,ut.Muncipal,ut.`Ward No`, `Contact`,ue.Email,"
                            + " `Date Joined`, `username`, `password`, `isadmin` FROM `user_login_details` u LEFT JOIN"
                            + " user_middle_name mu ON mu.uid=u.uid LEFT JOIN"
                            + " user_temp_address ut ON ut.uid=u.uid LEFT JOIN user_email ue ON ue.uid=u.uid";
                try{
                    rs=connect.returnResult(sql);
                    cols=connect.getColumnsHeading(sql, rs);
                    sp.repaint();
                    model.getDataVector().removeAllElements();
                    while(rs.next()){
                        Vector<String> v=new Vector<>();
                        for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                            v.add(rs.getString(i));
                        model.addRow(v);
                    }
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,ee.getMessage(),"Manage User",JOptionPane.ERROR_MESSAGE);
                }
    }
 
}
