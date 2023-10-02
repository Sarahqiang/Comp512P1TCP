package TCP;

import Server.Common.ResourceManager;
import Server.Middleware.TCPMiddleware;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private int port;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private TCPMiddleware middleware; // Middleware instance


    public TCPServer(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(10);
        middleware = new TCPMiddleware("Middleware");
        middleware.setFlightManager(new ResourceManager("FlightResourceManager"));
        middleware.setCarManager(new ResourceManager("CarResourceManager"));
        middleware.setRoomManager(new ResourceManager("RoomResourceManager"));
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from: " + clientSocket.getRemoteSocketAddress());

                // Handle each client connection in a separate thread
                executorService.execute(() -> handleClientRequest(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error starting the server: " + e.getMessage());
        } finally {
            stop();
        }
    }

    private void handleClientRequest(Socket clientSocket) {
        try {
            Boolean success = false;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String receivedCommand = in.readLine();
            System.out.println("Received command: " + receivedCommand);
            String[] tokens = receivedCommand.split(",");
            if (tokens.length >= 1) {
                String requestType = tokens[0];
                switch (requestType) {
                    case "AddFlight":
                        success = middleware.addFlight(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                        if (success) {
                            out.println("Flight added successfully!");
                        } else {
                            out.println("Failed to add flight.");
                        }
                        break;
                    case "DeleteFlight":
                        success =  middleware.deleteFlight(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        if (success) {
                            out.println("Flight deleted successfully!");
                        } else {
                            out.println("Failed to deleted flight.");
                        }
                        break;
                    case "QueryFlight":
                            int seats = middleware.queryFlight(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                            out.println("Number of seats available: " + seats);
                        break;
                    case "AddRooms":
                        success = middleware.addRooms(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                        if (success) {
                            out.println("Rooms added!");
                        } else {
                            out.println("Failed to add room.");
                        }
                        break;
                    case "DeleteRooms":
                        success =  middleware.deleteRooms(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                        if (success) {
                            out.println("Rooms deleted successfully!");
                        } else {
                            out.println("Failed to deleted Rooms.");
                        }
                        break;
                    case "QueryRooms":
                            int numRoom = middleware.queryRooms(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                            out.println("Number of rooms at this location: " + numRoom);
                        break;
                    case "QueryRoomsPrice":
                        int price = middleware.queryRoomsPrice(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                        out.println("Price of rooms at this location: " + price);
                        break;
                    case "ReserveRoom":
                        success = middleware.reserveRoom(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), String.valueOf(tokens[3]));
                        if (success) {
                            out.println("Room Reserved");
                        } else {
                            out.println("Room could not be reserved");
                        }
                        break;
                    case "AddCustomer":
                        int customer = middleware.newCustomer(Integer.parseInt(tokens[1]));
                        out.println("Add customer ID: " + customer);
                    case "AddCustomerID":
                        success = middleware.newCustomer(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        if (success) {
                            int customerID = Integer.parseInt(tokens[2]);
                            out.println("Add customer ID: " + customerID);
                        } else {
                            out.println("Customer could not be added");
                        }
                    case "AddCars":
                        success = middleware.addCars(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]));
                        if (success) {
                            out.println("Car added successfully!");
                        } else {
                            out.println("Failed to add car.");
                        }
                        break;
                    case "DeleteCar":
                        success =  middleware.deleteCars(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                        if (success) {
                            out.println("Car deleted successfully!");
                        } else {
                            out.println("Failed to deleted car.");
                        }
                        break;
                    case "QueryCars":
                        int numCars = middleware.queryCars(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                        out.println("Number of cars at this location: " + numCars);
                        break;
                    case "QueryCustomer":
                        String bill = middleware.queryCustomerInfo(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        out.println(bill);
                        out.flush();
                        break;
                    case "QueryFlightPrice":
                        int flightPrice = middleware.queryFlightPrice(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        out.println("Price of the flight is: " + flightPrice);
                        break;
                    case "QueryCarsPrice":
                        int carPrice = middleware.queryCarsPrice(Integer.parseInt(tokens[1]), String.valueOf(tokens[2]));
                        out.println("Price of cars at this location: " + carPrice);
                        break;
                    case "ReserveFlight":
                        success = middleware.reserveFlight(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]));
                        if (success) {
                            out.println("Flight Reserved");
                        } else {
                            out.println("Flight could not be reserved");
                        }
                        break;
                    case "ReserveCar":
                        success = middleware.reserveCar(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), String.valueOf(tokens[3]));
                        if (success) {
                            out.println("Car Reserved");
                        } else {
                            out.println("Car could not be reserved");
                        }
                        break;
                    case "Bundle":
                        int xid = Integer.parseInt(tokens[1]);
                        int customerID = Integer.parseInt(tokens[2]);

                        // Calculate the position of the "location" token based on the total number of tokens
                        int locationIndex = tokens.length - 3;

                        Vector<String> flightNumbers = new Vector<>();
                        for (int i = 3; i < locationIndex; i++) {
                            flightNumbers.add(tokens[i]);
                        }

                        String location = tokens[locationIndex];
                        boolean car = Boolean.parseBoolean(tokens[locationIndex + 1]);
                        boolean room = Boolean.parseBoolean(tokens[locationIndex + 2]);

                        success = middleware.bundle(xid, customerID, flightNumbers, location, car, room);
                        if (success) {
                            out.println("Bundle operation succeeded");
                        } else {
                            out.println("Bundle operation failed");
                        }
                        break;
                    case "Quit":
                        out.println("GoodBye");
                        break;



                    default:
                        out.println("Invalid request type");
                        break;
                }
            } else {
                out.println("Invalid command received");
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error handling client request: " + e.getMessage());
        }
    }


    public void stop() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        TCPServer server = new TCPServer(8081);
        server.start();
    }
}
