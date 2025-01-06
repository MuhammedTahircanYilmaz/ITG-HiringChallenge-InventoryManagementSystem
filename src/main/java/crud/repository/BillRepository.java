package crud.repository;

import crud.base.BaseRepository;
import crud.dtos.bills.requests.DeleteBillCommandDto;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Bill;
import crud.util.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class BillRepository implements BaseRepository<Bill> {

    private Connection connection;

    private static final String SQL_INSERT = "INSERT INTO Bills (Id, SupplierId, RetailerId, ProductId, Amount, CurrrentPrice," +
            " Date, CreatedAt, CreatedBy, Status) VALUES (?,?,?,?,?,?,?,?,?,'PENDING')";
    private static final String SQL_UPDATE = "UPDATE Bills SET SupplierId = ?, RetailerId = ?, ProductId = ?, Amount = ? , CurrentPrice = ?, Date = ?, UpdatedAt = ?, UpdatedBy = ?, Status = ? WHERE Id = ?";
    private static final String SQL_DELETE = "UPDATE Bills SET deleted = true, deleted_at = ?, deleted_by = ? WHERE id = ? AND deleted = false";
    private static final String SQL_DELETE_REJECTED = "DELETE from Bills WHERE Id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM Bills WHERE Deleted = false";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM Bills WHERE Id = ? AND Deleted = false";
    private static final String SQL_FIND_BY_SUPPLIER_ID = "SELECT * FROM Bills WHERE SupplierId = ? AND Deleted = false";
    private static final String SQL_FIND_BY_RETAILER_ID = "SELECT * FROM Bills WHERE RetailerId = ? AND Deleted = false";
    private static final String SQL_FIND_BY_PRODUCT_ID = "SELECT * FROM Bills WHERE ProductId = ? AND Deleted = false";


    public BillRepository(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.connection = connection;
    }

    @Override
    public Bill add(Bill entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Bill Entity cannot be null");
        }

        PreparedStatement ps = null;

        try{
            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(SQL_INSERT);
            connection.setAutoCommit(false);

            ps.setString(1, id.toString());
            ps.setString(2, entity.getSupplierId().toString());
            ps.setString(3, entity.getRetailerId().toString());
            ps.setString(4, entity.getProductId().toString());
            ps.setLong(5, entity.getAmount());
            ps.setDouble(6, entity.getCurrentPrice());
            ps.setTimestamp(7, entity.getDate());
            ps.setTimestamp(8, entity.getCreatedAt ());
            ps.setString(9, entity.getCreatedBy());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while adding the bill" + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection,ps);
        }
        return entity;
    }

    @Override
    public Bill update(Bill entity) throws DAOException {
        if (entity == null) {
            throw new DAOException("Bill Entity cannot be null");
        }

        PreparedStatement ps = null;
        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_UPDATE);

            ps.setString(1, entity.getSupplierId().toString());
            ps.setString(2, entity.getRetailerId().toString());
            ps.setString(3, entity.getProductId().toString());
            ps.setLong( 4, entity.getAmount());
            ps.setDouble(5,entity.getCurrentPrice());
            ps.setTimestamp(6,entity.getDate());
            ps.setTimestamp(7, entity.getUpdatedAt());
            ps.setString(8, entity.getUpdatedBy());
            ps.setString(9, entity.getStatus().toString());
            ps.setString(10, entity.getId().toString());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while updating the bill" + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection,ps);
        }
        return entity;
    }

    @Override
    public void delete(Bill bill) throws DAOException {
        if ( bill.getId() == null){
            throw new DAOException("Bill Id cannot be null");
        }

        PreparedStatement ps = null;
        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_DELETE);
            ps.setTimestamp(1, bill.getDeletedAt());
            ps.setString(2,bill.getDeletedBy());
            ps.setString(3,bill.getId().toString());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new DAOException("No Bill found with the ID: " + bill.getId());
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while deleting the bill" + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeConnectionAndStatement(connection,ps);
        }
    }
    public void deleteRejected(Bill bill) throws DAOException {
        if ( bill.getId() == null){
            throw new DAOException("Bill Id cannot be null");
        }
        PreparedStatement ps = null;
        try{

            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_DELETE);
            ps.setString(1, bill.getId().toString());

            ps.executeUpdate();
            connection.commit();

        } catch (SQLException ex) {
            Logger.error(this.getClass().getName(), ex.getMessage());
        }
    }

    @Override
    public Bill findById(UUID id) throws DAOException {
        if(id == null) {
            throw new DAOException("Bill Id cannot be null");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Bill bill = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_ID);
            ps.setString(1,id.toString());
            rs = ps.executeQuery();

            if(rs.next()){
                bill = getBill(rs);
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by Id: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bill;
    }

    public ArrayList<Bill> findByRetailerId(UUID id) throws DAOException {
        if(id == null){
            throw new DAOException("The Retailer Id cannot be null");
        }

        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_RETAILER_ID);
            ps.setString(1,id.toString());
            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by RetailerId: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bills;
    }
    public ArrayList<Bill> findBySupplierId(UUID id) throws DAOException {
        if(id == null){
            throw new DAOException("The Supplier Id cannot be null");
        }

        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_SUPPLIER_ID);
            ps.setString(1,id.toString());
            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by SupplierId: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bills;
    }

    public ArrayList<Bill> findByProductId(UUID id) throws DAOException {
        if(id == null){
            throw new DAOException("The Product Id cannot be null");
        }

        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_BY_PRODUCT_ID);
            ps.setString(1,id.toString());
            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by ProductId: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bills;
    }

    @Override
    public ArrayList<Bill> getAll() throws DAOException {

        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(SQL_FIND_ALL);
            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bills: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bills;
    }

    public Bill getBill(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet cannot be null");
        }

        LocalDateTime now = LocalDateTime.now();
        Timestamp sqlTimestamp = Timestamp.valueOf(now);
        Bill bill = new Bill();

        bill.setId(UUID.fromString(rs.getString("Id")));
        bill.setSupplierId(UUID.fromString(rs.getString("SupplierId")));
        bill.setRetailerId(UUID.fromString(rs.getString("RetailerId")));
        bill.setProductId(UUID.fromString(rs.getString("ProductId")));
        bill.setCurrentPrice(rs.getDouble("CurrentPrice"));
        bill.setDate(sqlTimestamp);
        bill.setAmount(rs.getLong("Amount"));

        return bill;
    }
}
