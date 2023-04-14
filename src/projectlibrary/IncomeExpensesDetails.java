
package projectlibrary;

import UtiltiyClasses.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.JXDatePicker;

public class IncomeExpensesDetails extends JFrame implements ActionListener,KeyListener {
    private Date frmDate,toDate;
    private JXDatePicker to,from;
    private JButton btn;
    private JTextField toCustomer;
    TatkalKoLagi tat=new TatkalKoLagi();
    DatabaseConnectivity connect=new DatabaseConnectivity();
    public IncomeExpensesDetails() {
        super("Income Expenses Details");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setVisible(true);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        btn= new JButton("Print Details");
        JLabel lbl=new JLabel("Print details from:");
        JLabel lbl2=new JLabel("To:");
        toCustomer=new JTextField(10);
        toCustomer.setText(""+1000.00);
        
        from=new JXDatePicker();
        from.setDate(Calendar.getInstance().getTime());
        from.setFormats(new SimpleDateFormat("yyyy.MM.dd"));
        
        to=new JXDatePicker();
        to.setDate(Calendar.getInstance().getTime());
        to.setFormats(new SimpleDateFormat("yyyy.MM.dd"));
        
        btn.addActionListener(this);
        add(lbl);
        add(from);
        add(lbl2);
        add(to);
        add(new JLabel("Paid to Suppliers:"));
        add(toCustomer);
        add(btn);     

    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                new IncomeExpensesDetails();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton tempBtn=(JButton)e.getSource();
        if(tempBtn==btn){
            frmDate=from.getDate();
            toDate=to.getDate();
            String fD=tat.returnDate(frmDate);
            String tD=tat.returnDate(toDate);
            double paisa=Double.parseDouble(toCustomer.getText());
            try {
                InputStream in = new FileInputStream(new File("C:\\Users\\DELL-PC\\Documents\\NetBeansProjects\\ProjectLibrary\\src\\Report\\IncomeExpensesReport.jrxml"));
                JasperDesign jd=JRXmlLoader.load(in);   
                JasperReport jr=JasperCompileManager.compileReport(jd);
                HashMap param=new HashMap();
                param.put("From", fD);
                param.put("To",tD);
                param.put("toCustomer",paisa);
                JasperPrint j=JasperFillManager.fillReport(jr, param,connect.getConn());
                JasperViewer.viewReport(j, false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex); 
            }
            
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
