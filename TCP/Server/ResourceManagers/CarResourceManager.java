package Server.ResourceManagers;

import Server.Common.ResourceManager;

import java.rmi.RemoteException;
import java.util.Vector;

public class CarResourceManager extends ResourceManager {
    public CarResourceManager(String p_name) {
        super(p_name);
    }

    @Override
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support flight methods");
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support room methods");
    }

    @Override
    public int newCustomer(int id) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support customer methods");
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support customer methods");
    }

    @Override
    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support flight methods");
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support room methods");
    }

    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support customer methods");
    }

    @Override
    public int queryFlight(int id, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support flight methods");
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support room methods");
    }

    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support customer methods");
    }

    @Override
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support flight methods");
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support room methods");
    }

    @Override
    public boolean reserveFlight(int id, int customerID, int flightNumber) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support flight methods");
    }

    @Override
    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
        throw new UnsupportedOperationException("CarResourceManager does not support room methods");
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
