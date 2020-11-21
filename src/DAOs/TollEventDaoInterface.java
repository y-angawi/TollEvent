/*Name: Yahya Angawi
  Student ID: D00233709
*/

package Daos;

import DTOs.Billing;
import DTOs.TollEvent;
import Exceptions.DaoException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TollEventDaoInterface 
{
    public Set<String> createLookUpTAble() throws DaoException;
    public  void flushToDatabse() throws DaoException;
    public void readTollEvent() throws FileNotFoundException;
    public void insertVehicles() throws DaoException;
    public List<TollEvent> findAllTollEvents() throws DaoException;
    public List<TollEvent> findAllTollEventsByReg(String reg) throws DaoException;
    public List<TollEvent> findAllTollEventsByTS(String ts) throws DaoException;
    public List<TollEvent> findAllTollEventsByTSRange(String s_ts, String f_ts) throws DaoException;
    public List<String> findAllReg() throws DaoException;
    public Map<String, ArrayList<TollEvent>> findTollEventsMap() throws DaoException;
    public List<TollEvent> PrintInvalidEventTest();
    public Set<String> PrintlookUpTAbleTest();
    public List<TollEvent> readTollEventsFromFileTest();
    public List<TollEvent> readTollEventsFromFileByRegTest(String regg);
    public List<TollEvent> readTollEventsFromFileByTSTest(String ts);
    public List<String> FindAllRegTest();
    public Map<String, ArrayList<TollEvent>> copyTollEventsMapTest();
    public void addTolleventToDatabse(long imgId, String reg, String time) throws DaoException;
    public List<Billing> processBilling() throws DaoException;
    public boolean findAllTollEventsByImgID(long reg) throws DaoException;
}
