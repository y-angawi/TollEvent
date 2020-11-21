/*Name: Yahya Angawi
  Student ID: D00233709
*/

package BusinessObjects;

import DTOs.Billing;
import DTOs.TollEvent;
import Daos.MySqlTollEventDao;
import Exceptions.DaoException;
import java.util.List;
import Daos.TollEventDaoInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class Server {

    private static TollEventDaoInterface IUserDao = new MySqlTollEventDao();

    public static void main(String[] args) throws DaoException, FileNotFoundException {
        /*THE FOLLOWING METHODS MUST BE CALLED IN THE SAME ORDER AS BELOW*/
        //popilate the table 
        System.out.println("Populating the Vehicles (Registration Numbers) Database...");
        IUserDao.insertVehicles(); //ok
        System.out.println("Reading Toll-Events From File...");
        Set<String> lookup = IUserDao.createLookUpTAble(); //ok
        IUserDao.readTollEvent();
        IUserDao.flushToDatabse();
        Server server = new Server();
        server.start();  //"IUse..." -> "I" for Interface

    }

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(8080);  // set up ServerSocket to listen for connections on port 8080

            System.out.println("Server: Server started. Listening for connections on port 8080...");

            int clientNumber = 0;  // a number for clients that the server allocates as clients connect

            while (true) // loop continuously to accept new client connections
            {
                Socket socket = ss.accept();    // listen (and wait) for a connection, accept the connection, 
                // and open a new socket to communicate with the client
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected.");

                System.out.println("Server: Port# of remote client: " + socket.getPort());
                System.out.println("Server: Port# of this server: " + socket.getLocalPort());

                Thread t = new Thread(new ClientHandler(socket, clientNumber)); // create a new ClientHandler for the client,
                t.start();                                                  // and run it in its own thread

                System.out.println("Server: ClientHandler started in thread for client " + clientNumber + ". ");
                System.out.println("Server: Listening for further connections...");
            }
        } catch (IOException e) {
            System.out.println("Server: IOException: " + e);
        }
        System.out.println("Server: Server exiting, Goodbye!");
    }

    public class ClientHandler implements Runnable // each ClientHandler communicates with one Client
    {

        BufferedReader socketReader;
        PrintWriter socketWriter;
        Socket socket;
        int clientNumber;

        public ClientHandler(Socket clientSocket, int clientNumber) {
            try {
                InputStreamReader isReader = new InputStreamReader(clientSocket.getInputStream());
                this.socketReader = new BufferedReader(isReader);

                OutputStream os = clientSocket.getOutputStream();
                this.socketWriter = new PrintWriter(os, true); // true => auto flush socket buffer

                this.clientNumber = clientNumber;  // ID number that we are assigning to this client

                this.socket = clientSocket;  // store socket ref for closing 

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            ArrayList<TollEvent> invalidTollEvents = new ArrayList<>();
            try {
                while ((message = socketReader.readLine()) != null) {
                    System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + message);

                    if (message.startsWith("\"PacketType\" : \"Heartbeat\"")) {
                        socketWriter.println("\"PacketType\" : \"HeartBeat response\"");

                    } else if (message.startsWith("\"PacketType\" : \"GetRegisteredVehicles\"")) {
                        List<String> reglist = IUserDao.findAllReg();
                        String list = buildVehiclesJson(reglist);
                        socketWriter.println("\"PacketType\" : \"ReturnRegisteredVehicles\" " + list);  // send message to client

                    } else if (message.startsWith("\"PacketType\" : \"ViewAllValidTollEvent\"")) {
                        List<TollEvent> list = IUserDao.findAllTollEvents();
                        String toll = buildValidTollEventJson(list);
                        socketWriter.println("\"PacketType\" : \"ReturnAllValidTollEvent\" " + toll);
                        //4
                    } else if (message.startsWith("\"PacketType\" : \"RegisterValidTollEvent\"")) {
                        String jarray = message.substring(53); // so that the json array start with [
                        JSONArray jsonObject = new JSONArray(jarray);
                        for (int i = 0; i < jsonObject.length(); i++) {
                            JSONObject object = jsonObject.getJSONObject(i);
                            String reg = object.getString("Vehicle Registration");
                            long imgId = object.getLong("Vehicle Image ID");
                            String time = object.getString("Timestamp");
                            if (IUserDao.findAllTollEventsByImgID(imgId) == false) {
                                IUserDao.addTolleventToDatabse(imgId, reg, time);
                            }
                        }
                        socketWriter.println("\"PacketType\" : \"RegisteredValidTollEvent\"");
                        //5
                    } else if (message.startsWith("{ \"PacketType\" : \"RegisterInvalidTollEvent\"")) {
                        JSONObject jsonObject = new JSONObject(message);
                        String reg = jsonObject.getString("Vehicle Registration");
                        long imgId = jsonObject.getLong("Vehicle Image ID");
                        String time = jsonObject.getString("LocalDateTime");
                        invalidTollEvents.add(new TollEvent(imgId, reg, time));
                        socketWriter.println("\"PacketType\" : \"RegisteredInvalidTollEvent\"");
                        //6
                    } else if (message.startsWith("\"PacketType\" : \"Billing\"")) {
                        List<Billing> list = IUserDao.processBilling();
                        String bill = buildBillingJson(list);
                        socketWriter.println("\"PacketType\" : \"Billing\" " + bill);

                    } else if (message.startsWith("\"PacketType\" : \"Close\"")) {
                        if (invalidTollEvents.size() > 0) {
                            System.out.println("List Of Invalid Toll-Events: ");
                            for (TollEvent toll : invalidTollEvents) {
                                System.out.println(toll.toString());
                            }
                        }
                        break;
                    } else {
                        socketWriter.println("I'm sorry I don't understand :(");
                    }
                }

                socket.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (DaoException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
        }
    }

    public static String buildVehiclesJson(List<String> list) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

        for (String u : list) {
            arrayBuilder.add(u);
        }

        // build the JsonArray
        JsonArray jsonArray = arrayBuilder.build();

        // wrap the JsonArray in a JsonObject and give the JsonArray a key name
        JsonObject jsonRootObject
                = Json.createObjectBuilder()
                        .add("Vehicles", jsonArray)
                        .build();

        return jsonRootObject.toString();
    }

    public static String buildValidTollEventJson(List<TollEvent> list) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

        for (TollEvent u : list) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("ImageId", u.getImgId())
                    .add("Registration", u.getReg())
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

    public static String buildBillingJson(List<Billing> list) {
        JsonBuilderFactory factory = Json.createBuilderFactory(null);
        JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

        for (Billing b : list) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("Name", b.getCustomerName())
                    .add("Cost", b.getCost())
                    .build()
            );
        }

        // build the JsonArray
        JsonArray jsonArray = arrayBuilder.build();

        // wrap the JsonArray in a JsonObject and give the JsonArray a key name
        JsonObject jsonRootObject
                = Json.createObjectBuilder()
                        .add("Billing", jsonArray)
                        .build();

        return jsonRootObject.toString();
    }

}
