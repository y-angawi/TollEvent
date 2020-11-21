/*Name: Yahya Angawi
  Student ID: D00233709
*/

package BusinessObjects;

import static BusinessObjects.Server.buildValidTollEventJson;
import static BusinessObjects.Server.buildVehiclesJson;
import DTOs.TollEvent;
import Daos.MySqlTollEventDao;
import Exceptions.DaoException;
import java.util.List;
import Daos.TollEventDaoInterface;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

public class Client {
    
    private static TollEventDaoInterface IUserDao = new MySqlTollEventDao();

    public static void main(String[] args) throws DaoException, FileNotFoundException {
        /*THE FOLLOWING METHODS MUST BE CALLED IN THE SAME ORDER AS BELOW*/
        //popilate the table 
        System.out.println("Populating the Vehicles (Registration Numbers) Database...");
        IUserDao.insertVehicles(); //ok
        System.out.println("Reading Toll-Events From File...");
        Set<String> lookup = IUserDao.createLookUpTAble(); 
        IUserDao.readTollEvent();
        IUserDao.flushToDatabse();
        Client client = new Client();
        client.start();
        
    }

    public void start() {
        Scanner in = new Scanner(System.in);
        String command = "";
        String reg = "";
        String time = "";
        long imgId = 0;

        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket, and open new socket
            Scanner socketReader = new Scanner(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PrintWriter socketWriter = new PrintWriter(os, true);

            System.out.println("Client: Port# of this client : " + socket.getLocalPort());
            System.out.println("Client: Port# of Server :" + socket.getPort());
            System.out.println("Client: This Client is running and has connected to the server");

            command = "\"PacketType\" : \"GetRegisteredVehicles\"";
            socketWriter.println(command);
            String v = socketReader.nextLine();

            System.out.println("Client: Response from server: \n" + v);

            // read a command from the user
            boolean exit = false;

            while (!exit) {
                menu();
                int action = in.nextInt();
                try {
                    switch (action) {
                        case 0:
                            command = "\"PacketType\" : \"Close\"";
                            socketWriter.println(command);
                            System.out.println("Shutting Down And Closing Connection.....");
                            exit = true;
                            break;

                        case 1:
                            command = "\"PacketType\" : \"Heartbeat\"";
                            socketWriter.println(command);
                            break;

                        case 2:
                            command = "\"PacketType\" : \"GetRegisteredVehicles\"";
                            socketWriter.println(command);
                            break;

                        case 3:
                            command = "\"PacketType\" : \"ViewAllValidTollEvent\"";
                            socketWriter.println(command);
                            break;
                        case 4:
                            List<TollEvent> tollevents = IUserDao.readTollEventsFromFileTest();
                            String tolls = buildValidTollEventJson(tollevents);
                            
                            socketWriter.println("\"PacketType\" : \"RegisterValidTollEvent\"" + tolls);
                            break;
                        case 5:
                            System.out.println("Please Enter The Registration Number: ");
                            in.nextLine();// had to add this line, somehow it skipping the user input
                            reg = in.nextLine();
                            System.out.println("Please Enter The Vehicle Image ID: ");
                            imgId = in.nextLong();
                            System.out.println("Please Enter The Local Date And Time: ");
                            time = createTS();
                            command = "{ \"PacketType\" : \"RegisterInvalidTollEvent\", "
                                    + "\"Vehicle Registration\" : \"" + reg + "\", "
                                    + "\"Vehicle Image ID\" : \"" + imgId + "\", "
                                    + "\"LocalDateTime\" : \"" + time + "\" }";
                            socketWriter.println(command);
                            break;
                        case 6:
                            command = "\"PacketType\" : \"Billing\"";
                            socketWriter.println(command);
                            break;
                    }

                    if (command.startsWith("\"PacketType\" : \"Heartbeat\"")) // we expect the server to return a time (in milliseconds)
                    {
                        String heart = socketReader.nextLine(); // wait for, and read time (as we expect time reply)
                        System.out.println("Client: Response from server: " + heart);

                    } else if (command.startsWith("\"PacketType\" : \"GetRegisteredVehicles\"")) // we expect the server to return a time (in milliseconds)
                    {
                        v = socketReader.nextLine();

                        System.out.println("Client: Response from server: \n" + v);

                    } else if (command.startsWith("\"PacketType\" : \"ViewAllValidTollEvent\"")) // we expect the server to return a time (in milliseconds)
                    {
                        v = socketReader.nextLine();

                        System.out.println("Client: Response from server: Time: " + v);
                        //4
                    } else if (command.startsWith("\"PacketType\" : \"RegisterValidTollEvent\"")) // we expect the server to return a time (in milliseconds)
                    {
                        v = socketReader.nextLine();

                        System.out.println("Client: Response from server: " + v);
                        //5
                    } else if (command.startsWith("\"PacketType\" : \"RegisterInvalidTollEvent\"")) // we expect the server to return a time (in milliseconds)
                    {
                        v = socketReader.nextLine();

                        System.out.println("Client: Response from server: " + v);
                        //6
                    }else if (command.startsWith("\"PacketType\" : \"Billing\"")) // we expect the server to return a time (in milliseconds)
                    {
                        v = socketReader.nextLine();

                        System.out.println("Client: Response from server: " + v);
                        //6
                    }else // the user has entered the close command or an invalid command
                    {
                        String input = socketReader.nextLine();// wait for, and retrieve the echo ( or other message)
                        System.out.println("Client: Response from server: \"" + input + "\"");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
            socketWriter.close();
            socketReader.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
    }

    private static String createTS() {
        Scanner sc = new Scanner(System.in);

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

        //2020-02-14T10:15:30.123Z
        if (!(ts.matches("^(((19|2[0-9])[0-9]{2})\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01]))T\\d{2}:\\d{2}:\\d{2}\\.\\d{3,}Z"))) {
            throw new IllegalArgumentException("The TimeStamp Format You Entered Is Invalid");
        }

        return ts;
    }

    private static void menu() {

        System.out.println("Please enter a command: ");
        System.out.println("\nAvailable command:\npress");
        System.out.println("0 - to shutdown\n"
                + "1 - To Heartbeat the server\n"
                + "2 - To Get All Registered Vehicles\n"
                + "3 - To Retrieve All Toll-Events From Databse\n"
                + "4 - To Register a Valid Toll-Event\n"
                + "5 - To Register an Invalid Toll-Event\n"
                + "6 - To Show Billing System\n");
        System.out.print("\nChoose From The Menu: (7 to show available Commands)> ");
    }
    public static String buildValidTollEventJson(List<TollEvent> list)
    {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

        for (TollEvent u : list) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("Vehicle Image ID", u.getImgId())
                    .add("Vehicle Registration", u.getReg())
                    .add("Timestamp", u.getTimestamp())
                    .build()
            );
        }
        
        // build the JsonArray
        JsonArray jsonArray = arrayBuilder.build();

        // wrap the JsonArray in a JsonObject and give the JsonArray a key name
        JsonObject jsonRootObject
                = Json.createObjectBuilder()
                        .add("TollEvents", jsonArray)
                        .build();

        return jsonRootObject.toString();
    }
}
