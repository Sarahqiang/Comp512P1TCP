package Server.ResourceManagers;

import Server.Common.ResourceManager;

import java.rmi.RemoteException;
import java.util.Vector;

public class FlightResourceManager extends ResourceManager {

    public FlightResourceManager(String p_name) {
        super(p_name);
    }

    // For non-flight methods, either leave them unimplemented or throw an UnsupportedOperationException
    @Override
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support car methods");
    }

    @Override
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support room methods");
    }

    @Override
    public int newCustomer(int id) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support customer methods");
    }

    @Override
    public boolean newCustomer(int id, int cid) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support customer methods");
    }

    @Override
    public boolean deleteCars(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support car methods");
    }

    @Override
    public boolean deleteRooms(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support room methods");
    }

    @Override
    public boolean deleteCustomer(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support customer methods");
    }

    @Override
    public int queryCars(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support car methods");
    }

    @Override
    public int queryRooms(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support room methods");
    }

    @Override
    public String queryCustomerInfo(int id, int customerID) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support customer methods");
    }


    @Override
    public int queryCarsPrice(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support car methods");
    }

    @Override
    public int queryRoomsPrice(int id, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support room methods");
    }

    @Override
    public boolean reserveCar(int id, int customerID, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support car methods");
    }

    @Override
    public boolean reserveRoom(int id, int customerID, String location) throws RemoteException {
        throw new UnsupportedOperationException("FlightResourceManager does not support room methods");
    }

    @Override
    public boolean bundle(int id, int customerID, Vector<String> flightNumbers, String location, boolean car, boolean room) throws RemoteException {
        return false;
    }

    @Override
    public String getName() throws RemoteException {
        return m_name;
    }
}
