
package ManageMember;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import UtiltiyClasses.DatabaseConnectivity;
import ManageBooks.IssueReceiveFinal;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class EditMemberFinal extends JFrame {
    private JPanel homePnl,container,searchPnl,tabelPnl,btnPnl;
    private JButton search,homeBtn,deleteBtn,editButton,addNewMember,printId;
    private JLabel name;
    private JTable table;
    private JScrollPane sp;
    private DefaultTableModel model;
    private JTextField nameF;
    private String uName,sql,val,temp;
    private ResultSet rs,tempRs;
    private int mid,deleteTemp;
    private Vector col;
    private int mmIIDD;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public EditMemberFinal(int uId){
        
        super("Edit Member");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        
        mmIIDD=uId;
        homePnl=new JPanel();
        homePnl.setLayout(new FlowLayout(0));
        homeBtn=new JButton("Home");
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        homePnl.add(homeBtn);
        add(homePnl,BorderLayout.NORTH);
        
        container=new JPanel();
        container.setLayout(new BorderLayout());
        searchPnl=new JPanel();
        searchPnl.setLayout(new FlowLayout());
        name= new JLabel("Enter Member Name or Id");
        nameF=new JTextField(30);
                       
        nameF.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                if(nameF.getText().length()!=0)
                    sql="SELECT mr.`mid`,`First Name`,mm.`Middle Name`,`Last Name`, `Gender`, `D.O.B`,"
                + " `Nationality`, `Address`,mc.`Citizenship No` ,`Contact`,me.`Email`, `Effective From`, "
                + "`Effective To`, `Member Type`, `Library Deposit`,`Fine` FROM "
                + "`member_records` mr LEFT JOIN `members_middle_name` mm ON mm.mid=mr.mid"
                + " LEFT JOIN member_citizenship_no mc ON mr.mid=mc.mid LEFT JOIN member_email me ON me.mid=mr.mid WHERE `mr`.`First Name` LIKE '"+nameF.getText()+"%' || `mr`.`mid` = '"+nameF.getText()+"'";
                else
                    sql="SELECT mr.`mid`,mr.`First Name`,mm.`Middle Name`,`Last Name`, `Gender`, `D.O.B`,"
                + " `Nationality`, `Address`,mc.`Citizenship No` ,`Contact`,Email, `Effective From`, "
                + "`Effective To`, `Member Type`, `Library Deposit`,`Fine` FROM "
                + "`member_records` mr LEFT JOIN `members_middle_name` mm ON mm.mid=mr.mid"
                + " LEFT JOIN member_citizenship_no mc ON mr.mid=mc.mid LEFT JOIN member_email me ON me.mid=mr.mid ORDER BY(mr.mid)";
                try{
                    rs=connect.returnResult(sql);
                    sp.repaint();
                    model.getDataVector().removeAllElements();
                    while(rs.next())
                        model.addRow(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),
                            rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                            rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                            rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16)});
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,ee.getMessage(),"Search Books",JOptionPane.ERROR_MESSAGE);
                }
           }      
        });
        search= new JButton("Search");
        deleteBtn=new JButton("Delete");
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDeleteBtnPressed();
            }
        });
        editButton=new JButton("Edit");
        editButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                if(table.getSelectedRow()==-1)
                    JOptionPane.showMessageDialog(null,"Please select a row first!","Edit Member",JOptionPane.ERROR_MESSAGE);
                else{
                    int i=Integer.parseInt((String) table.getValueAt(table.getSelectedRow(),0));
                    new MemberRegisterFinal(i,uId);
                    dispose();
                }
            }
        });
        addNewMember=new JButton("Add New Member");
        addNewMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MemberRegisterFinal(0,uId);
                dispose();
            }
        });
        rs=connect.returnResult("Select username from user_login_details where uid="+uId);
        try{
            while(rs.next())
                uName=rs.getString(1);
        }catch(SQLException ex){}
        printId=new JButton("Print Id");
        printId.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(table.getSelectedRow()==-1)
                    JOptionPane.showMessageDialog(null,"Please select a member first","Print Id",JOptionPane.ERROR_MESSAGE);
                else{
                    int i=Integer.parseInt((String) table.getValueAt(table.getSelectedRow(),0));
                    try {
                        InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\LibraryCard.jrxml"));
                        JasperDesign jd=JRXmlLoader.load(in);   
                        JasperReport jr=JasperCompileManager.compileReport(jd);
                        HashMap param=new HashMap();
                        param.put("mid",i);
                        param.put("username",uName);
                        JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                        JasperViewer.viewReport(j, false);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex); 
                    }
                }
            }
        });
        
        btnPnl=new JPanel(new FlowLayout());
        btnPnl.add(editButton);
        btnPnl.add(addNewMember);
        btnPnl.add(deleteBtn);
        btnPnl.add(printId);
        
        searchPnl.add(name);
        searchPnl.add(nameF);
        search.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //searchBooks();
            }
        });
        searchPnl.add(search);
        container.add(searchPnl,BorderLayout.NORTH);
        tabelPnl=new JPanel();
        tabelPnl.setLayout(new BorderLayout());
        container.add(tabelPnl,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
    
        //String columns[]={"Member ID","First Name","Middle Name","Last Name","Gender","D.O.B","Nationality","Citizenship No","Address","Contact","Email","Effective From","Effective To","Member Type","Library Deposit"};
        table=new JTable();
        model =new DefaultTableModel();
        table.setModel(model);
        table.setRowSelectionAllowed(true);//enables selecting an entire row
        table.setAutoCreateRowSorter(true);//automatically sorts row
        //table.setCellSelectionEnabled(true);//enables selecting cells
        //model.setColumnIdentifiers(columns);
        sp=new JScrollPane(table);
                                        
        model.addTableModelListener(new TableModelListener() {//get the edited value 
            @Override
            public void tableChanged(TableModelEvent e) {
                if(table.isEditing()){
                    val=String.valueOf(table.getValueAt(table.getSelectedRow(),table.getSelectedColumn()));
                    temp=String.valueOf(table.getValueAt(table.getSelectedRow(),0));
                    mid=Integer.parseInt(temp);
                    temp=String.valueOf(table.getColumnName(table.getSelectedColumn()));
                    try {
                        onEnterPressed();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null,"Error!");
                    }
                }
                
            }
        });
        try{
            String sQl="SELECT mr.`mid`,mr.`First Name`,mm.`Middle Name`,`Last Name`, `Gender`, `D.O.B`,"
                + " `Nationality`, `Address`,mc.`Citizenship No` ,`Contact`,`Email`, `Effective From`, "
                + "`Effective To`, `Member Type`, `Library Deposit`,`Fine` FROM "
                + "`member_records` mr LEFT JOIN `members_middle_name` mm ON mm.mid=mr.mid"
                + " LEFT JOIN member_citizenship_no mc ON mr.mid=mc.mid LEFT JOIN member_email me ON me.mid=mr.mid ORDER BY(mr.mid)";
            rs=connect.returnResult(sQl);
            col=connect.getColumnsHeading(sQl, rs);
            model.setColumnIdentifiers(col);
            while(rs.next()){
//                Vector<String> v=new Vector<>();
//                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
//                    v.add(rs.getString(i));
//                model.addRow(v);
                model.addRow(new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),
                    rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),rs.getString(16)});
            }
            sp.repaint();
        }catch(SQLException e){

        }
                               
        tabelPnl.add(sp,BorderLayout.CENTER);
        add(btnPnl,BorderLayout.SOUTH);
    }
    public void onEnterPressed() throws SQLException{
        String query;
        int n=JOptionPane.showConfirmDialog(null,"Are you sure you want to edit?","Edit Member",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        switch (n) {
            case JOptionPane.YES_OPTION:
                switch(temp){
                    case "Middle Name":
                        query="SELECT * FROM members_middle_name WHERE mid="+mid;
                        rs=connect.returnResult(query);
                        if(rs.next())
                            query="UPDATE members_middle_name SET `Middle Name`='"+val+"' WHERE mid="+mid;
                        else
                            query="INSERT INTO members_middle_name VALUES("+mid+",'"+val+"')";
                        break;
                    case "Citizenship No":
                        query="SELECT * FROM member_citizenship_no WHERE mid="+mid;
                        rs=connect.returnResult(query);
                        if(rs.next())
                            query="UPDATE member_citizenship_no SET `Citizenship No`='"+val+"' WHERE mid="+mid;
                        else
                            query="INSERT INTO member_citizenship_no VALUES("+mid+",'"+val+"')";
                        break;
                    case "Email":
                        query="SELECT * FROM member_email WHERE mid="+mid;
                        rs=connect.returnResult(query);
                        if(rs.next())
                            query="UPDATE member_email SET `Email`='"+val+"' WHERE mid="+mid;
                        else
                            query="INSERT INTO member_email VALUES("+mid+",'"+val+"')";
                        break;
                    default:
                        query="UPDATE member_records SET `"+temp+"`='"+val+"' WHERE mid="+mid;                        
                }
                connect.executeSql(query,"Update Member");
                break;
            case JOptionPane.NO_OPTION:                
                break;
            default:
                break;
        }
    }
    public void onDeleteBtnPressed(){
        if(table.getSelectedRow()!=-1){
            int temp2=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),0)));
            String qu="SELECT * FROM book_records WHERE mid="+temp2;
            try{
                tempRs=connect.returnResult(qu);
                if(tempRs.next()){
                    JOptionPane.showMessageDialog(null,"Please return issued books first","Delete Member",JOptionPane.ERROR_MESSAGE);
                    new IssueReceiveFinal(temp2,mmIIDD);
                }
                else{
                    int n=JOptionPane.showConfirmDialog(null,"Are you sure you want to delete a member?","Delete Member",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
                    if(JOptionPane.YES_OPTION==n){
                        String query="DELETE FROM member_records WHERE mid="+temp2;
                        connect.executeSql(query,"Delete Member");
                        model.removeRow(table.getSelectedRow());
                    }
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }else
            JOptionPane.showMessageDialog(null,"Please select a member first","Delete Member",JOptionPane.ERROR_MESSAGE);
    }
    
//    public static void main(String[] args){
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new EditMemberFinal();
//            }
//        });
//    }
}

