package crud.service.bills.rules;

import crud.base.BaseBusinessRules;
import crud.model.entities.Bill;
import crud.repository.BillRepository;

public class BillBusinessRules extends BaseBusinessRules<Bill, BillRepository> {

    public BillBusinessRules(BillRepository repository) {
        super(repository);
    }

    public boolean isAmountValid(long amount) {
        if(amount <= 0 )
            return false;
        return true;
    }

}
