package projectlibrary;

import UtiltiyClasses.DatabaseConnectivity;
import ManageBooks.SearchBooks;
import ManageBooks.IssueReceiveFinal;
import ManageBooks.AddBooksFinal;
import ManageMember.EditMemberFinal;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashBoard extends JFrame implements WindowListener{
    private JButton addNew,books,issue,receive,search,help,logOut;
    String sql,userName;
    private JLabel label1,userInfo;
    private JTextField fld;
    private JPanel pnl, pan1,container;
    private ResultSet rs;
    private int iid;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public DashBoard(int test, String username){
        super("Library Management System");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
        String sql=" SELECT uid,username,isadmin from user_login_details WHERE username= '"+username+"'";
        try{
            rs=connect.returnResult(sql);
            while(rs.next()){
                iid=rs.getInt(1);
                userName=rs.getString(2);
                if(rs.getString(3).equals("1"))
                    userName=userName+"\\Admin";
            }
        }catch(Exception e){
            System.out.println(e);
        }
        Border border = BorderFactory.createLineBorder(Color.GRAY, 2);

        container= new JPanel(new BorderLayout());//to adjust userinformation
 
        pan1=new JPanel(new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(0,260,0,230);
        g.gridx=g.gridy=0;
        userInfo=new JLabel("Logged In as:  "+userName);
        label1=new JLabel();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        label1.setText("Date: "+sdf.format(new Date()));
        logOut=new JButton("Log-Out");
        logOut.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int n=JOptionPane.showConfirmDialog(null,"Are you sure you want to log-out?","User Log Out",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if(n==JOptionPane.YES_OPTION){
                    new MainFrame();
                    dispose();
                }
            }
        });
        pan1.setBorder(border);
        pan1.add(label1,g);
        g.gridx++;
        pan1.add(userInfo,g);
        g.gridx++;
        pan1.add(logOut,g);
        pan1.setBackground(Color.LIGHT_GRAY);
        container.add(pan1, BorderLayout.NORTH);


        pnl=new JPanel();
        pnl.setLayout(new GridLayout(2,3,10,15));
        pnl.setBorder(new EmptyBorder(0, 20, 20, 20));

        addNew=new JButton("Issue Return Books");
        addNew.setIcon(new javax.swing.ImageIcon("Icons\\issueBooks1.png"));
        addNew.setToolTipText("Issue Books");
        pnl.add(addNew);

        addNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IssueReceiveFinal(0,iid);
            }
        });

        books=new JButton("Add Book");
        books.setIcon(new javax.swing.ImageIcon("Icons\\addBooks1.png"));
        books.setToolTipText("Add Books");
        pnl.add(books);
        books.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddBooksFinal(test);
            }
        });

        issue=new JButton("Manage Member");
        issue.setIcon(new javax.swing.ImageIcon("Icons\\addMembers1.png"));
        issue.setToolTipText("Manage Members");
        pnl.add(issue);

        issue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditMemberFinal(iid);
            }
        });

        receive=new JButton("Manage Account");
        receive.setIcon(new javax.swing.ImageIcon("Icons\\admin1.png"));
        receive.setToolTipText("Manage your Account");
        pnl.add(receive);

        receive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminPanel(test,iid);
            }
        });


        search=new JButton("Search");
        search.setIcon(new javax.swing.ImageIcon("Icons\\search1.png"));
        pnl.add(search);
        search.setToolTipText("Search");

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchBooks();
            }
        });

        help=new JButton("Contact Developers");
        help.setIcon(new javax.swing.ImageIcon("Icons\\cntc1.png"));
        help.setToolTipText("Contact Developers");
        pnl.add(help);

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ContactDeveloper();
            }
        });

        container.add(pnl,BorderLayout.CENTER);
        add(container);


    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                DashBoard dashBoard = new DashBoard();
//            }
//        });
//    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (JOptionPane.showConfirmDialog(null,"Are you sure you want to close this window?", "Close Window?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
            System.exit(0);
        }else{
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
