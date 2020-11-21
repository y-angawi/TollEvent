/*Name: Yahya Angawi
  Student ID: D00233709
*/

package Daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Exceptions.DaoException;

public class MySqlDao 
{
    public Connection getConnection() throws DaoException 
    {

        String driver = "org.mariadb.jdbc.Driver";
        String url = "jdbc:mariadb://localhost:3306/test?useFractionalSeconds=true";
        String username = "root";
        String password = "";
        Connection con = null;
        
        try 
        {
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } 
        catch (ClassNotFoundException ex1) 
        {
            System.out.println("Failed to find driver class " + ex1.getMessage());
            System.exit(1);
        } 
        catch (SQLException ex2) 
        {
            System.out.println("Connection failed " + ex2.getMessage());
            System.exit(2);
        }
        return con;
    }

    public void freeConnection(Connection con) throws DaoException 
    {
        try 
        {
            if (con != null) 
            {
                con.close();
                con = null;
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }
    }   
}