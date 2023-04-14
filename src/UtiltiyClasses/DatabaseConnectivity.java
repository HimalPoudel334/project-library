package UtiltiyClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;

public class DatabaseConnectivity {
    private String usrnam,pass,isAdmin;
    private ResultSet res;
    private Statement st;
    Connection myConn;
    public int test;
    public DatabaseConnectivity() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            myConn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db","root","");
            st=myConn.createStatement();
 
            String use="USE library_db";
            st.execute(use);

        }catch (ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,"Connection Failed\nCould not connect to database server!","Database Server Connection",JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
      Returns an integer.
     * Returns 1 if the user is Admin.
     * Returns 2 if the user is User.
     * Otherwise returns 0. 
     * Parameters: <tt>username:String</tt>, the username <tt>password:String</tt>, the password
     */
    public Connection getConn(){
        return myConn;
    }
    public int validateMember(String username,String password){
        try {
            String sql="SELECT username,password,isadmin FROM user_login_details WHERE username='"+username+"'";
            res=returnResult(sql);
            if(res.next()){
                usrnam=res.getString("username");
                pass=res.getString("password");
                isAdmin=res.getString("isadmin");
                if(username.equals(usrnam)&&password.equals(pass)){                
                    if(isAdmin.equals("1"))
                        test=1;
                    else
                        test=2; 
                }
                else{
                    JOptionPane.showMessageDialog(null,"Invalid username/password!","User Login",JOptionPane.ERROR_MESSAGE);
                    test=0;
                }        
            }else{
                test=0;
                JOptionPane.showMessageDialog(null,"Invalid username/password!","User Login",JOptionPane.ERROR_MESSAGE);
            }                
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage(),"User Login",JOptionPane.ERROR_MESSAGE);
        }
        return test;
    }
    /**
     * Executes the SQL query.
     * Parameters: <tt>query:String</tt>, The query to be executed.
     *             <tt>what:String</tt>The operation to be performed.
     */
    public boolean executeSql(String query,String what){
        boolean flag;
        try{
            st.execute(query);
            flag=true;
        }catch(SQLException e){
            flag=false;
            JOptionPane.showMessageDialog(null,"Please fill the required fields!\n"+e.getMessage(),what,JOptionPane.ERROR_MESSAGE);
        }
        return flag;
    }
    /**
     * Returns the ResultSet of the query given in argument.
     * Parameter: <tt>query:String</tt>, The query to be executed.
     */
    public ResultSet returnResult(String query){
        try{
            res=st.executeQuery(query);
        }catch(SQLException e){
            System.out.println(e);
        }
        return res;
    }
    /**
     * Executes the SQL query for the given number of times.
     * Parameters: <tt>query:String</tt>, The query to be executed.<tt>what:String</tt>, The operation to be performed.<tt>times:int</tt>How many times.
     */
    public boolean executeSql(String query,String what,int times){
        boolean flag;
        try{
            for(int i=1;i<=times;i++)
                st.execute(query);
            flag=true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Operation Failed!",what,JOptionPane.ERROR_MESSAGE);
            flag=false;
        }
        return flag;
    }
    public boolean executeSql(ArrayList<String> queries,String what){
        boolean flag=true;
        for(int i=0;i<queries.size();i++){
            try {
                st.execute(queries.get(i));
            } catch (SQLException ex) {
                flag=false;
                JOptionPane.showMessageDialog(null,"Operation Failed!\n"+ex.getMessage(),what,JOptionPane.ERROR_MESSAGE);
            }
        }
        return flag;
    }
    
    public void prepareStatement(String dbName,int id,File fil,int type){
        try {
            FileInputStream insf=new FileInputStream(fil);
            if(type>0){
                String sql="DELETE FROM members_photo WHERE mid="+id;
                executeSql(sql,"");
            }
            PreparedStatement stp=myConn.prepareStatement("INSERT INTO "+dbName+" VALUES(?,?)");
            stp.setInt(1,id);
            stp.setBinaryStream(2,(InputStream)insf,(int)fil.length());
            stp.executeUpdate();
        } catch (SQLException | FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null,"Operation Failed!\n"+ex.getMessage(),"Insert Image",JOptionPane.ERROR_MESSAGE);
        }
    }
    public Vector getColumnsHeading(String sql,ResultSet rs){
        Vector<String> vc=new Vector<>();
        try {
            vc.clear();
            ResultSetMetaData rsmd=rs.getMetaData();
            for(int i=1;i<=rsmd.getColumnCount();i++)
                vc.add(rsmd.getColumnName(i));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Operation Failed!\n"+ex.getMessage(),"Insert Image",JOptionPane.ERROR_MESSAGE);
        }
        return vc;
    }
    
}