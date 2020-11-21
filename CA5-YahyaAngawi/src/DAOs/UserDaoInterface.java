/**                                               Feb 2019          
 * UserDaoInterface
 * 
 * Declares the methods that all UserDAO types must implement,
 * be they MySql User DAOs or Oracle User DAOs etc...
 * 
 * Classes from the Business Layer (users of this DAO interface)
 * should use reference variables of this interface type to avoid 
 * dependencies on the underlying concrete classes (e.g. MySqlUserDao).
 * 
 * More sophistocated implementations will use a factory
 * method to instantiate the appropriate DAO concrete classes
 * by reading database configuration information from a 
 * configuration file (that can be changed without altering source code)
 * 
 * Interfaces are also useful when testing, as concrete classes
 * can be replaced by mock DAO objects.
 */
package Daos;

import DTOs.TollEvent;
import Exceptions.DaoException;
import java.util.List;
import java.util.Map;

public interface UserDaoInterface 
{
    public List<TollEvent> findAllUsers() throws DaoException;
    public TollEvent findUserByUsernamePassword(String uname, String pword) throws DaoException ;
    public List<TollEvent> findAllUsersNameStartWith(String s) throws DaoException;
    public boolean insertUser(String fname, String lname, String uname, String pass) throws DaoException;
    public int coutUsers() throws DaoException;
    public boolean updatePassword(String pass, int id) throws DaoException;
    public Map<Integer, TollEvent> findUsersByMap(int id) throws DaoException;
}
