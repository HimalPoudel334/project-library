package ManageUsers;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXDatePicker;
import projectlibrary.DashBoard;
import UtiltiyClasses.DatabaseConnectivity;
import UtiltiyClasses.TatkalKoLagi;
import UtiltiyClasses.Validater;
import java.io.IOException;
import java.text.ParseException;

public class AddNewUser extends JFrame {
    private final JPanel backPnl,infoPnl,container,formPnl,genderPnl,addressPnl,addressPnl1,imgPnl;
    private final JButton backBtn,addBtn,browseBtn,updateBtn;
    private Border innerBorder;
    private final JLabel infoLabel,idLbl,aRecentPP,imgLbl,uId,fName,mName,lName,gender,username,dob,tempAdd,perAdd,password,dateOfJoin,contact,email,isAdmin,muncipal,wardNo,tWardNo,tCity,city,nationality,tMuncipal,citizenshipNo;
    private final JTextField fNameF,mNameF,lNameF,usernameF,tCityF,tMuncipalF,tWardNoF,cityF,nationalityF,citizenF,muncipalF,wardNoF,emailF,contactF;
    private final JPasswordField passwordF;
    private final JRadioButton male,female,others;
    private final JXDatePicker dobF,dateOfJoinF;
    private ResultSet rs;
    private final ButtonGroup genderGroup;
    private final JCheckBox isAdminF;
    private boolean itemflag=false,flag;
    private String path;
    private File selectedFile;
    private FileInputStream fIS;
    DatabaseConnectivity connect= new DatabaseConnectivity();
    public AddNewUser(int id){
        
        super("User Registration");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(850,715);
        setLocationRelativeTo(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        backPnl=new JPanel(new FlowLayout(0));
        backBtn = new JButton("Back");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        backPnl.add(backBtn);
        add(backPnl, BorderLayout.NORTH);
        
        infoPnl=new JPanel();
        infoLabel=new JLabel("User Registration");
        infoLabel.setFont(new Font("Tahoma",1,16));
        infoPnl.setBackground(Color.RED);
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
        
        uId=new JLabel("User Id:");
        idLbl=new JLabel();
        fName=new JLabel("First Name:");
        mName=new JLabel("Middle Name:");
        lName=new JLabel("Last Name:");
        dob=new JLabel("Date Of Birth:");
        gender=new JLabel("Gender:");
        nationality=new JLabel("Nationality:");
        citizenshipNo=new JLabel("Citizenship No:");
        perAdd=new JLabel("Permanent Address:");
        tempAdd=new JLabel("Temporary Address(If any):");
        city=new JLabel("City:");
        tCity=new JLabel("City:");
        muncipal=new JLabel("Muncipality:");
        tMuncipal=new JLabel("Muncipality:");
        wardNo=new JLabel("Ward No:");
        tWardNo=new JLabel("Ward No:");
        contact=new JLabel("Phone No:");
        email=new JLabel("Email:");
        isAdmin=new JLabel("Is Admin:");
        dateOfJoin=new JLabel("Date Joined:");
        username=new JLabel("Username:");
        password=new JLabel("Password:");
        
        fNameF=new JTextField(20);
        mNameF=new JTextField(20);
        lNameF=new JTextField(20);
        nationalityF=new JTextField(20);
        citizenF=new JTextField(20);
        cityF=new JTextField(10);
        tCityF=new JTextField(10);
        muncipalF=new JTextField(10);
        tMuncipalF=new JTextField(10);
        wardNoF=new JTextField(5);
        tWardNoF=new JTextField(5);
        contactF=new JTextField(20);
        emailF=new JTextField(20);
        usernameF=new JTextField(20);
        passwordF=new JPasswordField(20);
        
        addressPnl=new JPanel(new FlowLayout());
        addressPnl.setBackground(Color.PINK);
        addressPnl.add(city);
        addressPnl.add(cityF);
        addressPnl.add(muncipal);
        addressPnl.add(muncipalF);
        addressPnl.add(wardNo);
        addressPnl.add(wardNoF);
        
        //why is this even required
        addressPnl1=new JPanel(new FlowLayout());
        addressPnl1.setBackground(Color.PINK);
        addressPnl1.add(tCity);
        addressPnl1.add(tCityF);
        addressPnl1.add(tMuncipal);
        addressPnl1.add(tMuncipalF);
        addressPnl1.add(tWardNo);
        addressPnl1.add(tWardNoF);
        
        dobF=new JXDatePicker();
        dobF.setDate(Calendar.getInstance().getTime());
        dobF.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        
        dateOfJoinF=new JXDatePicker();
        dateOfJoinF.setDate(Calendar.getInstance().getTime());
        dateOfJoinF.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
        
        isAdminF=new JCheckBox();
        isAdminF.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie){
                itemflag=true;
            }
        });
        addBtn=new JButton("Add User");
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                addBtnClicked(id);
            }
        });
        updateBtn=new JButton("Update");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBtnClicked(id);
            }
        });
        
        male=new JRadioButton("Male",true);
        male.setActionCommand("Male");
        female=new JRadioButton("Female");
        female.setActionCommand("Female");
        others=new JRadioButton("Others");
        others.setActionCommand("Other");
        genderGroup=new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderGroup.add(others);
        
        genderPnl=new JPanel();
        genderPnl.setBackground(Color.PINK);
        genderPnl.setLayout(new FlowLayout(0));
        genderPnl.add(male);
        genderPnl.add(female);
        genderPnl.add(others);
        
        formPnl.add(uId,c);
        c.gridy++;
        formPnl.add(fName,c);
        c.gridy++;
        formPnl.add(mName,c);
        c.gridy++;
        formPnl.add(lName,c);
        c.gridy++;
        formPnl.add(dob,c);
        c.gridy++;
        formPnl.add(gender,c);
        c.gridy++;        
        formPnl.add(nationality,c);
        c.gridy++;
        formPnl.add(citizenshipNo,c);
        c.gridy++;
        formPnl.add(perAdd,c);
        c.gridy++;
        formPnl.add(tempAdd,c);
        c.gridy++;
        formPnl.add(contact,c);
        c.gridy++;
        formPnl.add(email,c);
        c.gridy++;
        formPnl.add(dateOfJoin,c);
        c.gridy++;
        formPnl.add(isAdmin,c);
        c.gridy++;
        formPnl.add(username,c);
        c.gridy++;
        formPnl.add(password,c);
        
        c.gridx++;
        c.gridy=0;
        c.anchor=GridBagConstraints.LINE_START;
        
        String idval="SELECT MAX(uid) FROM user_login_details";
        rs=connect.returnResult(idval);
        try{
            while(rs.next())
                idLbl.setText(""+(rs.getInt(1)+1));
        }catch(SQLException e){
            System.out.println("Error in uid "+e.getMessage());
        }
        formPnl.add(idLbl,c);
        c.gridy++;
        formPnl.add(fNameF,c);
        c.gridy++;
        formPnl.add(mNameF,c);
        c.gridy++;
        formPnl.add(lNameF,c);
        c.gridy++;
        formPnl.add(dobF,c);
        c.gridy++;
        formPnl.add(genderPnl,c);
        c.gridy++;
        formPnl.add(nationalityF,c);
        c.gridy++;
        formPnl.add(citizenF,c);
        c.gridy++;
        formPnl.add(addressPnl,c);
        c.gridy++;
        formPnl.add(addressPnl1,c);
        c.gridy++;
        formPnl.add(contactF,c);
        c.gridy++;
        formPnl.add(emailF,c);
        c.gridy++;
        formPnl.add(dateOfJoinF,c);
        c.gridy++;
        formPnl.add(isAdminF,c);
        c.gridy++;
        formPnl.add(usernameF,c);
        c.gridy++;
        formPnl.add(passwordF,c);
        c.gridy++;
        if(id>0){
            getDataForEdit(id);
            formPnl.add(updateBtn,c);
        }
        else
            formPnl.add(addBtn,c);
        
        imgPnl=new JPanel();
        imgPnl.setLayout(null);
        imgPnl.setSize(100,85);
        imgPnl.setBackground(Color.CYAN);
        aRecentPP=new JLabel("A Recent PP Size Photo");
        aRecentPP.setBounds(5,5,160,30);
        imgPnl.add(aRecentPP);

        imgLbl=new JLabel();
        imgLbl.setBounds(5,40,150,150);
        Border brd=BorderFactory.createLineBorder(Color.BLACK,1);
        imgLbl.setBorder(brd);
        if(id>0){
            retriveImage(id);
        }
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
                    path = selectedFile.getAbsolutePath();
                    imgLbl.setIcon(ResizeImage(path));
                }
                //if the user click on save in Jfilechooser
                else if(result == JFileChooser.CANCEL_OPTION){
                    System.out.println("No File Select");
                }
            }
        });

        imgPnl.add(imgLbl);
        imgPnl.add(browseBtn);
        
        container=new JPanel(new BorderLayout());
        container.add(infoPnl,BorderLayout.NORTH);
        container.add(formPnl,BorderLayout.WEST);
        container.add(imgPnl,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
        
        setVisible(true);
    }
    public void addBtnClicked(int idValue){
        TatkalKoLagi tat=new TatkalKoLagi();
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
        else if(!(Validater.compare(citizenF.getText(),"N"))){
            showMessage("Citizenship number must be numeric","Error",0);
            citizenF.requestFocus();
        }
        else if(!(Validater.compare(cityF.getText(),"Aa"))){
            showMessage("Enter city name properly","Error",0);
            cityF.requestFocus();
        }
        else if(!(Validater.compare(muncipalF.getText(),"Aa"))){
            showMessage("Enter Muncipal name properly","Error",0);
            muncipalF.requestFocus();
        }
        else if(!(Validater.compare(wardNoF.getText(),"N"))){
            showMessage("Ward number must be numeric","Error",0);
            wardNoF.requestFocus();
        }
        else if((tCityF.getText().length()>0)&&!(Validater.compare(tCityF.getText(),"Aa"))){
            showMessage("Enter city name properly","Error",0);
            tCityF.requestFocus();
        }
        else if((tMuncipalF.getText().length()>0)&&!(Validater.compare(tMuncipalF.getText(),"Aa"))){
            showMessage("Enter Muncipal name properly","Error",0);
            tMuncipalF.requestFocus();
        }
        else if((tWardNoF.getText().length()>0)&&!(Validater.compare(tWardNoF.getText(),"N"))){
            showMessage("Ward number must be numeric","Error",0);
            tWardNoF.requestFocus();
        }        
        else if(!(Validater.compare(contactF.getText(),"N"))){
            showMessage("Contact number must be numeric","Error",0);
            contactF.requestFocus();
        }
        else if((emailF.getText().length()>0)&&!(Validater.compare(emailF.getText(),"ee"))){
            showMessage("Invalid email address","Error",0);
            emailF.requestFocus();
        }
        else if(!(Validater.compare(usernameF.getText(),"U"))){
            showMessage("Username error","Error",0);
            usernameF.requestFocus();            
        }
        else if(!(Validater.compare(String.valueOf(passwordF.getPassword()),"Pas"))){
            showMessage("Password must have at least one lowercase letter, one digit\none special character, one capital letter\n and must be 6-16 characters long","Error",0);
            passwordF.requestFocus();
        }
        else if((idValue==0)&&(selectedFile==null)){
            showMessage("Please select a photo","Error",0);
            browseBtn.requestFocus();
        }
        else{
            String fname=fNameF.getText();
            String mname=mNameF.getText();
            String lname=lNameF.getText();
            String gendeR=genderGroup.getSelection().getActionCommand();
            Date dt=dobF.getDate();
            String dat=tat.returnDate(dt);
            String national=nationalityF.getText();
            String citznNo=citizenF.getText();
            String ct=cityF.getText();
            String mun=muncipalF.getText();
            int wd=Integer.parseInt(wardNoF.getText());
            String tCt=tCityF.getText();
            String tMunc=tMuncipalF.getText();
            int tWard=0;
            if(tWardNoF.getText().length()>0)
                tWard=Integer.parseInt(tWardNoF.getText());
            String cont=contactF.getText();
            String eml=emailF.getText();
            Date dtOfJoin=dateOfJoinF.getDate();
            String dtoj=tat.returnDate(dtOfJoin);
            String usrnam=usernameF.getText();
            String pas=String.valueOf(passwordF.getPassword());
            String what="Register New User";
            ArrayList<String> al=new ArrayList<>();
            String sql;
            if(idValue>0){
                sql="UPDATE user_login_details SET first_name='"+fname+"',last_name='"+lname+"',`Date Of Birth`=DATE'"+dat+"',Gender='"+
                        gendeR+"',Nationality='"+national+"',`Citizenship No`='"+citznNo+"',PCity='"+
                        ct+"',PMuncipal='"+mun+"',PWardNo="+wd+",Contact='"+cont+"',`Date Joined`=DATE'"+
                        dtoj+"',username='"+usrnam+"',password='"+pas+"',isadmin="+itemflag+" WHERE uid="+idValue;
                al.add(sql);
                try{
                    sql="SELECT * FROM user_temp_address WHERE uid="+idValue;
                    rs=connect.returnResult(sql);
                    if(rs.next())
                        sql="UPDATE user_temp_address SET city='"+tCt+"',Muncipal='"+tMunc+"',`Ward No`='"+tWard+"' WHERE uid="+idValue;
                    else
                        sql="INSERT INTO user_temp_address VALUES("+idValue+",'"+tCt+"','"+tMunc+"',"+tWard+")";
                    if(tCityF.getText().length()!=0&&tMuncipalF.getText().length()!=0&&tWardNoF.getText().length()!=0)
                        al.add(sql);
                    sql="SELECT * FROM user_middle_name WHERE uid="+idValue;
                    rs=connect.returnResult(sql);
                    if(rs.next())
                        sql="UPDATE user_middle_name SET `Middle Name`='"+mname+"' WHERE uid="+idValue;
                    else
                        sql="INSERT INTO user_middle_name VALUES('"+idValue+"','"+mname+"')";
                    if(mNameF.getText().length()!=0)
                        al.add(sql);
                    sql="SELECT * FROM user_email WHERE uid="+idValue;
                    rs=connect.returnResult(sql);
                    if(rs.next())
                        sql="UPDATE user_email SET Email='"+eml+"' WHERE uid="+idValue;
                    else
                        sql="INSERT INTO user_email VALUES('"+idValue+"','"+eml+"')";
                    if(emailF.getText().length()!=0)
                        al.add(sql);
                    sql="SELECT * FROM user_photos WHERE uid="+idValue;
                    rs=connect.returnResult(sql);
                    if(!rs.next()){
                        if(selectedFile!=null)
                            connect.prepareStatement("user_photos",idValue,selectedFile,0);
                    }
                    else{
                        if(selectedFile!=null)
                            connect.prepareStatement("user_photos",idValue,selectedFile,1);
                    }
                }catch(SQLException ee){
                    JOptionPane.showMessageDialog(null,ee.getMessage());
                }
                flag=connect.executeSql(al,"Update User");
                if(flag){
                    JOptionPane.showMessageDialog(null,"Member successfully updated","Update Member",JOptionPane.INFORMATION_MESSAGE);
                }

            }else{
                al.clear();
                sql=" INSERT INTO user_login_details(first_name,last_name,`Date Of Birth`,Gender,Nationality,`Citizenship No`,PCity,PMuncipal,PWardNo,Contact,`Date Joined`,username,password,isadmin)VALUES('"+
                        fname+"','"+lname+"',DATE'"+dat+"','"+gendeR+"','"+
                        national+"','"+citznNo+"','"+ct+"','"+mun+"',"+wd+",'"+cont+"',DATE'"+dtoj+"','"+usrnam+"','"+pas+"',"+flag+")";
                al.add(sql);
                sql="INSERT INTO user_middle_name VALUES('"+idLbl.getText()+"','"+mname+"')";
                if(mNameF.getText().length()!=0)
                    al.add(sql);
                sql="INSERT INTO user_email VALUES('"+idLbl.getText()+"','"+eml+"')";
                if(emailF.getText().length()!=0)
                    al.add(sql);
                sql="INSERT INTO user_temp_address VALUES('"+idLbl.getText()+"','"+tCt+"','"+tMunc+"','"+tWard+"')";
                if(tCityF.getText().length()!=0&&tMuncipalF.getText().length()!=0&&tWardNoF.getText().length()!=0)
                    al.add(sql);
                boolean flag=connect.executeSql(al,what);
                if(selectedFile!=null)
                    connect.prepareStatement("user_photos",Integer.parseInt(idLbl.getText()), selectedFile,0);
                if(flag){
                    JOptionPane.showMessageDialog(null,"User successfully added","Add New User",JOptionPane.INFORMATION_MESSAGE);
                    new AddNewUser(0);
                    dispose();
                }
                if(idValue==-1){
                    dispose();
                    if(flag)
                        new DashBoard(1,usrnam);
                    else
                        new DashBoard(0,usrnam);
                    JOptionPane.showMessageDialog(null,"Welcome to Library Management System","New User Registration",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    public ImageIcon ResizeImage(String ImagePath)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(imgLbl.getWidth(),imgLbl.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
    public void getDataForEdit(int thatId){
        ArrayList<String> al=new ArrayList();
        String sq="SELECT u.`uid`, `first_name`, mu.`Middle Name`,`last_name`, `Date Of Birth`,"
                + " `Gender`, `Nationality`, `Citizenship No`, `PCity`, `PMuncipal`, "
                + "`PWardNo`,ut.City,ut.Muncipal,ut.`Ward No`, `Contact`,ue.Email, "
                + "`Date Joined`, `username`, `password`, "
                + "`isadmin` FROM `user_login_details` u LEFT JOIN user_middle_name mu ON mu.uid=u.uid"
                + " LEFT JOIN user_temp_address ut ON ut.uid=u.uid LEFT JOIN user_email ue ON ue.uid=u.uid Where u.uid="+thatId;
        rs=connect.returnResult(sq);
        
        try{
            while(rs.next()){
                for(int j=1;j<=rs.getMetaData().getColumnCount();j++){
                    al.add(rs.getString(j));
                }
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.getMessage(),"Update User",JOptionPane.ERROR_MESSAGE);
        }
        idLbl.setText(""+thatId);
        fNameF.setText(al.get(1));
        mNameF.setText(al.get(2));
        lNameF.setText(al.get(3));
        switch (al.get(5)) {
            case "Male":
                male.setSelected(true);
                break;
            case "Female":
                female.setSelected(true);
                break;
            default:
                others.setSelected(true);
                break;
        }
        nationalityF.setText(al.get(6));
        citizenF.setText(al.get(7));
        cityF.setText(al.get(8));
        muncipalF.setText(al.get(9));
        wardNoF.setText(al.get(10));
        tCityF.setText(al.get(11));
        tMuncipalF.setText(al.get(12));
        tWardNoF.setText(al.get(13));
        contactF.setText(al.get(14));
        emailF.setText(al.get(15));
        
        Date birthDa=null,datJoin=null;
        try {
            birthDa=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(4));
            datJoin=new SimpleDateFormat("yyyy-MM-dd").parse(al.get(16));
        } catch (ParseException ex) {
        }
        dobF.setDate(birthDa);
        dateOfJoinF.setDate(datJoin);
        
        if(al.get(19).equals("1"))
            isAdminF.setSelected(true);
        usernameF.setText(al.get(17));
        passwordF.setText(al.get(18));
        
    }
    public void retriveImage(int thatId){
        String query="SELECT photo FROM user_photos WHERE uid="+thatId;
        rs=connect.returnResult(query);
        int i = 0;
        try{
            if(rs.next()){
                do {
                    InputStream ins = rs.getBinaryStream(1);
                    BufferedImage img=ImageIO.read(ins);
                    Image imgdb = img.getScaledInstance(imgLbl.getWidth(), imgLbl.getHeight(), Image.SCALE_SMOOTH);
                    imgLbl.setIcon(new ImageIcon(imgdb));
                }while (rs.next());
            }
            else
                imgLbl.setText("No photo");
            
        }catch(IOException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error while retriving image "+ex.getMessage());
        }
    }
    public void showMessage(String mess,String what,int type){
        JOptionPane.showMessageDialog(null,mess,what,type);
    }
       

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new AddNewUser(0);
//            }
//        });
//    }

}
