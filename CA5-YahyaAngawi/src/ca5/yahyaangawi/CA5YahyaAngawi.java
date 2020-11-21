/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca5.yahyaangawi;

import DTOs.TollEvent;
import Daos.MySqlUserDao;
import Daos.UserDaoInterface;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author madwolff
 */
public class CA5YahyaAngawi {

    /**                                                 OOP Feb 2019
 * This App demonstrates the use of a Data Access Object (DAO) 
 * to separate Business logic from Database specific logic.
 * It uses DAOs, Data Transfer Objects (DTOs), and
 * a DaoInterface to define a contract between Business Objects
 * and DAOs.
 * 
 * "Use a Data Access Object (DAO) to abstract and encapsulate all 
 * access to the data source. The DAO manages the connection with 
 * the data source to obtain and store data" Ref: oracle.com
 * 
 * Here we use one DAO per database table.
 * 
 * Use the SQL script included with this project to create the 
 * required MySQL user_database and user table
 */

  public static void main( String [] args)
    {
        UserDaoInterface IUserDao = new MySqlUserDao();  //"IUse..." -> "I" for Interface
        // Notice that the userDao reference is an Interface type.
        // This allows for the use of different concrete implementations.
        // e.g. we could replace the MySqlUserDao with an OracleUserDao 
        // (accessing an Oracle Database) 
        // without changing anything in the Interface. 
        // If the Interface doesn't change, then none of the
        // code below that uses the interface needs to change.
        // The 'contract' defined by the interface will not be broken.
        // This means that this code is independent of the code
        // used to access the database. (Reduced coupling).
        
        // The Business Objects require that all TollEvent DAOs implement
        // the interface called "UserDaoInterface", as the code uses
        // only references of the interface type to access the DAO methods.
        
        
        try
        {
//            List<User> users = IUserDao.findAllUsers();
//            
//            if( users.isEmpty() )
//                System.out.println("There are no Users");
//            
//            for( TollEvent user : users )
//                System.out.println("TollEvent: " + user.toString());
//            
//            // test dao - with good username and password
//            TollEvent user = IUserDao.findUserByUsernamePassword("smithj", "password");
//            if(user != null)
//                System.out.println("TollEvent found: " + user);
//            else
//                System.out.println("Username with that password not found");
//            
//            // test dao - with bad username
//            user = IUserDao.findUserByUsernamePassword("madmax", "thunderdome");
//            if(user != null)
//                System.out.println("TollEvent found: " + user);
//            else
//                System.out.println("Username with that password not found");
//            
//            List<User> usersStart = IUserDao.findAllUsersNameStartWith("s");
            
//            if( usersStart.isEmpty() )
//                System.out.println("There are no Users");
//            
//            for( TollEvent userr : usersStart )
//                System.out.println("TollEvent: " + userr.toString());
            
//            boolean u = IUserDao.insertUser("yahya", "angawi", "yangawi", "whatever");
            
//            int count = IUserDao.coutUsers();
//            System.out.println(count);
            IUserDao.updatePassword("wahever1", 3);
            List<TollEvent> users = IUserDao.findAllUsers();
            
//            
            for( TollEvent userr : users )
                System.out.println("User: " + userr.toString());
            
            
        }
        catch( DaoException e )
        {
          e.printStackTrace();         
        }       
    }
}
