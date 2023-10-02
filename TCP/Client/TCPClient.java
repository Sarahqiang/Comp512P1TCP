package Client;

import java.io.*;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.StringTokenizer;
import java.util.Vector;

public class TCPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8081;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient() {
        super();
    }
    public static Vector<String> parse(String command) {
        Vector<String> arguments = new Vector<String>();
        StringTokenizer tokenizer = new StringTokenizer(command, ",");
        String argument = "";
        while (tokenizer.hasMoreTokens()) {
            argument = tokenizer.nextToken();
            argument = argument.trim();
            arguments.add(argument);
        }
        return arguments;
    }

    public void connectServer() {
        try {
            clientSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }

    public void start() {
        connectServer();
        System.out.println();
        System.out.println("Location \"help\" for list of supported commands");

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String command = "";
            Vector<String> arguments = new Vector<String>();
            try {
                System.out.print((char) 27 + "[32;1m\n>] " + (char) 27 + "[0m");
                command = stdin.readLine().trim();
            } catch (IOException io) {
                System.err.println((char) 27 + "[31;1mClient exception: " + (char) 27 + "[0m" + io.getLocalizedMessage());
                io.printStackTrace();
                System.exit(1);
            }

            try {
                arguments = parse(command);
                Command cmd = Command.fromString((String) arguments.elementAt(0));
                execute(cmd, arguments);

            } catch (Exception e) {
                System.err.println((char) 27 + "[31;1mCommand exception: " + (char) 27 + "[0m" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

}
    public static void checkArgumentsCount(Integer expected, Integer actual) throws IllegalArgumentException {
        if (expected != actual) {
            throw new IllegalArgumentException("Invalid number of arguments. Expected " + (expected - 1) + ", received " + (actual - 1) + ". Location \"help,<CommandName>\" to check usage of this command");
        }
    }
    public static int toInt(String string) throws NumberFormatException {
        return (Integer.valueOf(string)).intValue();
    }
    private void reconnect() {
        closeConnection();
        connectServer();
    }

    private String sendCommand(String commandToSend) {
        try {
            out.println(commandToSend);
            String response = in.readLine();
            reconnect();
            return response;
        } catch (IOException e) {
            System.err.println("Error sending command: " + e.getMessage());
            return "Error sending command.";
        }
    }

    public void execute(Command cmd, Vector<String> arguments) {
       try {
           switch (cmd) {
               case Help: {
                   if (arguments.size() == 1) {
                       System.out.println(Command.description());
                   } else if (arguments.size() == 2) {
                       Command l_cmd = Command.fromString((String) arguments.elementAt(1));
                       System.out.println(l_cmd.toString());
                   } else {
                       System.err.println((char) 27 + "[31;1mCommand exception: " + (char) 27 + "[0mImproper use of help command. Location \"help\" or \"help,<CommandName>\"");
                   }
                   break;
               }
               case AddFlight: {
                   checkArgumentsCount(5, arguments.size());

                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + "," +
                           arguments.elementAt(4);
                   System.out.println("Adding a new flight [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Flight Number: " + arguments.elementAt(2));
                   System.out.println("-Flight Seats: " + arguments.elementAt(3));
                   System.out.println("-Flight Price: " + arguments.elementAt(4));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case DeleteFlight: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;

                   System.out.println("Deleting a flight [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Flight Number: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryFlight: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;

                   System.out.println("Querying a flight [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Flight Number: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case AddRooms: {
                   checkArgumentsCount(5, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + "," +
                           arguments.elementAt(4);
                   System.out.println("Adding new rooms [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Room Location: " + arguments.elementAt(2));
                   System.out.println("-Number of Rooms: " + arguments.elementAt(3));
                   System.out.println("-Room Price: " + arguments.elementAt(4));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }case DeleteRooms: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;

                   System.out.println("Deleting all rooms at a particular location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Room Location: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryRooms: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;
                   System.out.println("Querying rooms location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Room Location: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryRoomsPrice: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;
                   System.out.println("Querying rooms price [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Room Location: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case ReserveRoom: {
                   checkArgumentsCount(4, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + ",";
                   System.out.println("Reserving a room at a location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   System.out.println("-Room Location: " + arguments.elementAt(3));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case AddCustomer: {
                   checkArgumentsCount(2, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," ;
                   System.out.println("Adding a new customer [xid=" + arguments.elementAt(1) + "]");
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case AddCustomerID: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," ;
                   System.out.println("Adding a new customer [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);

                   break;
               }
               case AddCars: {
                   checkArgumentsCount(5, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + "," +
                           arguments.elementAt(4);

                   System.out.println("Adding new cars [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Car Location: " + arguments.elementAt(2));
                   System.out.println("-Number of Cars: " + arguments.elementAt(3));
                   System.out.println("-Car Price: " + arguments.elementAt(4));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case DeleteCars: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," ;

                   System.out.println("Deleting all cars at a particular location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Car Location: " + arguments.elementAt(2));

                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case DeleteCustomer: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," ;
                   System.out.println("Deleting a customer from the database [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryCars: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," ;

                   System.out.println("Querying cars location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Car Location: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryCustomer: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," ;

                   System.out.println("Querying customer information [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryFlightPrice: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;
                   System.out.println("Querying a flight price [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Flight Number: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case QueryCarsPrice: {
                   checkArgumentsCount(3, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) ;
                   System.out.println("Querying cars price [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Car Location: " + arguments.elementAt(2));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case ReserveFlight: {
                   checkArgumentsCount(4, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + ",";
                   System.out.println("Reserving seat in a flight [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   System.out.println("-Flight Number: " + arguments.elementAt(3));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case ReserveCar: {
                   checkArgumentsCount(4, arguments.size());
                   String commandToSend = cmd.name() + "," +
                           arguments.elementAt(1) + "," +
                           arguments.elementAt(2) + "," +
                           arguments.elementAt(3) + ",";
                   System.out.println("Reserving a car at a location [xid=" + arguments.elementAt(1) + "]");
                   System.out.println("-Customer ID: " + arguments.elementAt(2));
                   System.out.println("-Car Location: " + arguments.elementAt(3));
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   break;
               }
               case Bundle: {
                   if (arguments.size() < 7) {
                       System.out.println("Error: Not enough arguments for Bundle command.");
                       break;
                   }
                   String xid = arguments.elementAt(1);
                   String customerID = arguments.elementAt(2);
                   String location = arguments.elementAt(arguments.size() - 3);
                   String car = arguments.elementAt(arguments.size() - 2);
                   String room = arguments.elementAt(arguments.size() - 1);
                   StringBuilder commandToSend = new StringBuilder();
                   commandToSend.append(cmd.name()).append(",").append(xid).append(",").append(customerID).append(",");
                   for (int i = 3; i < arguments.size() - 3; i++) {
                       commandToSend.append(arguments.elementAt(i)).append(",");
                   }
                   commandToSend.append(location).append(",").append(car).append(",").append(room).append(",");

                   System.out.println("Bundling resources for customer [xid=" + xid + "]");
                   String response = sendCommand(commandToSend.toString());
                   System.out.println(response);
                   break;
               }
               case Quit:{
                   checkArgumentsCount(1, arguments.size());
                   String commandToSend = cmd.name();
                   String response = sendCommand(commandToSend);
                   System.out.println(response);
                   closeConnection();
                   System.exit(0);
                   break;
               }




           }
       } catch (Exception e) {
           System.err.println("Error executing command: " + e.getMessage());
       }


    }

    public void closeConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TCPClient client = new TCPClient();
        client.connectServer();
        client.start();
    }
}
