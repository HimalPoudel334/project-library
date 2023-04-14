package ManageBooks;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXDatePicker;
import UtiltiyClasses.DatabaseConnectivity;
import UtiltiyClasses.TatkalKoLagi;
import UtiltiyClasses.Validater;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class IssueReceiveFinal extends JFrame {

    private JPanel panel1, topPanCont, topPan, middlePan, buttomPan, backPnl, container, jd, btnPnl;
    private JButton backBtn, issueBtn, receiveBtn, calFine, canclBtn, doneBtn, viewDetailsBtn,changeSetting;
    private JLabel jothKo, infoLabel, mid, bid, bname, mname, issueDate, retDate, MId, memName;
    private JTextField retDateF, bidF, bnameF, MidF, MemNameF,locationF;
    private JScrollPane sp;
    private DefaultTableModel model;
    private ResultSet res;
    private final JTable table;
    private JDialog dialog;
    private Border border;
    private JXDatePicker issueDateF;
    private int mIdd,f9=0;
    private static int maxBookIssue=5,fineRate=5;
    private boolean uid;
    private String naaam, SQL,usrName;
    DatabaseConnectivity connect = new DatabaseConnectivity();
    TatkalKoLagi tat = new TatkalKoLagi();

    public IssueReceiveFinal(int temp,int userId) {
        super("Issue/Receive Books");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setSize(800, 500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //naaam=MemNameF.getText();
        backPnl = new JPanel();
        topPan = new JPanel();
        panel1 = new JPanel();
        topPanCont = new JPanel();
        middlePan = new JPanel();
        buttomPan = new JPanel();

        infoLabel = new JLabel();

        backPnl.setLayout(new FlowLayout(0));
        backBtn = new JButton("Back");
        changeSetting=new JButton("Change Settings");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        backPnl.add(backBtn);
        add(backPnl, BorderLayout.NORTH);

        panel1.setBackground(Color.PINK);
        panel1.setLayout(new GridBagLayout());

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.gridy = gc.gridx = 0;
        gc.anchor = GridBagConstraints.WEST;

        MId = new JLabel("Member ID:");
        panel1.add(MId, gc);
        gc.gridy++;
        memName = new JLabel("Member Name:");
        panel1.add(memName, gc);

        gc.gridy = 0;
        gc.gridx++;
        MidF = new JTextField(10);
        MidF.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER)
                    whileTempIsZero();
            }
        });
        panel1.add(MidF, gc);
        gc.gridy++;
        MemNameF = new JTextField(20);
        panel1.add(MemNameF, gc);
        gc.gridx++;
        viewDetailsBtn = new JButton("View Details");
        panel1.add(viewDetailsBtn, gc);

        container = new JPanel();
        container.setLayout(new BorderLayout());

        topPan.setBackground(Color.PINK);
        topPan.setLayout(new FlowLayout());
        topPan.setVisible(false);

        topPanCont.setLayout(new BorderLayout());
        topPanCont.add(backPnl, BorderLayout.NORTH);
        topPanCont.add(panel1, BorderLayout.CENTER);
        topPanCont.add(topPan, BorderLayout.SOUTH);

        middlePan.setLayout(new BorderLayout());
        middlePan.setVisible(false);

        buttomPan.setLayout(new FlowLayout());
        buttomPan.setVisible(false);

        issueBtn = new JButton("Issue");
        issueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueBtnClicked();
            }
        });
        receiveBtn = new JButton("Receive");
        receiveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiveBtnClicked();
            }
        });
        calFine = new JButton("Print Bill");
        calFine.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                calFineBtnClicked();
            }
        });
        
        changeSetting.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                settings();
            }        
        });
        String query="SELECT username,isadmin FROM user_login_details WHERE uid="+userId;
        res=connect.returnResult(query);
        try{
            while(res.next()){
                usrName=res.getString(1);
                uid=res.getBoolean(2);
            }
        }catch(SQLException e){}
        
        if(uid)
            backPnl.add(changeSetting);
        
        buttomPan.add(receiveBtn);
        buttomPan.add(issueBtn);
        buttomPan.add(calFine);

        String columns[] = {"Book ID", "Book Name","Book Number","Location", "Issued Date", "Renew After", "Fine(In Rs)"};
        table = new JTable();
        model = new DefaultTableModel();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);//automatically sorts row
        table.setRowSelectionAllowed(true);//allows selection of entire row
        model.setColumnIdentifiers(columns);
        sp = new JScrollPane(table);
        if(temp==0){
            viewDetailsBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    whileTempIsZero();
                }
            });
        }else{
            viewDetailsBtn.setEnabled(false);
            mIdd=temp;
            SQL = "SELECT `First Name`,members_middle_name.`Middle Name`,`Last Name`,member_records.mid FROM member_records LEFT JOIN members_middle_name ON members_middle_name.mid=member_records.mid WHERE member_records.mid=" +temp;
            try{
                res = connect.returnResult(SQL);
                topPan.setVisible(true);
                middlePan.setVisible(true);
                buttomPan.setVisible(true);
                while(res.next())
                    naaam = res.getString(1) + " " + res.getString(2) + " " + res.getString(3);
            }catch(SQLException se){
                System.out.println(se.getMessage());
            }
            MidF.setText(""+temp);
            infoLabel.setText("Book Issue\\Receive Details of  " + naaam);
            issueBtn.setEnabled(false);
            SQL = "SELECT b.`Book Id`, `Book Name`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, CONCAT_WS('-',ro.`Location`,ro.`Room Id`,sh.`Shelf Name`,sh.`Shelf Id`,ra.`Rack Id`) AS `Book Location`,b.issue_date,b.renew_after FROM `book_records` b JOIN (book_and_shelf bs LEFT JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`BOok Id`=bs.`Book Id` WHERE b.mid=" +temp;
            try {
                res = connect.returnResult(SQL);
                sp.repaint();
                model.getDataVector().removeAllElements();
                while (res.next()) {
                    model.addRow(new String[]{res.getString(1), res.getString(2), res.getString(3), res.getString(4),res.getString(5),res.getString(6),viewFine(res.getString(5),res.getString(6))});
                }
            } catch (SQLException eee) {

            }
        }
        infoLabel.setFont(new Font("Tahoma", 1, 18));
        topPan.add(infoLabel);

        middlePan.add(sp, BorderLayout.CENTER);
        container.add(topPanCont, BorderLayout.NORTH);
        container.add(middlePan, BorderLayout.CENTER);
        container.add(buttomPan, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);
        MidF.requestFocusInWindow();
    }
    
    /**Executes set of codes if Issue button is clicked*/
    public void issueBtnClicked() {
        if(table.getRowCount()>=maxBookIssue){
            JOptionPane.showMessageDialog(null,"Quota already fulfilled. Cannot issue more than 5 books\nPlease return a book first!","Issue Book",JOptionPane.ERROR_MESSAGE);
            issueBtn.setEnabled(false);
        }
        else{
            dialog = new JDialog();
            dialog.setTitle("Issue Book");
            dialog.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
            dialog.setSize(515, 405);
            dialog.setLocationRelativeTo(table);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
            dialog.setLayout(new BorderLayout());

            border = BorderFactory.createTitledBorder(border, "Please Fill the Following Details", 0, 0, new Font("Tahoma", 0, 14), Color.RED);

            jd = new JPanel();
            jd.setLayout(new GridBagLayout());
            jd.setBorder(border);

            btnPnl = new JPanel(new FlowLayout());

            GridBagConstraints g = new GridBagConstraints();
            g.gridx = g.gridy = 0;
            g.anchor = GridBagConstraints.LINE_END;
            g.insets = new Insets(8, 10, 0, 10);

            mid = new JLabel("Member ID:");
            mname = new JLabel("Member Name:");
            bid = new JLabel("Book ID:");
            bname = new JLabel("Book Name:");
            issueDate = new JLabel("Issue Date:");
            retDate = new JLabel("Renew After:");
            jothKo = new JLabel(naaam);

            bidF = new JTextField(10);
            bnameF = new JTextField(20);
                                    
            bnameF.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    onBookFieldPressed();
                }
            });
            locationF=new JTextField(20);
            retDateF = new JTextField(10);
            retDateF.setText("30");

            canclBtn = new JButton("Cancel");
            canclBtn.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ee){
                    dialog.dispose();
                }
            });
            doneBtn = new JButton("Done");
            doneBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doneBtnClicked();
                }
            });

            issueDateF = new JXDatePicker();
            issueDateF.setDate(Calendar.getInstance().getTime());
            issueDateF.setFormats(new SimpleDateFormat("yyyy.MM.dd"));

            jd.add(mid, g);
            g.gridy++;
            jd.add(mname, g);
            g.gridy++;
            jd.add(bid, g);
            g.gridy++;
            jd.add(bname, g);
            g.gridy++;
            jd.add(new JLabel(""));
            g.gridy++;
            jd.add(new JLabel("Book Location:"),g);
            g.gridy++;
            jd.add(issueDate, g);
            g.gridy++;
            jd.add(retDate, g);

            g.gridx++;
            g.gridy = 0;
            g.anchor = GridBagConstraints.LINE_START;
            jd.add(new JLabel(""+mIdd),g);
            g.gridy++;
            jd.add(jothKo, g);
            g.gridy++;
            jd.add(bidF, g);
            g.gridy++;
            jd.add(bnameF, g);
            g.gridy++;
            jd.add(new JLabel("Room Location-Room Number-Shelf Name-Shelf Id-Rack Id"),g);
            g.gridy++;
            jd.add(locationF,g);
            g.gridy++;
            jd.add(issueDateF, g);
            g.gridy++;
            JPanel daysKoLagi = new JPanel(new FlowLayout());
            daysKoLagi.add(retDateF);
            daysKoLagi.add(new JLabel("Days"));
            jd.add(daysKoLagi, g);
            
            btnPnl.add(canclBtn);
            btnPnl.add(doneBtn);

            dialog.add(jd, BorderLayout.CENTER);
            dialog.add(btnPnl, BorderLayout.SOUTH);
            bnameF.requestFocusInWindow();
        }
    }
    
    /**Executes set of codes if Receive button is clicked*/
    public void receiveBtnClicked() {
        if (table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book first", "Receive Book", JOptionPane.ERROR_MESSAGE);
        }
        else {
            String val = String.valueOf(table.getValueAt(table.getSelectedRow(), 0));
            int value = Integer.parseInt(val);
            int fine=Integer.parseInt(String.valueOf(table.getValueAt(table.getSelectedRow(),6)));
            if(fine>0){
                int yes=JOptionPane.showConfirmDialog(null,"Fine is not cleared!\nPlease pay fine amount first!","Receive Books",JOptionPane.YES_NO_OPTION,JOptionPane.ERROR_MESSAGE);
                if(yes==JOptionPane.YES_OPTION){
                    String sql = "UPDATE book_records SET mid=NULL,`Is Available`=true,issue_date=NULL WHERE `Book Id`=" + value;
                    f9+=fine;
                    if(connect.executeSql(sql, "Receive Book")){
                        model.removeRow(table.getSelectedRow());
                    }
                }
            }
            else{
                String sql = "UPDATE book_records SET mid=NULL,`Is Available`=true,issue_date=NULL WHERE `Book Id`=" + value;
                connect.executeSql(sql, "Receive Book");
                model.removeRow(table.getSelectedRow());
            }
            issueBtn.setEnabled(true);
        }
    }

    /**Executes set of codes if Calculate Fine button is clicked*/
    public void calFineBtnClicked() {
        int fine=0;
        for(int i=0;i<table.getRowCount();i++){
            fine=fine+Integer.parseInt(String.valueOf(table.getValueAt(i,6)));
        }
        String query="UPDATE member_records SET Fine="+fine+" WHERE mid="+mIdd;
        connect.executeSql(query,"Calculate Fine");
        int yes=JOptionPane.showConfirmDialog(null,"Total fine is Rs "+fine+"\n Receive?","Calculate Fine",JOptionPane.YES_NO_OPTION);
        if(yes==JOptionPane.YES_OPTION){
            try {
                    InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\FineReceived.jrxml"));
                    JasperDesign jd=JRXmlLoader.load(in);   
                    JasperReport jr=JasperCompileManager.compileReport(jd);
                    HashMap param=new HashMap();
                    param.put("MemId",mIdd);
                    param.put("username",usrName);                    
                    JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                    JasperViewer.viewReport(j, false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex); 
                }
        }
    }
    /**Calculates the fine for each book in the table
     * @param issueDate
     * @param renewAfter
     * @return */
    public String viewFine(String issueDate,String renewAfter){
        LocalDate dt=LocalDate.now();
        long fine=0;
        String fn;
        LocalDate dat=LocalDate.parse(issueDate);
        Period diff=dat.until(dt);
        long days=diff.toTotalMonths()*30+diff.getDays();
        int rnu=Integer.parseInt(renewAfter);
        if(days>rnu)
            fine=(days-rnu)*fineRate;
        fn=String.valueOf(fine);
        return fn;      
    }

    /**Executes set of codes if Done button is clicked*/
    public void doneBtnClicked() {
        if(table.getRowCount()>=maxBookIssue){
            JOptionPane.showMessageDialog(null,"Quota already fulfilled. Cannot issue more than 5 books","Issue Book",JOptionPane.ERROR_MESSAGE);
            dialog.dispose();
            issueBtn.setEnabled(false);
        }
        else{
            int BID = Integer.parseInt(bidF.getText());
            try{
                String sql="SELECT * FROM book_records WHERE `Book Id`="+BID;
                res=connect.returnResult(sql);
                if(!(res.next()))
                    JOptionPane.showMessageDialog(null,"No such book available","Issue Book",JOptionPane.ERROR_MESSAGE);
            }catch(SQLException ee){
                
            }
            int RETURNAFTER = Integer.parseInt(retDateF.getText());
            Date ISSUEDATE = issueDateF.getDate();
            String isd = tat.returnDate(ISSUEDATE);
            
            String query = "UPDATE book_records SET `Is Available`=0, mid=" + mIdd + ",issue_date=Date'" + isd + "',renew_after=" + RETURNAFTER + " WHERE `Book Id`=" + BID;
            connect.executeSql(query, "Issue Book");
            query = "SELECT b.`Book Id`, `Book Name`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, CONCAT_WS('-',ro.`Location`,ro.`Room Id`,sh.`Shelf Name`,sh.`Shelf Id`,ra.`Rack Id`) AS `Book Location`,b.issue_date,b.renew_after FROM `book_records` b JOIN (book_and_shelf bs LEFT JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`BOok Id`=bs.`Book Id` WHERE b.mid=" + mIdd;
        try {
            res = connect.returnResult(query);
            sp.repaint();
            model.getDataVector().removeAllElements();
            while (res.next()) {
                model.addRow(new String[]{res.getString(1), res.getString(2), res.getString(3),res.getString(4),res.getString(5),res.getString(6),viewFine(res.getString(5),res.getString(6))});
            }
        } catch (SQLException eee) {

        }
            bnameF.setText("");
            bidF.setText("");
            locationF.setText("");
            bnameF.requestFocusInWindow();
        }
    }
    
    /**Executes set of codes if bnameF(Search Bar) is pressed*/
    public void onBookFieldPressed() {
        if (bnameF.getText().length() != 0) {
            JPopupMenu popup = new JPopupMenu();
            popup.setAutoscrolls(true);
            popup.setEnabled(true);
            String sql = "SELECT b.`Book Id`, `Book Name`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, CONCAT_WS('-',ro.`Location`,ro.`Room Id`,sh.`Shelf Name`,sh.`Shelf Id`,ra.`Rack Id`) AS `Book Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`BOok Id`=bs.`Book Id` WHERE b.`Book Name` LIKE '"+bnameF.getText()+"%' AND `Is Available`=1 LIMIT 5";
            ResultSet re = connect.returnResult(sql);
            try {
                if(re.next())
                    do{
                        String bi=re.getString(1);
                        String bn=re.getString(2);
                        String bloc=re.getString(4);
                        popup.add(new JMenuItem(bn)).addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent e){
                                bidF.setText(bi);
                                bnameF.setText(bn);
                                locationF.setText(bloc);
                            }
                        });                                        
                    }while(re.next());
                else{
                    JOptionPane.showMessageDialog(null,"No such book available","Issue book",JOptionPane.ERROR_MESSAGE);
                    bnameF.requestFocus();
                }
            } catch (SQLException exc) {
                System.out.println("Error from onBookFieldPressed " + exc);
            }
            popup.show(bnameF,0,25);
        }
    }
    public void whileTempIsZero(){
        mIdd = Integer.parseInt(MidF.getText());
        SQL = "SELECT `First Name`,members_middle_name.`Middle Name`,`Last Name`,member_records.mid FROM member_records LEFT JOIN members_middle_name ON members_middle_name.mid=member_records.mid WHERE member_records.mid="+ mIdd;
        try {
            res = connect.returnResult(SQL);
            if (!res.next()) {
                JOptionPane.showMessageDialog(null, "Member not found", "View Member Details", JOptionPane.ERROR_MESSAGE);
                topPan.setVisible(false);
                middlePan.setVisible(false);
                buttomPan.setVisible(false);
            } else {
                topPan.setVisible(true);
                middlePan.setVisible(true);
                buttomPan.setVisible(true);
                naaam = res.getString(1) + " " + res.getString(2) + " " + res.getString(3);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        MemNameF.setText(naaam);
        infoLabel.setText("Book Issue\\Receive Details of  " + naaam);
        SQL = "SELECT b.`Book Id`, `Book Name`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, CONCAT_WS('-',ro.`Location`,ro.`Room Id`,sh.`Shelf Name`,sh.`Shelf Id`,ra.`Rack Id`) AS `Book Location`,b.issue_date,b.renew_after FROM `book_records` b JOIN (book_and_shelf bs LEFT JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`BOok Id`=bs.`Book Id` WHERE b.mid=" + mIdd;
        try {
            res = connect.returnResult(SQL);
            sp.repaint();
            model.getDataVector().removeAllElements();
            while (res.next()) {
                model.addRow(new String[]{res.getString(1), res.getString(2), res.getString(3),res.getString(4),res.getString(5),res.getString(6),viewFine(res.getString(5),res.getString(6))});
            }
        } catch (SQLException eee) {

        }
        issueBtn.setEnabled(true);
    }
    public void settings(){
        JDialog sd=new JDialog();
        sd.setTitle("Issue Book");
        sd.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        sd.setSize(400,200);
        sd.setLocationRelativeTo(table);
        sd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        sd.setVisible(true);
        sd.setLayout(new BorderLayout());
        
        sd.add(new JLabel("Please Fill The Following Details"),BorderLayout.NORTH);
                
        JPanel pan=new JPanel(new GridBagLayout());
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5,5,5,5);
        gbc.anchor=GridBagConstraints.LINE_END;
        gbc.gridx=gbc.gridy=0;
        
        try{
            String sql="SELECT * FROM issue_settings WHERE id=1";
            res=connect.returnResult(sql);
            while(res.next()){
                maxBookIssue=res.getInt(2);
                fineRate=res.getInt(3);
            }
        }catch(SQLException e){
            
        }
        
        pan.add(new JLabel("Enter Book Limit:"),gbc);
        gbc.gridy++;
        pan.add(new JLabel("Enter Fine Rate(In Rs):"),gbc);
        gbc.gridy++;
        
        gbc.gridy=0;
        gbc.gridx++;
        gbc.anchor=GridBagConstraints.LINE_START;
        JSpinner spin=new JSpinner();
        spin.setValue(maxBookIssue);
        spin.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                if(table.getRowCount()>Integer.parseInt(spin.getValue().toString())){
                    getToolkit().beep();
                    spin.setValue(table.getRowCount());
                }
            }
        });
        pan.add(spin,gbc);
        gbc.gridy++;
        JTextField fld=new JTextField(10);
        fld.setText(""+fineRate);
        pan.add(fld,gbc);
        
        JButton don=new JButton("Done");
        don.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(!(Validater.compare(fld.getText(),"N"))){
                    JOptionPane.showMessageDialog(null,"Please enter fine rate","Change Settings",0);
                    fld.requestFocus();
                }
                else{
                    maxBookIssue=Integer.parseInt(spin.getValue().toString());                                 
                    fineRate=Integer.parseInt(fld.getText());
                    String sql="UPDATE issue_settings SET no_of_books='"+maxBookIssue+"', fine_rate='"+fineRate+"' WHERE id=1";
                    connect.executeSql(sql,"Change Settings");
                    sd.dispose();
                }                
            }
        });
        gbc.gridy++;
        pan.add(don,gbc);
        
        sd.add(pan,BorderLayout.CENTER);
        
        
        
        
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new IssueReceiveFinal(0,1);
            }
        });
    }

}
