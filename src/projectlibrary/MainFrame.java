package projectlibrary;

import UtiltiyClasses.DatabaseConnectivity;
import ManageUsers.AddNewUser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame{
    private final JLabel username;
    private final JLabel password;
    private final JTextField user;
    private final JPasswordField passs;
    private final JButton login,registerBtn;
    private final JPanel body;
    private String userName,passWord;
    private int test;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public MainFrame(){
        super("User Login");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setSize(400,200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new FlowLayout());
        body=new JPanel(new GridBagLayout());
        //body.setLayout();
                
        username =new JLabel();
        username.setText("Username:");
        password = new JLabel("Password:");
        
        user=new JTextField(15);
        passs=new JPasswordField(15);
        login =new JButton("Login");
        registerBtn=new JButton("Register");
        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    new AddNewUser(-1);
                    dispose();
            }
        });
        
        passs.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    checkLogin();
            }
        });

        // creates a constraints object
        GridBagConstraints c = new GridBagConstraints();

        // insets for all components
        c.insets = new Insets(2, 2, 2, 2);

        // column 0
        c.gridx = 0;

        // row 0
        c.gridy = 0;
        body.add(username,c);

        c.gridx++;
        body.add(user,c);

        c.gridy++;
        body.add(passs,c);

        c.gridx = 0;
        body.add(password,c);
//        login.setLayout(new FlowLayout(FlowLayout.RIGHT));
        c.gridx++;
        c.gridy++;
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });
        login.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    checkLogin();    
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        body.add(login,c);
        c.gridy++;
        
        String sql="SELECT * FROM user_login_details";
        ResultSet rs=connect.returnResult(sql);
        try {
            if(!rs.next()){
                login.setVisible(false);
                user.setEditable(false);
                passs.setEditable(false);
                body.add(registerBtn,c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        add(body);

    }
    public void checkLogin(){
        userName=user.getText();
        passWord=String.valueOf(passs.getPassword());
        test=connect.validateMember(userName,passWord);
        if(test==1||test==2){
            new DashBoard(test,userName);
            dispose();
        }                  
    }
    public static void  main( String [] args){
//    {SwingUtilities.invokeLater(new Runnable() {
//        @Override
//        public void run() {
//            new MainFrame();
//        }
//    });
//
//    }
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());//sets Nimbus look and feel
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
