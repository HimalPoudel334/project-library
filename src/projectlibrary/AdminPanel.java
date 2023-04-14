package projectlibrary;

import UtiltiyClasses.DatabaseConnectivity;
import ManageSuppliers.SuppliersDetails;
import ManageUsers.ManageUser;
import UtiltiyClasses.TatkalKoLagi;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.JXDatePicker;

public class AdminPanel extends JFrame {
    private JPanel mainPan,contentPan,diaInfoPnl,homePnl,labelPan;
    private  JLabel label,usernName,oldPass,newPass,confirmPass,infoLabel;
    private JButton homeBtn,changePassBtn,manageUserBtn,submitBtn,cancelBtn,reportBtn,supplier,incomeExpen;
    private JTextField userF;
    private JXDatePicker frm,to;
    private String username;
    private int money;
    private JPasswordField oldPassF,newPassF,confirmPassF;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public AdminPanel(int test,int iid){
        super("Admin Panel");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(520,300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        homePnl=new JPanel();
        labelPan=new JPanel(new FlowLayout());
        homePnl.setLayout(new FlowLayout(0,5,5));
        homeBtn=new JButton("Home");
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        homePnl.add(homeBtn,FlowLayout.LEFT);
        add(homePnl,BorderLayout.NORTH);
        
        String sql="SELECT username FROM user_login_details WHERE uid="+iid;
        ResultSet rs=connect.returnResult(sql);
        try{
            while(rs.next())
                username=rs.getString(1);
        }catch(SQLException e){
            
        }
        
        frm=new JXDatePicker();
        frm.setDate(Calendar.getInstance().getTime());
        frm.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        
        to=new JXDatePicker();
        to.setDate(Calendar.getInstance().getTime());
        to.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        
        labelPan.add(new JLabel("Book Issued Report from "));
        labelPan.add(frm);
        labelPan.add(new JLabel("To "));
        labelPan.add(to);
        
        contentPan=new JPanel();
        contentPan.setLayout(new FlowLayout());
        
        changePassBtn=new JButton("Change Password");
        changePassBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordDialog(iid);
            }
        });
        
        manageUserBtn=new JButton("Manage User");
        manageUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ManageUser(iid);
                dispose();
            }
        });
        reportBtn=new JButton("Generate");
        reportBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                TatkalKoLagi tat=new TatkalKoLagi();
                String fm=tat.returnDate(frm.getDate());
                String t=tat.returnDate(to.getDate());                
                try {
                    InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\GenerateReport1.jrxml"));
                    JasperDesign jd=JRXmlLoader.load(in);   
                    JasperReport jr=JasperCompileManager.compileReport(jd);
                    HashMap param=new HashMap();
                    param.put("From",fm);
                    param.put("To",t);
                    JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                    JasperViewer.viewReport(j, false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex); 
                }
            }
        });
        labelPan.add(reportBtn);
        supplier=new JButton("Supplier Details");
        supplier.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new SuppliersDetails(test);
                dispose();
            }
        
        });
        
        incomeExpen=new JButton("Icome-Expenses Report");
        incomeExpen.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String sql="SELECT SUM(rupees) AS price FROM paid_suppliers";
                ResultSet rs=connect.returnResult(sql);
                try{
                    while(rs.next()){
                        money=rs.getInt(1);
                    }
                    InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\IncomeExpensesReport.jrxml"));
                    JasperDesign jd=JRXmlLoader.load(in);   
                    JasperReport jr=JasperCompileManager.compileReport(jd);
                    HashMap param=new HashMap();
                    param.put("username",username);
                    param.put("toCustomer",money);
                    param.put("From","");
                    param.put("To","");
                    JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                    JasperViewer.viewReport(j, false);
                }catch(SQLException ex) {} catch (FileNotFoundException ex) {   
                    Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JRException ex) {
                    Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
                }   
            }
        });
        
        if(test==1)
            contentPan.add(manageUserBtn);
        contentPan.add(supplier);
        contentPan.add(changePassBtn);
        contentPan.add(labelPan);
        //contentPan.add(incomeExpen);
        
        label=new JLabel("Choose what do you want to do?");
        contentPan.add(label);

        add(contentPan,BorderLayout.CENTER);
    }
    public void changePasswordDialog(int id){
        JDialog cp=new JDialog();
        cp.setTitle("Change Password");
        cp.setLocationRelativeTo(homeBtn);
        cp.setVisible(true);
        cp.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cp.setSize(600,300);
        cp.setLayout(new BorderLayout());
        diaInfoPnl=new JPanel();
        diaInfoPnl.setBackground(Color.PINK);
        infoLabel=new JLabel("Please fill the following details");
        diaInfoPnl.add(infoLabel);
        cp.add(diaInfoPnl,BorderLayout.NORTH);
        mainPan=new JPanel();
        mainPan.setLayout(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints();
        g.anchor=GridBagConstraints.FIRST_LINE_START;
        g.insets=new Insets(5,5,5,5);
        g.gridy=g.gridx=0;
        usernName=new JLabel("Username:");
        mainPan.add(usernName,g);
        g.gridx++;
        userF=new JTextField(20);
        userF.setText(username);
        userF.setEditable(false);
        mainPan.add(userF,g);
        g.gridx=0;
        g.gridy++;
        oldPass=new JLabel("Current Password:");
        mainPan.add(oldPass,g);
        g.gridx++;
        oldPassF=new JPasswordField(20);
        mainPan.add(oldPassF,g);
        g.gridx=0;
        g.gridy++;
        newPass=new JLabel("New Password:");
        mainPan.add(newPass,g);
        g.gridx++;
        newPassF=new JPasswordField(20);
        mainPan.add(newPassF,g);
        g.gridx=0;
        g.gridy++;
        confirmPass=new JLabel("Confirm Password:");
        mainPan.add(confirmPass,g);
        g.gridx++;
        confirmPassF=new JPasswordField(20);
        mainPan.add(confirmPassF,g);
        g.gridy++;
        cancelBtn=new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cp.dispose();
            }
        });
        mainPan.add(cancelBtn,g);
        g.insets=new Insets(5,100,5,5);
        submitBtn=new JButton("Change Password");
        submitBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){
                changePassword();
            }
        });
        submitBtn.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    changePassword();    
                }  
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        
        mainPan.add(submitBtn,g);

        cp.add(mainPan,BorderLayout.CENTER);
    }
    public void changePassword(){
        String what="Change Password";
        String username=userF.getText();
        String oldP=String.valueOf(oldPassF.getPassword());
        String newPa=String.valueOf(newPassF.getPassword());
        String confirmNewPass=String.valueOf(confirmPassF.getPassword());
        if(newPa.equals(confirmNewPass)){
            int isValid=connect.validateMember(username,oldP);
            if(isValid==1)
                if(newPa.equals(confirmNewPass)){
                    String change="UPDATE user_login_details SET password='"+newPa+"' WHERE username='"+username+"' AND password='"+oldP+"'";
                    connect.executeSql(change,what);
                }
            else
                JOptionPane.showMessageDialog(null,"Password doesnot match","Change Password",JOptionPane.ERROR_MESSAGE);
        }
        else
            JOptionPane.showMessageDialog(null,"New Password not Confirmed!","Change Password",JOptionPane.ERROR_MESSAGE);
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                AdminPanel ap=new AdminPanel();
//            }
//        });
//    }
}
