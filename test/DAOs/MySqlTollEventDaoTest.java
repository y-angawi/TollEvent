/*Name: Yahya Angawi
  Student ID: D00233709
*/

package DAOs;

import DTOs.Billing;
import DTOs.TollEvent;
import Daos.MySqlTollEventDao;
import Exceptions.DaoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author madwolff
 */
public class MySqlTollEventDaoTest {

    private MySqlTollEventDao instance = new MySqlTollEventDao();

    public MySqlTollEventDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance.insertVehicles();
        try {
            Set<String> lookup = instance.createLookUpTAble(); //ok
            instance.readTollEvent();
            instance.flushToDatabse();
        } catch (DaoException ex) {
            Logger.getLogger(MySqlTollEventDaoTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createLookUpTAble method, of class MySqlTollEventDao.
     */
    @Test
    public void testCreateLookUpTAble() throws Exception {
        System.out.println("createLookUpTAble");

        Set<String> expResult = new HashSet<>();
        Scanner in = new Scanner(new File("Vehicles.csv"));

        // Use any character other than a-z or A-Z as a delimiter.
        // The "^" indicates 'NOT', so match characters other than characters a-z or A-Z
        in.useDelimiter("[^a-zA-Z0-9]+");

        while (in.hasNext()) {

            String reg = in.nextLine();

            expResult.add(reg);
        }

        Set<String> result = instance.createLookUpTAble();
        assertEquals(expResult, result);

    }

    /**
     * Test of flushToDatabse method, of class MySqlTollEventDao.
     */
    @Test
    public void testFlushToDatabse() throws Exception {
        System.out.println("flushToDatabse");
        instance.flushToDatabse();
        //Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Getting the connection
      String mysqlUrl = "jdbc:mysql://localhost:3306/test";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
      //Creating the Statement object
      Statement stmt = con.createStatement();
      //Query to get the number of rows in a table
      String query = "select count(*) from tollevents";
      //Executing the query
      ResultSet rs = stmt.executeQuery(query);
      //Retrieving the result
      rs.next();
      int count = rs.getInt(1);
    
        assertTrue(count == 34);
    }
    /**
     * Test of readTollEvent method, of class MySqlTollEventDao.
     */
    @Test
    public void testReadTollEvent() throws Exception {
        System.out.println("readTollEvent");
        instance.flushToDatabse();
        //Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Getting the connection
      String mysqlUrl = "jdbc:mysql://localhost:3306/test";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
      //Creating the Statement object
      Statement stmt = con.createStatement();
      //Query to get the number of rows in a table
      String query = "select count(*) from tollevents";
      //Executing the query
      ResultSet rs = stmt.executeQuery(query);
      //Retrieving the result
      rs.next();
      int count = rs.getInt(1);
    
        assertTrue(count == 34);
    }
    /**
     * Test of insertVehicles method, of class MySqlTollEventDao.
     */
    @Test
    public void testInsertVehicles() throws Exception {
        System.out.println("insertVehicles");
        instance.flushToDatabse();
        //Registering the Driver
      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
      //Getting the connection
      String mysqlUrl = "jdbc:mysql://localhost:3306/test";
      Connection con = DriverManager.getConnection(mysqlUrl, "root", "");
      System.out.println("Connection established......");
      //Creating the Statement object
      Statement stmt = con.createStatement();
      //Query to get the number of rows in a table
      String query = "select count(*) from Vehicles";
      //Executing the query
      ResultSet rs = stmt.executeQuery(query);
      //Retrieving the result
      rs.next();
      int count = rs.getInt(1);
        
        assertTrue(count == 30);
    }
    /**
     * Test of findAllTollEvents method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindAllTollEvents() throws Exception {
        System.out.println("findAllTollEvents");
        List<TollEvent> expResult = instance.readTollEventsFromFileTest();
        List<TollEvent> result = instance.findAllTollEvents();
          
        assertTrue(result.containsAll(expResult) && expResult.containsAll(result));
    }

    /**
     * Test of findAllTollEventsByReg method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindAllTollEventsByReg() throws Exception {
        System.out.println("findAllTollEventsByReg");
        String reg = "181MH3461";
        List<TollEvent> expResult = new ArrayList<>();
        List<TollEvent> result = instance.findAllTollEventsByReg(reg);
        TollEvent t = new TollEvent(30441, reg, "2020-02-17T23:25:10.654Z");
        expResult.add(t);
        
        assertEquals(expResult, result);
    }
    /**
     * Test of findAllTollEventsByTS method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindAllTollEventsByTS() throws Exception {
        System.out.println("findAllTollEventsByTS");
        String ts = "2020-02-17T23:25:10.654Z";
        MySqlTollEventDao instance = new MySqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>();
        TollEvent t = new TollEvent(30441, "181MH3461", ts);
        expResult.add(t);
        List<TollEvent> result = instance.findAllTollEventsByTS(ts);
        assertEquals(expResult, result);
    }
    /**
     * Test of findAllTollEventsByTSRange method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindAllTollEventsByTSRange() throws Exception {
        System.out.println("findAllTollEventsByTSRange");
        String s_ts = "2020-02-17T18:58:08.123Z";
        String f_ts = "2020-02-17T23:25:10.654Z";
        MySqlTollEventDao instance = new MySqlTollEventDao();
        List<TollEvent> expResult = new ArrayList<>();
        
        TollEvent t = new TollEvent(30441, "181MH3461", f_ts);
        TollEvent t2 = new TollEvent(30439, "181MH3459", s_ts);
        expResult.add(t);
        expResult.add(t2);
        List<TollEvent> result = instance.findAllTollEventsByTSRange(s_ts, f_ts);
        assertTrue(expResult.containsAll(result)&&result.containsAll(expResult));
    }
    /**
     * Test of findAllReg method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindAllReg() throws Exception {
        System.out.println("findAllReg");
        
        List<String> expResult = instance.FindAllRegTest();
        List<String> result = instance.findAllReg();
        
        assertTrue(expResult.containsAll(result)&&result.containsAll(expResult));
    }
    /**
     * Test of findTollEventsMap method, of class MySqlTollEventDao.
     */
    @Test
    public void testFindTollEventsMap() throws Exception {
        System.out.println("findTollEventsMap");
        Map<String, ArrayList<TollEvent>> expResult = instance.copyTollEventsMapTest();
        Map<String, ArrayList<TollEvent>> result = instance.findTollEventsMap();
        
        System.out.println(expResult);
        System.out.println(result);
        
        assertTrue(expResult.size() == result.size() );
    }

    /**
     * Test of processBilling method, of class MySqlTollEventDao.
     */
    @Test
    public void testProcessBilling() throws Exception {
        System.out.println("processBilling");
        MySqlTollEventDao instance = new MySqlTollEventDao();
        List<Billing> expResult = new ArrayList<>();
        expResult.add(new Billing("adam", 35));
        expResult.add(new Billing("alice wonderland", 20));
        expResult.add(new Billing("anne", 60));
        expResult.add(new Billing("crowley", 30));
        expResult.add(new Billing("derek", 30));
        expResult.add(new Billing("dermont", 30));
        expResult.add(new Billing("john doe", 55));
        expResult.add(new Billing("john wick", 30));
        expResult.add(new Billing("yahya angawi", 25));
        List<Billing> result = instance.processBilling();
        assertEquals(expResult, result);
    }
}
