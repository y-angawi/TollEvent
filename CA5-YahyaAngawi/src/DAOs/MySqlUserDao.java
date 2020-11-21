/**                                             Feb 2019
 * 
 * Data Access Object (DAO) for User table with MySQL-specific code
 * This 'concrete' class implements the 'UserDaoInterface'.
 * 
 * The DAO will contain the SQL query code to interact with the database,
 * so, the code here is specific to a particular database (e.g. MySQL or Oracle etc...)
 * No SQL queries will be used in the Business logic layer of code, thus, it 
 * will be independent of the database specifics.
 * 
*/

package Daos;

import DTOs.TollEvent;
import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MySqlUserDao extends MySqlDao implements UserDaoInterface
{
    @Override
    public List<TollEvent> findAllUsers() throws DaoException 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> users = new ArrayList<>();
        
        try 
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM USER";
            ps = con.prepareStatement(query);
            
            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String lastname = rs.getString("LAST_NAME");
                String firstname = rs.getString("FIRST_NAME");
//                TollEvent u = new TollEvent(userId, firstname, lastname, username, password);
//                users.add(u);
            }
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findAllUsers() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return users;     // may be empty
    }

    @Override
    public TollEvent findUserByUsernamePassword(String uname, String pword) throws DaoException 
    {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TollEvent u = null;
        try {
            con = this.getConnection();
            
            String query = "SELECT * FROM USER WHERE USERNAME = ? AND PASSWORD = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, uname);
            ps.setString(2, pword);
            
            rs = ps.executeQuery();
            if (rs.next()) 
            {
            	int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String lastname = rs.getString("LAST_NAME");
                String firstname = rs.getString("FIRST_NAME");
//                u = new TollEvent(userId, firstname, lastname, username, password);
            }
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findUserByUsernamePassword() " + e.getMessage());
            }
        }
        return u;     // u may be null 
    }  
    
    /**
     *
     * @param s
     * @return
     * @throws DaoException
     */
    @Override
    public List<TollEvent> findAllUsersNameStartWith(String s) throws DaoException 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> users = new ArrayList<>();
        
        try 
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM USER WHERE USERNAME LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, s + "%");
            
            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String lastname = rs.getString("LAST_NAME");
                String firstname = rs.getString("FIRST_NAME");
//                TollEvent u = new TollEvent(userId, firstname, lastname, username, password);
//                users.add(u);
            }
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findAllUsers() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return users;     // may be empty
    }
    
    @Override
    public Map<Integer, TollEvent> findUsersByMap(int id) throws DaoException 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, TollEvent> users = new HashMap<>();
        
        try 
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM USER";
            ps = con.prepareStatement(query);
            
            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) 
            {
                int userId = rs.getInt("ID");
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String lastname = rs.getString("LAST_NAME");
                String firstname = rs.getString("FIRST_NAME");
//                TollEvent u = new TollEvent(userId, firstname, lastname, username, password);
//                users.put(userId, u);
            }
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findAllUsers() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return users;      // may be empty
    }
    
     @Override
    public boolean insertUser(String fname, String lname, String uname, String pass) throws DaoException 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;
        
        try 
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "insert into user VALUES (null, ?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, uname);
            ps.setString(4, pass);
            ps.executeUpdate();
            
            success = (ps.executeUpdate() == 1);
            System.out.println("User Data Was Inserted");
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findAllUsers() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return success;     // may be empty
    }
    
     @Override
    public boolean updatePassword(String pass, int id) throws DaoException 
    {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;
        
        try 
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "UPDATE user "
                                + "SET PASSWORD = ? "
                                + "WHERE id = ?";
            ps = con.prepareStatement(query);
            
            ps.setString(1, pass);
            ps.setInt(2, id);
            ps.executeUpdate();
            
            success = (ps.executeUpdate() == 1);
            System.out.println("User Data Was Inserted");
        } 
        catch (SQLException e) 
        {
            throw new DaoException("findAllUsers() " + e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (rs != null) 
                {
                    rs.close();
                }
                if (ps != null) 
                {
                    ps.close();
                }
                if (con != null) 
                {
                    freeConnection(con);
                }
            } 
            catch (SQLException e) 
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return success;     // may be empty
    }
    
    @Override
    public int coutUsers() throws DaoException 
    {
    	Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
         int rowCount = 0;
           
        try {
             //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT count(*) FROM USER";
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            rs.next();
             rowCount = rs.getInt(1);
             
             
        } catch (SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            //Using a PreparedStatement to execute SQL...
           
        
        return rowCount;     // may be empty
    }
}