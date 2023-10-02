package Server.Middleware;

import Server.Common.*;
import Server.Interface.IResourceManager;
import Server.ResourceManagers.CarResourceManager;
import Server.ResourceManagers.FlightResourceManager;
import Server.ResourceManagers.RoomResourceManager;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class TCPMiddleware extends ResourceManager {
    private IResourceManager flightManager;
    private IResourceManager carManager;
    private IResourceManager roomManager;

    public TCPMiddleware(String p_name) {
        super(p_name);
    }

    public void setFlightManager(IResourceManager RM) {
        this.flightManager = RM;
    }

    public void setCarManager(IResourceManager RM) {
        this.carManager = RM;
    }

    public void setRoomManager(IResourceManager RM) {
        this.roomManager = RM;
    }


    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        synchronized (flightManager) {
            return flightManager.addFlight(id, flightNum, flightSeats, flightPrice);
        }
    }

    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        synchronized (carManager) {
            return carManager.addCars(id, location, numCars, price);
        }
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        synchronized (roomManager) {
            return roomManager.addRooms(id, location, numRooms, price);

        }

    }

    @Override
    public int newCustomer(int xid) throws RemoteException {
        int cid = super.newCustomer(xid);
        flightManager.newCustomer(xid, cid);
        carManager.newCustomer(xid, cid);
        roomManager.newCustomer(xid, cid);

        return cid;
    }

    @Override
    public boolean newCustomer(int xid, int customerID) throws RemoteException {
        boolean isSuccess = super.newCustomer(xid, customerID);
        if (isSuccess) {
            flightManager.newCustomer(xid, customerID);
            carManager.newCustomer(xid, customerID);
            roomManager.newCustomer(xid, customerID);
        }
        return isSuccess;
    }

    @Override
    public boolean deleteCustomer(int xid, int customerID) throws RemoteException {
        Trace.info("Middleware::deleteCustomer(" + xid + ", " + customerID + ") called");

        Customer customer = (Customer) readData(xid, Customer.getKey(customerID));
        if (customer == null) {
            Trace.warn("Middleware::deleteCustomer(" + xid + ", " + customerID + ") failed--customer doesn't exist");
            return false;
        } else {
            RMHashMap reservations = customer.getReservations();
            for (String reservedKey : reservations.keySet()) {
                ReservedItem reserveditem = customer.getReservedItem(reservedKey);
                int amount = reserveditem.getCount();
                Trace.info("Middleware::deleteCustomer(" + xid + ", " + customerID + ") has reserved " + reserveditem.getKey() + " " + reserveditem.getCount() + " times");

                // Delegate to the appropriate ResourceManager based on the reservedKey
                if (reservedKey.startsWith("flight-")) {
                    flightManager.cancelReservation(xid, reservedKey, amount);
                    flightManager.deleteFlight(xid, Integer.parseInt(reservedKey.substring(7)));
                } else if (reservedKey.startsWith("car-")) {
                    carManager.cancelReservation(xid, reservedKey, amount);
                    carManager.deleteCars(xid, reservedKey.substring(4));
                } else if (reservedKey.startsWith("room-")) {
                    roomManager.cancelReservation(xid, reservedKey, amount);
                    roomManager.deleteRooms(xid, reservedKey.substring(5));
                }
            }
            removeData(xid, customer.getKey());
            Trace.info("Middleware::deleteCustomer(" + xid + ", " + customerID + ") succeeded");
            return true;
        }
    }


    @Override
    public String queryCustomerInfo(int xid, int customerID) throws RemoteException {
        Trace.info("Middleware::queryCustomerInfo(" + xid + ", " + customerID + ") called");
        Customer customer = (Customer) readData(xid, Customer.getKey(customerID));
        if (customer == null) {
            Trace.warn("Middleware::queryCustomerInfo(" + xid + ", " + customerID + ") failed--customer doesn't exist");
            // NOTE: don't change this--WC counts on this value indicating a customer does not exist...
            return "";
        } else {
            Trace.info("Middleware::queryCustomerInfo(" + xid + ", " + customerID + ")");
            System.out.println(customer.getBill());

            System.out.println(m_data.toString());
            String result = customer.getBill();
            System.out.println(result);
            return result;
        }
    }


    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        synchronized (flightManager) {
            return flightManager.deleteFlight(id, flightNum);
        }
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException {
        synchronized (carManager) {
            return carManager.deleteCars(id, location);
        }
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException {
        synchronized (roomManager) {
            return roomManager.deleteRooms(id, location);
        }
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException {
        return flightManager.queryFlight(id, flightNumber);
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException {
        return carManager.queryCars(id, location);
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException {
        return roomManager.queryRooms(id, location);

    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        return flightManager.queryFlightPrice(id, flightNumber);
    }

    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException {
        return carManager.queryCarsPrice(id, location);
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException {
        return roomManager.queryRoomsPrice(id, location);
    }

    @Override
    public boolean reserveFlight(int xid, int customerID, int flightNumber) throws RemoteException {
        String key = Flight.getKey(flightNumber);

        Trace.info("Middleware::reserveItem(" + xid + ", customer=" + customerID + ", " + key + ", " + flightNumber + ") called");
        // Read customer object if it exists (and read lock it)
        Customer customer = (Customer) readData(xid, Customer.getKey(customerID));

        if (customer == null) {
            Trace.warn("Middleware::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + flightNumber + ")  failed--customer doesn't exist");
            return false;
        }

        synchronized (customer) {
            synchronized (flightManager) {
//                System.out.println("Middleware is sending a query to roomRM with key: " + key);
                int checkAvail = flightManager.checkForAvail(xid, key);

                if (checkAvail == -1) {
                    Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + flightNumber + ") failed--item is not available");
                } else {
//                    System.out.println("pass the condition");
                    if (flightManager.reserveFlight(xid, customerID, flightNumber)) {
                        int price = checkAvail;
                        customer.reserve(key, flightNumber + "", price);
                        writeData(xid, customer.getKey(), customer);
                        Trace.info("Middleware::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + flightNumber + ") succeeded");
                        return true;
                    }

                    return false;
                }
            }
        }
        return false;


    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
        String key = Car.getKey(location);

        Trace.info("Middleware::reserveItem(" + id + ", customer=" + customerID + ", " + key + ", " + location + ") called");
        // Read customer object if it exists (and read lock it)
        Customer customer = (Customer) readData(id, Customer.getKey(customerID));

        if (customer == null) {
            Trace.warn("Middleware::reserveItem(" + id + ", " + customerID + ", " + key + ", " + location + ")  failed--customer doesn't exist");
            return false;
        }

        synchronized (customer) {
            synchronized (carManager) {
                int checkAvail = carManager.checkForAvail(id, key);

                if (checkAvail == -1) {
                    Trace.warn("RM::reserveItem(" + id + ", " + customerID + ", " + key + ", " + location + ") failed--item is not available");
                } else {
                    if (carManager.reserveCar(id, customerID, location)) {
                        int price = checkAvail;
                        customer.reserve(key, location, price);
                        writeData(id, customer.getKey(), customer);
                        Trace.info("Middleware::reserveItem(" + id + ", " + customerID + ", " + key + ", " + location + ") succeeded");
                        return true;
                    }

                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean reserveRoom(int xid, int customerID, String location) throws RemoteException {
        String key = Room.getKey(location);

        Trace.info("Middleware::reserveItem(" + xid + ", customer=" + customerID + ", " + key + ", " + location + ") called");
        // Read customer object if it exists (and read lock it)
        Customer customer = (Customer) readData(xid, Customer.getKey(customerID));

        if (customer == null) {
            Trace.warn("Middleware::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ")  failed--customer doesn't exist");
            return false;
        }

        synchronized (customer) {
            synchronized (roomManager) {
                int checkAvail = roomManager.checkForAvail(xid, key);

                if (checkAvail == -1) {
                    Trace.warn("RM::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ") failed--item is not available");
                } else {
                    System.out.println("pass the condition");
                    if (roomManager.reserveRoom(xid, customerID, location)) {
                        int price = checkAvail;
                        customer.reserve(key, location, price);
                        writeData(xid, customer.getKey(), customer);
                        Trace.info("Middleware::reserveItem(" + xid + ", " + customerID + ", " + key + ", " + location + ") succeeded");
                        return true;
                    }

                    return false;
                }
            }
        }
        return false;

    }

    @Override
    public boolean bundle(int xid, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {
        Trace.info("Middleware::bundle(" + xid + ", " + customerID + ", " + flightNumbers + ", " + location + ", " + car + ", " + room + ") called");

        Customer customer = (Customer) readData(xid, Customer.getKey(customerID));

        if (customer == null) {
            Trace.warn("Middleware::bundle(" + xid + ", " + Customer.getKey(customerID) + ")  failed--customer doesn't exist");
            return false;
        }

        boolean success = true;
        boolean check = true;

        HashMap<String, Integer> avail_flight = new HashMap<>();
        HashSet<String> not_avail_flight = new HashSet<>();
        for (String flightNumber : flightNumbers) {
            if (!avail_flight.containsKey(flightNumber)) {
                avail_flight.put(flightNumber, this.queryFlight(xid, Integer.parseInt(flightNumber)));
            }
        }
        for (String flightNumber : flightNumbers) {
            int new_count = avail_flight.get(flightNumber) - 1;
            avail_flight.put(flightNumber, new_count);
            if (new_count < 0) {
                not_avail_flight.add(flightNumber);
                check = false;
                Trace.info("Middleware::bundle(" + xid + ", " + flightNumber + ") failed--flight full");
            }
        }

        if (this.queryRooms(xid, location) == 0) {
            check = false;
            Trace.info("Middleware::bundle(" + xid + ", " + location + ") failed--no more room avail");
        }

        if (this.queryCars(xid, location) == 0) {
            check = false;
            Trace.info("Middleware::bundle(" + xid + ", " + location + ") failed--no more car avail");
        }

        if (check == false) {
            Trace.warn("Middleware::bundle(" + xid + ", " + customerID + ", " + flightNumbers + ", " + location + ", " + car + ", " + room + ") failed--not enough resource");
            return false;
        }

        synchronized (flightManager) {
            // Reserve all flights
            for (String flightNumber : flightNumbers) {
                success &= this.reserveFlight(xid, customerID, Integer.parseInt(flightNumber));
            }

            synchronized (carManager) {
                // Reserve car if requested
                if (car) {
                    success &= this.reserveCar(xid, customerID, location);
                }
            }

            synchronized (roomManager) {
                // Reserve room if requested
                if (room) {
                    success &= this.reserveRoom(xid, customerID, location);
                }
            }
        }
        if (!success) {
            Trace.warn("Middleware::bundle(" + xid + ", " + customerID + ", " + flightNumbers + ", " + location + ", " + car + ", " + room + ") failed");
        } else {
            Trace.info("Middleware::bundle(" + xid + ", " + customerID + ", " + flightNumbers + ", " + location + ", " + car + ", " + room + ") succeeded");
        }

        return success;
    }


    @Override
    public String getName() throws RemoteException {
        return "Middleware";
    }


}
