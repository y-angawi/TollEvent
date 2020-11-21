/*Name: Yahya Angawi
  Student ID: D00233709
*/

package BusinessObjects;

import DTOs.TollEvent;
import Daos.MySqlTollEventDao;
import Exceptions.DaoException;
import java.util.List;
import Daos.TollEventDaoInterface;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws DaoException, FileNotFoundException {
        TollEventDaoInterface IUserDao = new MySqlTollEventDao();  //"IUse..." -> "I" for Interface
        List<TollEvent> list = null;
        /*THE FOLLOWING METHODS MUST BE CALLED IN THE SAME ORDER AS BELOW*/
        //popilate the table 
        System.out.println("Populating the Vehicles (Registration Numbers) Database...");
        IUserDao.insertVehicles(); //ok
        System.out.println("Reading Toll-Events From File...");
        Set<String> lookup = IUserDao.createLookUpTAble(); //ok
        IUserDao.readTollEvent();
        IUserDao.flushToDatabse();

        boolean exit = false;

        while (exit == false) {
            menu();
            try {
                System.out.print("\nChoose From The Menu: (10 to show available Options)> ");
                int action = sc.nextInt();
                sc.nextLine();

                switch (action) {
                    case 0:
                        System.out.println("Shutting down....");

                        exit = true;
                        break;

                    case 1:
                        System.out.println("Creating the Look-Up Table...");
//                        Set<String> lookup = IUserDao.createLookUpTAble();
                        System.out.println(lookup.toString());
                        break;

                    case 2:
                        System.out.println("Processing Toll-Events...");
                        IUserDao.readTollEvent();
                        break;
                    case 3:
                        System.out.println("Flushing Toll-Events To Databse...");
                        IUserDao.flushToDatabse();
                        break;
                    case 4:
                        System.out.println("Retrieving All Toll-Events From Databse...");
                        list = IUserDao.findAllTollEvents();
                        System.out.println(list.toString());
                        break;
                    case 5:
                        System.out.println("Please Enter The Toll-Event Registration Number: ");
                        String reg = sc.next();
                        list = IUserDao.findAllTollEventsByReg(reg);
                        System.out.println(list);
                        break;
                    case 6:
                        System.out.println("Please Enter The Following TimeStamp Details Of The Toll-Event: ");
                        String ts = createTS();
                        list = IUserDao.findAllTollEventsByTS(ts);
                        System.out.println(list.toString());
                        break;
                    case 7:
                        //2020-02-15T22:15:48.123Z
                        System.out.println("Please Enter The Following Starting TimeStamp Details Of The Toll-Event: ");
                        ts = createTS();
                        System.out.println("Please Enter The Following Ending TimeStamp Details Of The Toll-Event: ");
                        String ts2 = createTS();
                        list = IUserDao.findAllTollEventsByTSRange(ts, ts2);
                        System.out.println(list.toString());
                        break;
                    case 8:
                        List<String> reglist = IUserDao.findAllReg();
                        System.out.println(reglist.toString());
                        break;
                    case 9:
                        Map<String, ArrayList<TollEvent>> map = IUserDao.findTollEventsMap();
                        System.out.println(map);
                        break;
                    case 10:
                        List<TollEvent> invalid = IUserDao.PrintInvalidEventTest();
                        System.out.println(invalid);
                        break;
                }
            } catch (Exception e) {
                System.out.println("Wrong Input! " + e.getMessage());
                continue;
            }
        }
    }

    private static void menu() {
        System.out.println("\nAvailable options:\npress");
        System.out.println("0 - to shutdown\n"
                + "1 - To creating And Printing Look-Up TAble\n"
                + "2 - To Process Toll-Events\n"
                + "3 - To Flush Toll-Events To Databse\n"
                + "4 - To Retrieve All Toll-Events From Databse\n"
                + "5 - To Find All Toll-Events By Registration Number\n"
                + "6 - To Find All Toll-Events By TimeStamp\n"
                + "7 - To Find All Toll-Events By TimeStamp Range\n"
                + "8 - To Find All Registration Numbers\n"
                + "9 - To Find All Toll-Events As Map\n"
                + "10 - To Print Invalid Registration Of Toll-Event");
    }

    private static String createTS() {

        //2020-02-14T10:15:30.123Z
        String ts = "";
        System.out.println("Please Enter The Year Of The Toll-Event (e.g 2020): ");
        String year = sc.next();
        
        System.out.println("Please Enter The Month Of The Toll-Event (e.g 02): ");
        String month = sc.next();
        
        System.out.println("Please Enter The Day Of The Toll-Event (e.g 14): ");
        String day = sc.next();
        
        System.out.println("Please Enter The Hour Of The Toll-Event: (e.g 10)");
        String hour = sc.next();
        
        System.out.println("Please Enter The Minut Of The Toll-Event (e.g 15): ");
        String minut = sc.next();
        
        System.out.println("Please Enter The Seconds Of The Toll-Event (e.g 30.123): ");
        String seconds = sc.next();
        ts = year + "-" + month + "-" + day + "T" + hour + ":" + minut + ":" + seconds + "Z";
        
        System.out.println(ts);
        
        if (!(ts.matches("^(((19|2[0-9])[0-9]{2})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01]))T\\d{2}:\\d{2}:\\d{2}\\.\\d{3,}Z"))) {
            throw new IllegalArgumentException("The TimeStamp Format You Entered Is Invalid");
        }
        
        
        return ts;
    }
}
