
package ManageMember;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXDatePicker;
import UtiltiyClasses.DatabaseConnectivity;
import UtiltiyClasses.TatkalKoLagi;
import UtiltiyClasses.Validater;
import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class MemberRegisterFinal extends JFrame {
    private JPanel homePnl,container,infoPnl,formPnl,genderPnl,imagePnl,imgInfoPnl;
    private JButton homeBtn,addBtn,browseBtn,updateBtn;
    private JLabel mIddd,img,eff_Frm,infoLabel,imgLabel,memId,fName,mName,lName,gender,male,female,otherL,dob,nationality,citznNo,address,contact,email,effFrm,effTo,memType,libDepo;
    private Border innerBorder,outerBorder;
    private JTextField fNameF,mNameF,lNameF,nationalityF,citzenNoF,addressF,contactF,emailF,libDepoF;
    private JRadioButton maleBtn,femaleBtn,other;
    private ButtonGroup genderGroup;
    private JXDatePicker dobF,effToF;
    private ResultSet rs;
    private File selectedFile;
    private int temp,idddd,idd;
    private String usrId;
    private JComboBox<String> memTypeF;
    private String[] type={"Full Time","Partial"};
    private boolean flag;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    
    public MemberRegisterFinal(int idddd,int userId){
        super("Member Registration");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(540,660);
        setLocationRelativeTo(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        idd=userId;
        homePnl=new JPanel();
        homePnl.setLayout(new FlowLayout(0));
        homeBtn=new JButton("Back");
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditMemberFinal(userId);
                dispose();
            }
        });
        homePnl.add(homeBtn);
        add(homePnl,BorderLayout.NORTH);
                
        infoPnl=new JPanel();
        infoLabel=new JLabel("Member Registration");
        infoLabel.setFont(new Font("Tahoma",1,16));
        infoPnl.setBackground(Color.BLUE);
        infoPnl.add(infoLabel);
        
        innerBorder=BorderFactory.createTitledBorder(innerBorder, "Please Fill the Following Details",0,0,new Font("Tahoma",1,14), Color.RED);
        
        formPnl=new JPanel();
        formPnl.setLayout(new GridBagLayout());
        formPnl.setBackground(Color.PINK);
        formPnl.setBorder(innerBorder);
        GridBagConstraints c=new GridBagConstraints();
        c.insets=new Insets(3,5,2,5);
        c.anchor=GridBagConstraints.LINE_END;
        c.gridx=c.gridy=0;
                
        memId=new JLabel("Member ID:");
        fName = new JLabel("First Name:");
        mName=new JLabel("Middle Name:");
        lName=new JLabel("Last Name:");
        gender=new JLabel("Gender:");
        dob=new JLabel("Date Of Birth:");
        nationality=new JLabel("Nationality:");
        citznNo=new JLabel("Citizenship No:");
        address=new JLabel("Address:");
        contact=new JLabel("Phone No:");
        email=new JLabel("Email:");
        effFrm=new JLabel("Effective From:");
        effTo=new JLabel("Effective To:");
        memType=new JLabel("Member Type:");
        libDepo=new JLabel("Library Deopsit:");
        
        String query="SELECT MAX(mid) FROM member_records";
        rs=connect.returnResult(query);
        try{
            while(rs.next())
                temp=rs.getInt(1)+1;
        }catch(Exception ex){
            System.out.println("Error in mid label ="+ex);
        }
        query="SELECT username FROM user_login_details WHERE uid="+userId;
        rs=connect.returnResult(query);
        try{
            while(rs.next())
                usrId=rs.getString(1);
        }catch(Exception ex){
            System.out.println("Error in username label ="+ex);
        }
        
        mIddd=new JLabel(""+(temp));
        fNameF=new JTextField(20);
        mNameF=new JTextField(20);
        lNameF=new JTextField(20);
        nationalityF=new JTextField(20);
        citzenNoF=new JTextField(20);
        addressF=new JTextField(20);
        contactF=new JTextField(20);
        emailF=new JTextField(20);
        libDepoF=new JTextField(10);
               
        maleBtn=new JRadioButton("Male");
        maleBtn.setSelected(true);
        maleBtn.setActionCommand("Male");
        femaleBtn=new JRadioButton("Female");
        femaleBtn.setActionCommand("Female");                
        other=new JRadioButton("Other");
        other.setActionCommand("Other");////sets the value of selected radio button
        genderGroup=new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(other);
        
        genderPnl=new JPanel();
        genderPnl.setLayout(new FlowLayout(0));
        genderPnl.setBackground(Color.PINK);
        genderPnl.add(maleBtn);
        genderPnl.add(femaleBtn);
        genderPnl.add(other);
        
        memTypeF=new JComboBox<>(type);
        eff_Frm=new JLabel();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        eff_Frm.setText(""+sdf.format(new Date()));
        
        dobF=new JXDatePicker();
        dobF.setDate(Calendar.getInstance().getTime());
        dobF.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
                
        effToF = new JXDatePicker();
        effToF.setDate(Calendar.getInstance().getTime());
        effToF.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        
        formPnl.add(memId,c);
        c.gridy++;
        formPnl.add(fName,c);
        c.gridy++;
        formPnl.add(mName,c);
        c.gridy++;
        formPnl.add(lName,c);
        c.gridy++;
        formPnl.add(gender,c);
        c.gridy++;
        formPnl.add(dob,c);
        c.gridy++;
        formPnl.add(nationality,c);
        c.gridy++;
        formPnl.add(citznNo,c);
        c.gridy++;
        formPnl.add(address,c);
        c.gridy++;
        formPnl.add(contact,c);
        c.gridy++;
        formPnl.add(email,c);
        c.gridy++;
        formPnl.add(effFrm,c);
        c.gridy++;
        formPnl.add(effTo,c);
        c.gridy++;
        formPnl.add(memType,c);
        c.gridy++;
        formPnl.add(libDepo,c);
        if(idddd!=0)
            getDataForEdit(idddd);
        c.gridx++;
        c.gridy=0;
        c.anchor=GridBagConstraints.LINE_START;
        formPnl.add(mIddd,c);
        c.gridy++;
        formPnl.add(fNameF,c);
        c.gridy++;
        formPnl.add(mNameF,c);
        c.gridy++;
        formPnl.add(lNameF,c);
        c.gridy++;
        formPnl.add(genderPnl,c);
        c.gridy++;
        formPnl.add(dobF,c);
        c.gridy++;
        formPnl.add(nationalityF,c);
        c.gridy++;
        formPnl.add(citzenNoF,c);
        c.gridy++;
        formPnl.add(addressF,c);
        c.gridy++;
        formPnl.add(contactF,c);
        c.gridy++;
        formPnl.add(emailF,c);
        c.gridy++;
        formPnl.add(eff_Frm,c);
        c.gridy++;
        formPnl.add(effToF,c);
        c.gridy++;
        formPnl.add(memTypeF,c);
        c.gridy++;
        formPnl.add(libDepoF,c);
        c.gridy++;
        addBtn=new JButton("Add Member");
        updateBtn=new JButton("Update");
        if(idddd==0)
            addBtn.setText("Add");
        else
            addBtn.setText("Update");
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(idddd==0){
                    onAddBtnClicked(idddd);
                }else{
                    onAddBtnClicked(idddd);
                }
            }
        });        
        addBtn.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    if(idddd==0){
                        onAddBtnClicked(idddd);                        
                    }else{
                        onAddBtnClicked(idddd);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        formPnl.add(addBtn,c);
                
        imagePnl=new JPanel();
        imagePnl.setLayout(null);
        imagePnl.setSize(90,80);
        imagePnl.setBackground(Color.CYAN);
//        imgInfoPnl=new JPanel();
//        imgInfoPnl.setLayout(new FlowLayout());
        imgLabel=new JLabel("A Recent PP Size Photo");
        imgLabel.setBounds(5,5,160,30);
        imagePnl.add(imgLabel);
        
        img=new JLabel();
        img.setBounds(5,40,150,150);
        Border brd=BorderFactory.createLineBorder(Color.BLACK,1);
        img.setBorder(brd);
        if(idddd>0)
            retriveImage(idddd);
        browseBtn=new JButton("Browse");
        browseBtn.setBounds(25,195,100,30);
        browseBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                //filter the files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
                file.addChoosableFileFilter(filter);
                int result = file.showOpenDialog(null);
                 //if the user click on save in Jfilechooser
                if(result == JFileChooser.APPROVE_OPTION){
                    selectedFile = file.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    img.setIcon(ResizeImage(path));
                }
                //if the user click on save in Jfilechooser
                else if(result == JFileChooser.CANCEL_OPTION){
                    System.out.println("No File Select");
                }
            }
        });
        
        imagePnl.add(imgLabel);
        imagePnl.add(img);
        imagePnl.add(browseBtn);
                                
        container=new JPanel();
        container.setLayout(new BorderLayout());
        container.add(infoPnl,BorderLayout.NORTH);
        container.add(formPnl,BorderLayout.WEST);
        container.add(imagePnl,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
        setVisible(true);
    }
    
    public void onAddBtnClicked(int idValue){
                
        TatkalKoLagi obj=new TatkalKoLagi();
        if(!(Validater.compare(fNameF.getText(),"Aa"))){
            showMessage("Enter first name properly","Error",0);
            fNameF.requestFocus();            
        }
        else if((mNameF.getText().length()>0)&&!(Validater.compare(mNameF.getText(),"Aa"))){
                showMessage("Enter middle name properly","Error",0);
                mNameF.requestFocus();
        }
        else if(!(Validater.compare(lNameF.getText(),"Aa"))){
            showMessage("Enter last name properly","Error",0);
            lNameF.requestFocus();
        }
        else if(!(Validater.compare(nationalityF.getText(),"Aa"))){
            showMessage("Please enter nationality","Error",0);
            nationalityF.requestFocus();
        }
        else if((citzenNoF.getText().length()>0)&&!(Validater.compare(citzenNoF.getText(),"N"))){
            showMessage("Citizenship number must be numeric","Error",0);
            citzenNoF.requestFocus();
        }
        else if(!(Validater.compare(addressF.getText(),"Aa"))){
            showMessage("Please enter member address","Error",0);
            addressF.requestFocus();
        }
        else if(!(Validater.compare(contactF.getText(),"N"))){
            showMessage("Contact number must be numeric","Error",0);
            contactF.requestFocus();
        }
        else if((emailF.getText().length()>0)&&!(Validater.compare(emailF.getText(),"E"))){
                showMessage("Invalid email address","Error",0);
                emailF.requestFocus();
        }
        else if(!(Validater.compare(libDepoF.getText(),"N"))){
            showMessage("Library deposit must be not be empty and must be numeric","Error",0);
            libDepoF.requestFocus();
        }
        else if((idValue==0)&&selectedFile==null){
            showMessage("Please select a photo","Error",0);
                browseBtn.requestFocus();
        }
        else{
            String fname=fNameF.getText();
            String mname=mNameF.getText();
            String lname=lNameF.getText();
            String genderr=genderGroup.getSelection().getActionCommand();
            Date bD=dobF.getDate();
            String birthDate=obj.returnDate(bD);
            String Nationality=nationalityF.getText();
            String ctznNo=citzenNoF.getText();
            String add=addressF.getText();
            String phone=contactF.getText();
            String eml=emailF.getText();
            String eFD=eff_Frm.getText();
            Date eT=effToF.getDate();
            String eTD=obj.returnDate(eT);
            int money=Integer.parseInt(libDepoF.getText());
            String mTyp=(String)memTypeF.getSelectedItem();
            ArrayList<String> al=new ArrayList<>();
            String sqL;
            if(idValue==0){
                al.clear();
                sqL=" INSERT INTO member_records(`First Name`,`Last Name`,`Gender`,`D.O.B`,`Nationality`,`Address`,`Contact`,`Effective From`,`Effective To`,`Member Type`,`Library Deposit`) VALUES('"+fname+"','"
                        +lname+"','"+genderr+"','"+birthDate+"','"+Nationality+"','"+add+"','"+phone+"',DATE'"+eFD+"',DATE'"+eTD+"','"+mTyp+"',"+money+")";
                al.add(sqL);
                sqL="INSERT INTO members_middle_name(mid,`Middle Name`) VALUES("+temp+",'"+mname+"')";
                if(mNameF.getText().length()!=0)
                    al.add(sqL);
                sqL="INSERT INTO member_citizenship_no VALUES("+temp+","+ctznNo+")";
                if(citzenNoF.getText().length()!=0)
                    al.add(sqL);
                sqL="INSERT INTO member_email(mid,Email) VALUES("+temp+",'"+eml+"')";
                if(emailF.getText().length()!=0)
                    al.add(sqL);
                flag=connect.executeSql(al, "Member Registeration");
                if(selectedFile!=null)
                    connect.prepareStatement("members_photo",temp,selectedFile,0);
                if(flag){
                    JOptionPane.showMessageDialog(null,"Memeber Registration Successful","Register New Member",JOptionPane.INFORMATION_MESSAGE);
                    try {
                        InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\MemberShip.jrxml"));
                        JasperDesign jd=JRXmlLoader.load(in);   
                        JasperReport jr=JasperCompileManager.compileReport(jd);
                        HashMap param=new HashMap();
                        if(idddd!=0)
                            param.put("mid",Integer.parseInt(mIddd.getText()));
                        else
                            param.put("memid",temp);
                        param.put("username",usrId);
                        JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                        JasperViewer.viewReport(j, false);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex); 
                    }
                    dispose();
                    new EditMemberFinal(idd);
                }                
            }else{
                al.clear();
                sqL=" UPDATE member_records SET `First Name`='"+fname+"',`Last Name`='"
                        +lname+"',`Gender`='"+genderr+"',`D.O.B`='"+
                        birthDate+"',`Nationality`='"+Nationality+"',`Address`='"
                        +add+"',`Contact`='"+phone+"',`Effective From`=DATE'"
                        +eFD+"',`Effective To`=DATE'"+eTD+"',`Member Type`='"+
                        mTyp+"',`Library Deposit`="+money+" WHERE mid="+idValue;
                al.add(sqL);
                try{
                    sqL="SELECT * FROM members_middle_name WHERE mid="+idValue;
                    rs=connect.returnResult(sqL);
                    if(rs.next())
                        sqL="UPDATE members_middle_name SET `Middle Name`='"+mname+"' WHERE mid="+idValue;
                    else
                        sqL="INSERT INTO members_middle_name VALUES("+idValue+",'"+mname+"')";
                    al.add(sqL);
                    sqL="SELECT * FROM member_citizenship_no WHERE mid="+idValue;
                    rs=connect.returnResult(sqL);
                    if(rs.next())
                        sqL="UPDATE member_citizenship_no SET `Citizenship No`='"+ctznNo+"' WHERE mid="+idValue;
                    else
                        sqL="INSERT INTO member_citizenship_no VALUES("+idValue+",'"+ctznNo+"')";
                    al.add(sqL);
                    sqL="SELECT * FROM member_email WHERE mid="+idValue;
                    rs=connect.returnResult(sqL);
                    if(rs.next())
                        sqL="UPDATE member_email SET `Email`='"+eml+"' WHERE mid="+idValue;
                    else
                        sqL="INSERT INTO member_email VALUES("+idValue+",'"+eml+"')";
                    al.add(sqL);
                    flag=connect.executeSql(al, "Update Member");
                    sqL="SELECT * FROM members_photo WHERE mid="+idValue;
                    rs=connect.returnResult(sqL);
                    if(!rs.next()){
                        if(selectedFile!=null)
                            connect.prepareStatement("members_photo",temp,selectedFile,0);
                    }
                    else{
                        if(selectedFile!=null)
                            connect.prepareStatement("members_photo",idValue,selectedFile,1);
                    }
                        
                    if(flag){
                        JOptionPane.showMessageDialog(null,"Memeber has been successfully updated","Update Member",JOptionPane.INFORMATION_MESSAGE);
                    }
                }catch(SQLException e){

                }
            }
        }
    }
    public void getDataForEdit(int thatId){
        ArrayList<String> al=new ArrayList<>();
        String sql="SELECT mr.`mid`,`First Name`,mm.`middle name`,`Last Name`, `Gender`, `D.O.B`,"
                + " `Nationality`, `Address`,mc.`citizenship no` ,`Contact`,me.Email,`Effective From`, "
                + "`Effective To`, `Member Type`, `Library Deposit` FROM "
                + "`member_records` mr LEFT JOIN `members_middle_name` mm ON mm.mid=mr.mid"
                + " LEFT JOIN member_citizenship_no mc ON mc.mid=mr.mid LEFT JOIN member_email me ON me.mid=mr.mid WHERE mr.mid="+thatId;
        ResultSet rS=connect.returnResult(sql);
        Vector col=connect.getColumnsHeading(sql, rS);
        try{
            while(rS.next()){
                for(int j=1;j<=col.size();j++)
                    al.add(rS.getString(j));
            }             
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
        mIddd.setText(al.get(0));
        fNameF.setText(al.get(1));
        mNameF.setText(al.get(2));
        lNameF.setText(al.get(3));
        switch (al.get(4)) {
            case "Male":
                maleBtn.setSelected(true);
                break;
            case "Female":
                femaleBtn.setSelected(true);
                break;
            default:
                other.setSelected(true);
                break;
        }
        try {
            Date birthDa=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(5));
            Date efTo=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(11));
            dobF.setDate(birthDa);
            effToF.setDate(efTo);
            
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"Could not set Date!",JOptionPane.ERROR_MESSAGE);
        }
        nationalityF.setText(al.get(6));
        addressF.setText(al.get(7));
        citzenNoF.setText(al.get(8));
        contactF.setText(al.get(9));
        emailF.setText(al.get(10));
        eff_Frm.setText(al.get(11));
        memTypeF.setSelectedItem(al.get(13));
        libDepoF.setText(al.get(14));
               
    }
    public ImageIcon ResizeImage(String ImagePath)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img1 = MyImage.getImage();
        Image newImg = img1.getScaledInstance(this.img.getWidth(),this.img.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    
    public void retriveImage(int thatId){
        String query="SELECT photo FROM members_photo WHERE mid="+thatId;
        rs=connect.returnResult(query);
        int i = 0;
        try{
            if(rs.next()){
                do {
                    InputStream ins = rs.getBinaryStream(1);
                    BufferedImage imgB=ImageIO.read(ins);
                    Image imgdb = imgB.getScaledInstance(this.img.getWidth(), this.img.getHeight(), Image.SCALE_SMOOTH);
                    this.img.setIcon(new ImageIcon(imgdb));
                }while (rs.next());
            }
            else
                img.setText("No photo");
            
        }catch(IOException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error while retriving image "+ex.getMessage());
        }
    }
    public void showMessage(String mess,String what,int type){
        JOptionPane.showMessageDialog(null,mess,what,type);
    }
//    public static void main(String[] args){
//       new MemberRegisterFinal(0,);
//    }
            
}
