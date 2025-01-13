package crud.repository.bill;

import crud.base.AbstractRepository;
import crud.base.BaseRepository;
import crud.exception.DAOException;
import crud.model.entities.Bill;

import java.util.ArrayList;
import java.util.UUID;

public interface BillRepository extends BaseRepository<Bill> {

    public void deleteRejected(Bill bill) throws DAOException;
    public ArrayList<Bill> findByRetailerId(UUID id) throws DAOException;
    public ArrayList<Bill> findBySupplierId(UUID id) throws DAOException;
    public ArrayList<Bill> findByProductId(UUID id) throws DAOException;

}
