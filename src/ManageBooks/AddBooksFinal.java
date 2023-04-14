
package ManageBooks;

import ManageSuppliers.AddNewSupplier;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;
import org.jdesktop.swingx.JXDatePicker;
import UtiltiyClasses.DatabaseConnectivity;
import UtiltiyClasses.TatkalKoLagi;
import UtiltiyClasses.Validater;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddBooksFinal extends JFrame{
    private JPanel homePnl,infoPan,contentPan,formPan;
    private JButton homeBtn,addBtn,plusBtn;
    private Border formInnerBorder,formOuterBorder;
    private JLabel infoLabel,bIdLab,bid,bname,author,addInDate,ISBN,pages,price,language,publisher,yrPublished,edition,quantity;
    private JTextField bnameF,authorF,ISBNF,pagesF,languageF,priceF,publisherF,editionF,quantityF;
    private JXDatePicker add_in_date;
    private JComboBox<String> yrPublishedF,genre,supplierF;
    String what,cmbStr,numStr,shlfId;
    int temp,supId,tempStr,money=0;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    TatkalKoLagi obj=new TatkalKoLagi();
    public AddBooksFinal(int id){
        super("Add Books");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(400,640);
        setVisible(true);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
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
        
        infoPan=new JPanel();
        infoPan.setLayout(new FlowLayout());
        infoPan.setBackground(Color.CYAN);
        infoLabel=new JLabel("Please Fill the Book Details");
        infoPan.add(infoLabel);
        
        formPan=new JPanel();
        formPan.setBackground(Color.PINK);
        formPan.setLayout(new GridBagLayout());
        
        formInnerBorder=BorderFactory.createTitledBorder("Book Information");
        formOuterBorder=BorderFactory.createEmptyBorder(5, 5, 5, 5);
        formPan.setBorder(BorderFactory.createCompoundBorder(formOuterBorder, formInnerBorder));
        
        String genr[]={"","100-Fiction","200-Computer Science","300-Pure Science","400-Philosophy and Psycology","500-Politics and Socail Science","600-Mythology","700-General Knowledge","800-Science and Technology"};   
        GridBagConstraints c=new GridBagConstraints();
        c.insets=new Insets(3,5,2,5);
        c.anchor=GridBagConstraints.LINE_END;
        c.gridx=c.gridy=0;
                        
        bid=new JLabel("Book Id:");
        bname = new JLabel("Book Name:");
        author = new JLabel("Author:");
        addInDate= new JLabel("Add In Date:");
        pages= new JLabel("Pages:");
        ISBN= new JLabel("ISBN:");
        price=new JLabel("Price:");
        language= new JLabel("Language:");
        yrPublished= new JLabel("Year Published:");
        edition= new JLabel("Edition:");
        quantity=new JLabel("Quantity:");
        //datepicker
        add_in_date = new JXDatePicker();
        add_in_date.setDate(Calendar.getInstance().getTime());
        add_in_date.setFormats(new SimpleDateFormat("yyyy.MM.dd"));
                
        String query="SELECT MAX(`Book Id`) FROM book_records";
        ResultSet rs=connect.returnResult(query);
        try{
            while(rs.next())
                temp=rs.getInt(1)+1;
        }catch(SQLException ex){
            System.out.println("Error in Bid label ="+ex);
        }
        bIdLab=new JLabel(""+temp);
        bnameF = new JTextField(20);
        authorF = new JTextField(20);
        ISBNF=new JTextField(20);
        pagesF=new JTextField(10);
        priceF=new JTextField(10);
        languageF=new JTextField(20);
        plusBtn=new JButton("+");
        plusBtn.setToolTipText("Add New Supplier");
        supplierF=new JComboBox<>();
        getSuppliers();
        supplierF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String supp=String.valueOf(supplierF.getSelectedItem());
                supId=Integer.parseInt(supp.substring(0,1));
                
            }
        });
        String yrs[]=new String[40];
        for(int i=0;i<40;i++)
            yrs[i]=String.valueOf(1990+i);
        yrPublishedF= new JComboBox<>(yrs);
        genre=new JComboBox<>(genr);
        genre.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String gen=(String)genre.getSelectedItem();
                cmbStr=getGenre(gen);
            }
        });
        yrPublishedF.setSelectedItem("2005");
        editionF = new JTextField(10);
        quantityF=new JTextField(10);
        
        formPan.add(bid,c);
        c.gridy++;
        formPan.add(bname,c);
        c.gridy++;
        formPan.add(author,c);
        c.gridy++;
        formPan.add(new JLabel("Genre:"),c);
        c.gridy++;
        formPan.add(ISBN,c);
        c.gridy++;
        formPan.add(price,c);
        c.gridy++;
        formPan.add(yrPublished,c);
        c.gridy++;
        formPan.add(edition,c);
        c.gridy++;
        formPan.add(pages,c);
        c.gridy++;
        formPan.add(new JLabel("Supplier:"),c);
        c.gridy++;
        formPan.add(addInDate,c);
        c.gridy++;
        formPan.add(language,c);
        c.gridy++;
        formPan.add(quantity,c);
        
        c.gridy=0;
        c.gridx++;
        c.anchor=GridBagConstraints.LINE_START;
        formPan.add(bIdLab,c);
        c.gridy++;
        formPan.add(bnameF,c);
        c.gridy++;
        formPan.add(authorF,c);
        c.gridy++;
        formPan.add(genre,c);
        c.gridy++;
        formPan.add(ISBNF,c);
        c.gridy++;
        formPan.add(priceF,c);
        c.gridy++;
        formPan.add(yrPublishedF,c);
        c.gridy++;
        formPan.add(editionF,c);
        c.gridy++;
        formPan.add(pagesF,c);
        c.gridy++;
        formPan.add(supplierF,c);
        if(id==1){
            c.insets=new Insets(5,160,5,5);
            formPan.add(plusBtn,c);
        }
        c.insets=new Insets(5,5,5,5);
        c.gridy++;
        formPan.add(add_in_date,c);
        c.gridy++;
        formPan.add(languageF,c);
        c.gridy++;
        formPan.add(quantityF,c);
        c.gridy++;
        
        addBtn=new JButton("Add Book");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBooks();
            }
        });
        addBtn.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    addBooks();    
                }           
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        plusBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewSupplier(0);
            }
        });
        formPan.add(addBtn,c);
        
        contentPan=new JPanel();
        contentPan.setLayout(new BorderLayout());
        contentPan.add(infoPan,BorderLayout.NORTH);
        contentPan.add(formPan,BorderLayout.CENTER);
        add(contentPan,BorderLayout.CENTER);
        
    }
    public void addBooks(){
        what="Add new book";
        
        if(!Validater.compare(bnameF.getText(),"Aas")){
            showMessage("Enter book name properly","Error",0);
            bnameF.requestFocus();
        }
        else if(!(Validater.compare(authorF.getText(),"Aas"))){
            showMessage("Author name should be non numeric and start with captial letter","Error",0);
            authorF.requestFocus();
        }
        else if(genre.getSelectedItem()==""){
            showMessage("Please select a genre!","Error",0);
            genre.requestFocus();
        }
        else if(!(Validater.compare(ISBNF.getText(),"N"))){
            showMessage("Invalid ISBN!","Error",0);
            ISBNF.requestFocus();
        }
        else if(!(Validater.compare(priceF.getText(),"N"))){
            showMessage("Book price is not valid","Error",0);
            priceF.requestFocus();
        }
        else if(!(Validater.compare(editionF.getText(),"N"))){
            showMessage("Please enter book edition","Error",0);
            editionF.requestFocus();
        }
        else if(!(Validater.compare(pagesF.getText(),"N"))){
            showMessage("Invalid page number!","Error",0);
            pagesF.requestFocus();
        }
        else if(supplierF.getSelectedItem()==""){
            showMessage("Please select a supplier!","Error",0);
            supplierF.requestFocus();
        }
        else if(!(Validater.compare(languageF.getText(),"Aa"))){
            showMessage("Language field cannot be numberic","Error",0);
            languageF.requestFocus();
        }
        else if(!(Validater.compare(quantityF.getText(),"N"))){
            showMessage("Book quantity must be numeric!","Error",0);
            quantityF.requestFocus();
        }
        else{         
            String bok=bnameF.getText();
            String athr=authorF.getText();
            Date dtoi=add_in_date.getDate();
            String finalDate=obj.returnDate(dtoi);
            int pri=Integer.parseInt(priceF.getText());
            int pag=Integer.parseInt(pagesF.getText());
            int edi=Integer.parseInt(editionF.getText());
            String lan=languageF.getText();
            String pubYear=(String)yrPublishedF.getSelectedItem();
            String isbn=ISBNF.getText();
            int quant=Integer.parseInt(quantityF.getText());
            String sql,sql1;
            ArrayList<String> al=new ArrayList<>();
            sql=" INSERT INTO book_records(`Book Name`,`Author`,`Add_In_Date`,ISBN,Pages,`Year Published`,Price,Edition,Language)VALUES('"+bok+"','"+athr+"',Date'"+finalDate+"','"+isbn+"',"+pag+",'"+pubYear+"',"+pri+","+edi+",'"+lan+"')";

            sql1="SELECT MAX(book_num_float) FROM book_and_shelf WHERE `Shelf Id`="+shlfId;
            ResultSet res=connect.returnResult(sql1);
            try{
                while(res.next())
                   tempStr=res.getInt(1)+1;                        
            }catch(Exception e){}
            for(int i=0;i<quant;i++){
                money+=pri;
                al.add(sql);
                sql1="INSERT INTO book_and_shelf VALUES("+(temp+i)+","+cmbStr+","+(tempStr+i)+","+shlfId+","+1+")";
                al.add(sql1);
                sql1="INSERT INTO book_supplier_info(sid,bid) VALUES("+supId+","+(temp+i)+")";
                al.add(sql1);
           }
            sql1="INSERT INTO paid_suppliers VALUES("+supId+","+money+")";
            al.add(sql1);

            if(connect.executeSql(al,"Add Books")){
                JOptionPane.showMessageDialog(null,"Book successfully added","Add Book",JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AddBooksFinal(1);
                new SearchBooks();
            }
        }
    }
    public String getGenre(String selItem){
        String genre;
        genre=selItem.substring(0,3);
        shlfId=selItem.substring(0,1);
        return genre;
    }
    public void showMessage(String mess,String what,int type){
        JOptionPane.showMessageDialog(null,mess,what,type);
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new AddBooksFinal(1);
            }
        });
    }

    public void getSuppliers(){
        supplierF.addItem("");
        String que="SELECT CONCAT_WS('-',`Supplier Id`,`Supplier Name`) FROM supplier_records";
        ResultSet rs=connect.returnResult(que);
        try {
            while(rs.next())
                supplierF.addItem(rs.getString(1));
        } catch (SQLException ex) {
        }
    }
}
