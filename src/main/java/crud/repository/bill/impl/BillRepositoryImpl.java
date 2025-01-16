package crud.repository.bill.impl;

import crud.base.AbstractRepository;
import crud.exception.DAOException;
import crud.infrastructure.ConnectionFactory;
import crud.model.entities.Bill;
import crud.repository.bill.BillRepository;
import crud.util.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class BillRepositoryImpl extends AbstractRepository implements BillRepository {

    private final Connection connection;

    private static final String tableName = "bills";

    public BillRepositoryImpl(Connection connection) {
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

        String query = "INSERT INTO bills (id, supplier_id, retailer_id, product_id, amount, current_price," +
                " date, created_at, created_by, status) VALUES (?,?,?,?,?,?,?,?,?,'PENDING')";
        try{
            UUID id = UUID.randomUUID();
            ps = connection.prepareStatement(query);
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
        String query = "UPDATE bills SET amount = ? current_price = ?, date = ?, updated_at = ?," +
                " updated_by = ? WHERE id = ? And deleted = false";

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(query);

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
        String query;

        try{
            connection.setAutoCommit(false);
            query = softDeleteQuery(tableName,bill.getId().toString());

            ps = connection.prepareStatement(query);

            ps.setTimestamp(1, bill.getDeletedAt());
            ps.setString(2,bill.getDeletedBy());

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
        String query;

        try{
            connection.setAutoCommit(false);

            query = hardDeleteQuery(tableName, bill.getId().toString());

            ps = connection.prepareStatement(query);

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
        String query;

        try{
            connection.setAutoCommit(false);
            HashMap<String, String> columns = new HashMap<>();
            columns.put("id", id.toString());
            columns.put("deleted","false");

            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);
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
        String query;

        try{
            connection.setAutoCommit(false);

            HashMap<String, String> columns = new HashMap<>();
            columns.put("retailer_id", id.toString());
            columns.put("deleted","false");

            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by Retailer Id: " + ex.getMessage(), ex);
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
        String query;
        try{
            connection.setAutoCommit(false);

            HashMap<String, String> columns = new HashMap<>();
            columns.put("supplier_id", id.toString());
            columns.put("deleted","false");

            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by Supplier Id: " + ex.getMessage(), ex);
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
        String query;

        try{
            connection.setAutoCommit(false);
            HashMap<String, String> columns = new HashMap<>();
            columns.put("product_id", id.toString());
            columns.put("deleted","false");

            query = findByColumnsQuery(tableName, columns);

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()){
                bills.add(getBill(rs));
            }
            connection.commit();

        } catch (SQLException ex) {
            throw new DAOException("Error while getting the bill by Product Id: " + ex.getMessage(), ex);
        } finally {
            ConnectionFactory.closeAll(connection,ps,rs);
        }
        return bills;
    }

    @Override
    public ArrayList<Bill> findAll() throws DAOException {

        ArrayList<Bill> bills = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = findAllQuery(tableName);

        try{
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(query);
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

        Bill bill = new Bill();

        bill.setId(UUID.fromString(rs.getString("id")));
        bill.setSupplierId(UUID.fromString(rs.getString("supplier_id")));
        bill.setRetailerId(UUID.fromString(rs.getString("retailer_id")));
        bill.setProductId(UUID.fromString(rs.getString("product_id")));
        bill.setCurrentPrice(rs.getDouble("current_price"));
        bill.setDate(rs.getTimestamp("date"));
        bill.setAmount(rs.getLong("amount"));

        return bill;
    }
}
