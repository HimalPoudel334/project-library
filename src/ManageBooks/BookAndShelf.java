
package ManageBooks;

import UtiltiyClasses.DatabaseConnectivity;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.Vector;

public class BookAndShelf extends JFrame implements ActionListener,KeyListener{
    private JPanel backPnl,formPnl;
    private JTextArea area;
    private JScrollPane sp;
    private JTable table;
    private ResultSet rs;
    private DefaultTableModel model;
    private JComboBox<String> jc;
    private JTextField field;
    private Vector column;
    private JLabel comboLabel,searchBasis;
    private JButton backBtn;
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public BookAndShelf() {
        super("Book And Shelf");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(500,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        backPnl=new JPanel();
        backPnl.setLayout(new FlowLayout(0));
        backBtn=new JButton("Back");
        backBtn.addActionListener(this);
        backPnl.add(backBtn);
        add(backPnl,BorderLayout.NORTH);
        
        formPnl=new JPanel(new FlowLayout());
        String[] ar={"Hello","There","How","Are","You"};
        jc=new JComboBox<>(ar);
        field=new JTextField(20);
        
        formPnl.add(new JLabel("Search:"));
        formPnl.add(jc);
        formPnl.add(new JLabel("Enter shelf id:"));
        formPnl.add(field);
        
        JPanel container=new JPanel(new BorderLayout());
        container.add(formPnl,BorderLayout.NORTH);
        
        String query="SELECT `Book Id`, CONCAT_WS('.',`book_num_int`, `book_num_float`) as `Book Number`,"
                + " r.`Room Id`,r.`Location`,s.`Shelf Id`,s.`Shelf Name`,ra.`Rack Id`"
                + " FROM `book_and_shelf` bs LEFT JOIN(racks ra JOIN (shelves s JOIN room r"
                + " ON s.`Room Id`=r.`Room Id`) ON s.`Shelf Id`=ra.`Shelf Id`) ON bs.`Rack Id`=ra.`Rack Id`";
        rs=connect.returnResult(query);
        column=connect.getColumnsHeading(query, rs);
                        
        table=new JTable();
        model=new DefaultTableModel();
        table.setModel(model);
        sp=new JScrollPane(table);
        
        try{
            sp.repaint();
            model.setColumnIdentifiers(column);
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
        
        container.add(sp,BorderLayout.CENTER);
        
        add(container,BorderLayout.CENTER);
        
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookAndShelf().setVisible(true);
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton temp=(JButton)e.getSource();
        if(temp==backBtn)
            dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    
}
