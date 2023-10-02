package Server.ResourceManagers;

import Server.Common.ResourceManager;

import java.rmi.RemoteException;
import java.util.Vector;

public class RoomResourceManager extends ResourceManager {
    public RoomResourceManager(String p_name) {
        super(p_name);
    }

    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support flight methods");
    }

    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support car methods");
    }


    @Override
    public int newCustomer(int id) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support customer methods");
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support customer methods");
    }

    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support flight methods");
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support car methods");
    }


    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support customer methods");
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support flight methods");
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support car methods");
    }


    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support customer methods");
    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support flight methods");
    }

    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support car methods");
    }


    @Override
    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support flight methods");
    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
        throw new UnsupportedOperationException("RoomResourceManager does not support car methods");
    }

    @Override
    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {
        return false;
    }

    @Override
    public String getName() throws RemoteException {
        return null;
    }
}
