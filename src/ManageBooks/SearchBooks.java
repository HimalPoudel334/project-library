package ManageBooks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import UtiltiyClasses.DatabaseConnectivity;
import java.util.Vector;

public class SearchBooks  extends JFrame{
    private JPanel homePnl,container,searchPnl,tabelPnl;
    private JLabel book,searchBasis;
    private JButton search,homeBtn;
    private JTextField bookName;
    private JTable table;
    private JScrollPane sp;
    private DefaultTableModel model;
    private ResultSet rs;
    private String sql,SqL,temp="Book Id";
    private Vector<String> cols;
    private JComboBox<String> jc;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    @SuppressWarnings("Convert2Lambda")
    public SearchBooks() {

        super("Search Books");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

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
        book= new JLabel(temp);
        search= new JButton("Search");
        search.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //searchBooks();
            }
        });
        String str[]={"Book Id","Book Name","Books Quantity","Author","ISBN","Available Books","Issued Books","Language","Supplier Name"};
        jc=new JComboBox(str);
        jc.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                temp=String.valueOf(jc.getSelectedItem());
                book.setText("Enter "+temp+" to Search:");
                if(temp.equals("Available Books")||temp.equals("Issued Books")){
                    validateOption();
                    book.setText("Enter Book Id or Book Name: ");
                }
                else if(temp.equals("Books Quantity")){
                    book.setText("Enter Book Name");
                    SqL="SELECT `Book Name`,Author,Add_In_Date,ISBN,Pages,`Year Published`,Price,Edition,Language,COUNT(`Book Id`) AS Quantity FROM book_records GROUP BY `Book Name`";
                }
                else{
                   SqL="SELECT b.`Book Id`, `Book Name`, `Author`,ISBN,sr.`Supplier Name`, `Year Published`,"
                           + " `Price`,`Edition`, `Language`, `Is Available`,"
                           + " CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`,"
                           + " ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` "
                           + "FROM `book_records` b JOIN (book_and_shelf bs JOIN"
                           + "(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`)"
                           + " ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`)"
                           + " ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN"
                           + " supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id`";
                }
                try{
                    rs=connect.returnResult(SqL);
                    cols=connect.getColumnsHeading(SqL,rs);
                    model.setColumnIdentifiers(cols);
                    sp.repaint();
                    model.getDataVector().removeAllElements();
                    while(rs.next()){
                        Vector<String> v=new Vector<>();
                        for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                            v.add(rs.getString(i));
                        model.addRow(v);
                    }
                }catch(SQLException ee){
                        JOptionPane.showMessageDialog(null,ee.getMessage(),"Search Books",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
                
        searchBasis=new JLabel("Search According to: ");
        searchPnl.add(searchBasis);
        searchPnl.add(jc);
        bookName=new JTextField(30);
        bookName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                onBookFieldPressed();
           }      
        });
        searchPnl.add(book);
        searchPnl.add(bookName);
        searchPnl.add(search);
        container.add(searchPnl,BorderLayout.NORTH);
        tabelPnl=new JPanel();
        tabelPnl.setLayout(new BorderLayout());
        container.add(tabelPnl,BorderLayout.CENTER);
        add(container,BorderLayout.CENTER);
    
        String what="Search Books";
        table=new JTable();
        model =new DefaultTableModel();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);//automatically sorts row
        table.setCellSelectionEnabled(true);//enables selecting cells
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);//lets see
        sp=new JScrollPane(table);
        try{
            String sQl="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`,`Year Published`,"
                    + " `Price`,`Edition`, `Language`, `Is Available`, "
                    + "CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`,"
                    + " ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` "
                    + "FROM `book_records` b JOIN (book_and_shelf bs JOIN"
                    + "(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`)"
                    + " ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`)"
                    + " ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN "
                    + "supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id`";
            rs=connect.returnResult(sQl);
            cols=connect.getColumnsHeading(sql,rs);
            model.setColumnIdentifiers(cols);
            while(rs.next()){
                Vector<String> v=new Vector<>();
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    v.add(rs.getString(i));
                model.addRow(v);
            }
        }catch(SQLException e){

        }
        tabelPnl.add(sp,BorderLayout.CENTER);
    }
    public void onBookFieldPressed(){
        if(temp.equals("Available Books")||temp.equals("Issued Books")){
            validateOption();
            if(bookName.getText().length()!=0){
                rs=connect.returnResult(sql);
                cols=connect.getColumnsHeading(sql,rs);
                model.setColumnIdentifiers(cols);
            }
            else{
                rs=connect.returnResult(SqL);
                cols=connect.getColumnsHeading(SqL,rs);
                model.setColumnIdentifiers(cols);
            }
        }
        else if(temp.equals("Books Quantity")){
            sql="SELECT `Book Name`,Author,Add_In_Date,ISBN,Pages,`Year Published`,Price,Edition,Language,COUNT(`Book Id`) AS Quantity FROM book_records WHERE `Book Name` Like'"+bookName.getText()+"%' GROUP BY `Book Name`";
            rs=connect.returnResult(sql);
            cols=connect.getColumnsHeading(sql,rs);
            model.setColumnIdentifiers(cols);
        }
        else{
            if(bookName.getText().length()!=0){
                if(temp.equals("Book Id"))
                    sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE b.`Book Id` = '"+bookName.getText()+"'";
                else if(temp.equals("Supplier Name"))
                    sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE sr.`"+temp+"` LIKE '"+bookName.getText()+"%'";
                else
                    sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE b.`"+temp+"` LIKE '"+bookName.getText()+"%'";
            }
            else{
                sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`,`Year Published`,"
                        + " `Price`,`Edition`, `Language`, `Is Available`,"
                        + " CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`,"
                        + " ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location`"
                        + " FROM `book_records` b JOIN (book_and_shelf bs JOIN"
                        + "(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`)"
                        + " ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`)"
                        + " ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id`";
            }
            rs=connect.returnResult(sql);
            cols=connect.getColumnsHeading(sql,rs);
            model.setColumnIdentifiers(cols);
        }
        try{
            sp.repaint();
            model.getDataVector().removeAllElements();
            while(rs.next()){
                Vector<String> v=new Vector<>();
                for(int i=1;i<=rs.getMetaData().getColumnCount();i++)
                    v.add(rs.getString(i));
                model.addRow(v);
            }
        }catch(SQLException ee){
            JOptionPane.showMessageDialog(null,ee.getMessage(),"Search Books",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void validateOption(){
        if(temp.equals("Available Books")){
            SqL="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE b.`Is Available`=1";
            sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id`  WHERE b.`Book Id` ='"+bookName.getText()+"'||b.`Book Name` LIKE'"+bookName.getText()+"%'AND `Is Available`=1";
        }
        else{ 
            SqL="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE `Is Available`=0";
            sql="SELECT b.`Book Id`, `Book Name`, `Author`, `ISBN`,sr.`Supplier Name`, `Year Published`, `Price`,`Edition`, `Language`, `Is Available`, CONCAT_WS('.',bs.`book_num_int`, bs.`book_num_float`) as `Book Number`, ra.`Rack Id`,sh.`Shelf Id`,sh.`Shelf Name`,ro.`Room Id`,ro.`Location` FROM `book_records` b JOIN (book_and_shelf bs JOIN(racks ra JOIN (shelves sh JOIN room ro ON sh.`Room Id`=ro.`Room Id`) ON sh.`Shelf Id`=ra.`Shelf Id`) ON bs.`Shelf Id`=ra.`Shelf Id`) ON b.`Book Id`=bs.`Book Id` INNER JOIN(book_supplier_info bsi INNER JOIN supplier_records sr ON bsi.sid=`sr`.`Supplier Id`) ON bsi.bid=`b`.`Book Id` WHERE `b.Book Id`= '"+bookName.getText()+"'||b.`Book Name` LIKE'"+bookName.getText()+"%'AND `Is Available`=0";
        }
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SearchBooks();
            }
        });
    }
}
