package projectlibrary;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContactDeveloper extends JFrame {
    private JPanel topLevel,homePnl;
    private JLabel label1,label2,himal,susil,hEmail,sEmail;
    private JButton homeBtn;
    private Border border;
    public ContactDeveloper() {
        super("Contact Developers");
        super.setIconImage(new ImageIcon("Icons\\TryingIcons.jpg").getImage());
        setSize(300,170);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        homePnl=new JPanel();
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
        topLevel=new JPanel();
        topLevel.setLayout(new GridBagLayout());

        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(5,5,10,20);
        g.gridx=0;
        g.gridy=0;
        label1=new JLabel("Name");
        topLevel.add(label1,g);
        g.gridy++;
        himal=new JLabel("Himal Poudel");
        topLevel.add(himal,g);
        g.gridy++;
        susil=new JLabel("Susil Poudel");
        topLevel.add(susil,g);
        g.gridx++;
        g.gridy=0;
        label2=new JLabel("Email");
        topLevel.add(label2,g);
        g.gridy++;
        hEmail=new JLabel("himalp334@gmail.com");
        topLevel.add(hEmail,g);
        g.gridy++;
        sEmail=new JLabel("susilsisal@gmail.com");
        topLevel.add(sEmail,g);
        add(topLevel,BorderLayout.CENTER);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ContactDeveloper contact=new ContactDeveloper();
            }
        });
    }*/
}
