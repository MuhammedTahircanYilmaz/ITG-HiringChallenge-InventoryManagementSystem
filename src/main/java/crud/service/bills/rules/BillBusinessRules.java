package crud.service.bills.rules;

import crud.base.BaseBusinessRules;
import crud.model.entities.Bill;
import crud.repository.bill.impl.BillRepositoryImpl;

public class BillBusinessRules extends BaseBusinessRules<Bill, BillRepositoryImpl> {

    public BillBusinessRules(BillRepositoryImpl repository) {
        super(repository);
    }

    public boolean isAmountInvalid(long amount) {
        if(amount <= 0  || amount > 50000)
            return true;
        return false;
    }

}
