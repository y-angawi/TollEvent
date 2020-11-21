/*Name: Yahya Angawi
  Student ID: D00233709
*/

package Daos;

import DTOs.Billing;
import DTOs.TollEvent;
import Exceptions.DaoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.reflect.Array.set;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySqlTollEventDao extends MySqlDao implements TollEventDaoInterface {

    private Set<String> lookUpTAble = new HashSet<>();
    private Map<String, ArrayList<TollEvent>> tollEvents = new HashMap<>();
    private List<TollEvent> invalidEvent = new ArrayList<>();

    @Override
    public Set<String> createLookUpTAble() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT REG FROM Vehicles";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) {
                String reg = rs.getString("REG");

                lookUpTAble.add(reg);
            }
        } catch (SQLException e) {
            throw new DaoException("findAllReg() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllReg() " + e.getMessage());
            }
        }
        return lookUpTAble;
    }

    @Override
    public void flushToDatabse() throws DaoException {
        Connection con = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        try {
            con = this.getConnection();
            stmt = con.createStatement();
            // Drop the table IF EXISTS so that it does not give an error if insert in it same record
            String sql = "DROP TABLE IF EXISTS tollevents ";
            stmt.executeUpdate(sql);
            
            String sql2 = "CREATE TABLE IF NOT EXISTS `TollEvents` ("
                            + " `ID` int(11) NOT NULL AUTO_INCREMENT," +
                            "    `imgId` numeric NOT NULL," +
                            "    `reg` varchar(50) NOT NULL," +
                            "    `TIMESTAMP` TIMESTAMP(3) NOT NULL," +
                             "    `bill` varchar(50) ," +
                             "PRIMARY KEY  (`ID`)" +
                            "  );";
            stmt.executeUpdate(sql2);
            System.out.println("CREATING Tollevents Table In database...");
            
            for (String key : tollEvents.keySet()) {
                ArrayList<TollEvent> list = tollEvents.get(key);
                for (TollEvent event : list) {
                    long imgId = event.getImgId();
                    String reg = event.getReg();
                    String time = event.getTimestamp();
                    Instant inst = Instant.parse(time);
                    java.sql.Timestamp ts_now = java.sql.Timestamp.from(inst);

                    String query = "INSERT INTO TollEvents VALUES (null, ?, ?, ?, null)";
                    ps = con.prepareStatement(query);
                    ps.setLong(1, imgId);
                    ps.setString(2, reg);
                    ps.setTimestamp(3, ts_now);

                    ps.executeUpdate();
                }

            }
        } catch (SQLException e) {
            throw new DaoException("flushIntoDatabse() " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("flushIntoDatabse() " + e.getMessage());
            }
        }
    }

    @Override
    public void addTolleventToDatabse(long imgId, String reg, String time) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
                    con = this.getConnection();
                    Instant inst = Instant.parse(time);
                    java.sql.Timestamp ts_now = java.sql.Timestamp.from(inst);

                    String query = "INSERT INTO TollEvents VALUES (null, ?, ?, ?, null)";
                    ps = con.prepareStatement(query);
                    ps.setLong(1, imgId);
                    ps.setString(2, reg);
                    ps.setTimestamp(3, ts_now);
                    
                    ps.executeUpdate();
                    
        } catch (SQLException e) {
            throw new DaoException("addTolleventToDatabse() " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("addTolleventToDatabse() " + e.getMessage());
            }
        }
    }
    
    @Override
    public void readTollEvent() {
        ArrayList<TollEvent> list;
        Scanner in;
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("Toll-Events.csv"));
            
            while ((line = br.readLine()) != null) {
                in = new Scanner(line);
                in.useDelimiter("[^a-zA-Z0-9|\\-|:.]+");
                while (in.hasNext()) {

                    String reg = in.next();
                    long imgId = in.nextLong();
                    String time = in.nextLine().substring(1);
                    TollEvent rego = new TollEvent(imgId, reg, time);
                    
                    if (lookUpTAble.contains(reg)) {
                        if (tollEvents.containsKey(reg)) {
                            list = tollEvents.get(reg);
                            list.add(rego);
                            tollEvents.put(reg, list);
                        } else {

                            list = new ArrayList<>();
                            list.add(rego);
                            tollEvents.put(reg, list);
                        }

                    } else {

                        invalidEvent.add(rego);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void insertVehicles() {
        Connection con = null;
        Statement stmt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();
            stmt = con.createStatement();
            
            String sql = "DROP TABLE IF EXISTS Vehicles ";
            stmt.executeUpdate(sql);
            
            String sql2 = "CREATE TABLE IF NOT EXISTS `Vehicles` ("
                            + " `ID` int(11) NOT NULL AUTO_INCREMENT," +
                            "  `REG` varchar(50) NOT NULL,"
                            + "PRIMARY KEY  (`ID`)" +
                            "  );";
            stmt.executeUpdate(sql2);
            System.out.println("CREATING Vehicles Table In database...");
            
            Scanner in = new Scanner(new File("Vehicles.csv"));

            // Use any character other than a-z or A-Z as a delimiter.
            // The "^" indicates 'NOT', so match characters other than characters a-z or A-Z
            in.useDelimiter("[^a-zA-Z0-9]+");

            while (in.hasNext()) {

                String reg = in.nextLine();

                String query = "insert into Vehicles VALUES (null, ?)";
                ps = con.prepareStatement(query);

                ps.setString(1, reg);
                ps.executeUpdate();

            }
        } catch (SQLException e) {
            try {
                throw new DaoException("insertVehicles() " + e.getMessage());
            } catch (DaoException ex) {
                Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                try {
                    throw new DaoException("insertVehicles() " + e.getMessage());
                } catch (DaoException ex) {
                    Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public List<TollEvent> findAllTollEvents() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TollEvent> TollEvents = new ArrayList<>();

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT imgId, reg, timestamp FROM TollEvents";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) {
                long imgId = rs.getLong("imgId");
                String reg = rs.getString("reg");
                java.sql.Timestamp ts = rs.getTimestamp("timestamp");
                Instant timestamp = ts.toInstant();
                TollEvent tollEvent = new TollEvent(imgId, reg, timestamp.toString());
                TollEvents.add(tollEvent);
            }
        } catch (SQLException e) {
            try {
                throw new DaoException("findAllTollEvents() " + e.getMessage());
            } catch (DaoException ex) {
                Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                try {
                    throw new DaoException("findAllTollEvents() " + e.getMessage());
                } catch (DaoException ex) {
                    Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return TollEvents;     // may be empty
    }

    @Override
    public List<TollEvent> findAllTollEventsByReg(String reg) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TollEvent tollEvent = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        try {
            con = this.getConnection();

            String query = "SELECT imgId, reg, timestamp FROM tollEvents WHERE reg = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, reg);

            rs = ps.executeQuery();
            while(rs.next()) {
                long imgId = rs.getLong("imgId");
                String regg = rs.getString("reg");
                java.sql.Timestamp ts = rs.getTimestamp("timestamp");
                Instant timestamp = ts.toInstant();
                tollEvent = new TollEvent(imgId, regg, timestamp.toString());
                tollEvents.add(tollEvent);
            }
        } catch (SQLException e) {
            throw new DaoException("findTollEventsByReg() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findTollEventsByReg() " + e.getMessage());
            }
        }
        return tollEvents;   
    }
    
    @Override
    public boolean findAllTollEventsByImgID(long imgId) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            con = this.getConnection();

            String query = "SELECT imgId FROM tollEvents WHERE imgId = ?";
            ps = con.prepareStatement(query);
            ps.setLong(1, imgId);

            rs = ps.executeQuery();
            
            if(rs.next() == false){ return false;}
            
        } catch (SQLException e) {
            throw new DaoException("findTollEventsByReg() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findTollEventsByReg() " + e.getMessage());
            }
        }
        return true;   
    }

    @Override
    public List<TollEvent> findAllTollEventsByTS(String ts) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TollEvent tollEvent = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        try {
            con = this.getConnection();

            String query = "SELECT imgId, reg, timestamp FROM TollEvents WHERE TIMESTAMP >= ?";
            ps = con.prepareStatement(query);
            Instant inst = Instant.parse(ts);
            java.sql.Timestamp ts_now = java.sql.Timestamp.from(inst);
            ps.setTimestamp(1, ts_now);

            rs = ps.executeQuery();
            while(rs.next()) {
                long imgId = rs.getLong("imgId");
                String reg = rs.getString("reg");
                tollEvent = new TollEvent(imgId, reg, ts);
                tollEvents.add(tollEvent);
            }
        } catch (SQLException e) {
            throw new DaoException("findTollEventsByTS() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findTollEventsByTS() " + e.getMessage());
            }
        }
        return tollEvents;     
    }

    @Override
    public List<TollEvent> findAllTollEventsByTSRange(String s_ts, String f_ts) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TollEvent tollEvent = null;
        List<TollEvent> tollEvents = new ArrayList<>();
        try {
            con = this.getConnection();

            String query = "SELECT imgId, reg, timestamp FROM TollEvents WHERE TIMESTAMP BETWEEN ? and ? ORDER BY TIMESTAMP ASC";
            ps = con.prepareStatement(query);
            Instant inst = Instant.parse(s_ts);
            java.sql.Timestamp ts_now = java.sql.Timestamp.from(inst);
            Instant inst2 = Instant.parse(f_ts);
            java.sql.Timestamp ts_now2 = java.sql.Timestamp.from(inst2);
            ps.setTimestamp(1, ts_now);
            ps.setTimestamp(2, ts_now2);

            rs = ps.executeQuery();
            while(rs.next()) {
                long imgId = rs.getLong("imgId");
                String reg = rs.getString("reg");
                java.sql.Timestamp ts = rs.getTimestamp("timestamp");
                String instant = ts.toInstant().toString();
                tollEvent = new TollEvent(imgId, reg, instant);
                tollEvents.add(tollEvent);
            }
        } catch (SQLException e) {
            throw new DaoException("findTollEventsByTS() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findTollEventsByTS() " + e.getMessage());
            }
        }
        return tollEvents;     // tollEvent may be null 
    }

    @Override
    public List<String> findAllReg() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> regs = new ArrayList<>();

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT DISTINCT reg FROM TollEvents GROUP BY reg ORDER BY reg ASC";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) {

                String reg = rs.getString("reg");

                regs.add(reg);
            }
        } catch (SQLException e) {
            throw new DaoException("findAllTollEvents() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllTollEvents() " + e.getMessage());
            }
        }
        return regs;     // may be empty
    }

    @Override
    public Map<String, ArrayList<TollEvent>> findTollEventsMap() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, ArrayList<TollEvent>> tollEvents = new HashMap<>();
        ArrayList<TollEvent> list = new ArrayList<>();

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT imgId, reg, timestamp FROM TollEvents";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) {
                long imgId = rs.getLong("imgId");
                String reg = rs.getString("reg");
                java.sql.Timestamp ts = rs.getTimestamp("timestamp");
                Instant timestamp = ts.toInstant();

                TollEvent tollEvent = new TollEvent(imgId, reg, timestamp.toString());

                if (tollEvents.containsKey(reg)) {
                    list = tollEvents.get(reg);
                    list.add(tollEvent);
                    tollEvents.put(reg, list);
                } else {

                    list = new ArrayList<>();
                    list.add(tollEvent);
                    tollEvents.put(reg, list);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findAllTollEvents() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllTollEvents() " + e.getMessage());
            }
        }
        return tollEvents;
    }
    
    @Override
    public List<Billing> processBilling() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Billing bill = null;
        List<Billing> billing = new ArrayList<>();
        try {
            con = this.getConnection();
            //  and `TollEvents`.bill = null
            String query = "SELECT name, sum(cost) as cost FROM TollEvents, customer, vehicle, customer_vehicles, vehicle_type_cost"
                    + " WHERE TollEvents.reg = vehicle.reg and vehicle.id = customer_vehicles.vehicleid and "
                    + "customer.id = customer_vehicles.customerid and vehicle.type = vehicle_type_cost.type "
                    + "AND MONTH(`TollEvents`.`TIMESTAMP`) < MONTH(curdate()) GROUP BY customer.name ORDER BY customer.name ASC";
            ps = con.prepareStatement(query);
            

            rs = ps.executeQuery();
            while(rs.next()) {
                String name = rs.getString("name");
                int cost = rs.getInt("cost");
                bill = new Billing(name, cost);
                billing.add(bill);
            }
            // all registration number in all the records of TollEvents table are also
            // in vehicle table, therefore, they all will be processed and thus i update
            // the status of Bill column to be processed for all the records 
            // id > 0 and
            query = "UPDATE TollEvents SET bill='processed' WHERE bill=null";
            ps = con.prepareStatement(query);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DaoException("processBilling() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    freeConnection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("processBilling() " + e.getMessage());
            }
        }
        return billing;     // tollEvent may be null 
    }
    
     /*THE FOLLOWING METHODS ARE HELPER METHDS THAT ARE USED IN JUNIT TESTING*/
    
    @Override
    public List<TollEvent> PrintInvalidEventTest(){
    
    return invalidEvent;
    }
    
      @Override
    public Set<String> PrintlookUpTAbleTest(){
    
    return lookUpTAble;
    }
    
    @Override
    public Map<String, ArrayList<TollEvent>> copyTollEventsMapTest(){
    
    return tollEvents;
    }
    
    @Override
    public List<TollEvent> readTollEventsFromFileTest(){
    
        List<TollEvent> expResult = new ArrayList<>();
        Scanner in;

        
        try {
            in = new Scanner(new File("Toll-Events.csv"));
            in.useDelimiter("[^a-zA-Z0-9|\\-|:.]+");
        
        while (in.hasNext()) {
            
            String reg = in.next();
            long imgId = in.nextLong();
            String time = in.next();
            
            TollEvent rego = new TollEvent(imgId, reg, time);
            if (lookUpTAble.contains(reg)) {
                expResult.add(rego);
            }
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return expResult;
}
    
    @Override
    public List<TollEvent> readTollEventsFromFileByRegTest(String regg){
    
        List<TollEvent> expResult = new ArrayList<>();
        Scanner in;

        
        try {
            in = new Scanner(new File("Toll-Events.txt"));
            in.useDelimiter("[^a-zA-Z0-9|\\-|:.]+");
        
        while (in.hasNext()) {
            
            String reg = in.next();
            long imgId = in.nextLong();
            String time = in.next();
            
            TollEvent rego = new TollEvent(imgId, reg, time);
            if (lookUpTAble.contains(reg)) {
                if(reg.equalsIgnoreCase(regg)){
                expResult.add(rego);
                }
            }
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return expResult;
}
    @Override
    public List<TollEvent> readTollEventsFromFileByTSTest(String ts){
    
        List<TollEvent> expResult = new ArrayList<>();
        Scanner in;

        
        try {
            in = new Scanner(new File("Toll-Events.txt"));
            in.useDelimiter("[^a-zA-Z0-9|\\-|:.]+");
        
        while (in.hasNext()) {
            
            String reg = in.next();
            long imgId = in.nextLong();
            String time = in.next();
            
            TollEvent rego = new TollEvent(imgId, reg, time);
            if (lookUpTAble.contains(reg)) {
                if(time.equalsIgnoreCase(ts)){
                expResult.add(rego);
                }
            }
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return expResult;
}
    
     @Override
    public List<String> FindAllRegTest(){
    
        List<String> expResult = new ArrayList<>();
        Scanner in;

        
        try {
            in = new Scanner(new File("Toll-Events.txt"));
            in.useDelimiter("[^a-zA-Z0-9|\\-|:.]+");
        
        while (in.hasNext()) {
            
            String reg = in.next();
            long imgId = in.nextLong();
            String time = in.next();
            
            TollEvent rego = new TollEvent(imgId, reg, time);
            if (lookUpTAble.contains(reg)) {
                if(!(expResult.contains(reg))){
                expResult.add(reg);
                }
            }
        }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlTollEventDao.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return expResult;
}

       
}
