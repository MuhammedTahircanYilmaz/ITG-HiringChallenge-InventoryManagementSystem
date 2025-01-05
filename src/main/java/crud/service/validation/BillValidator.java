package crud.service.validation;

import crud.base.BaseValidator;
import crud.exception.BusinessException;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.service.bills.rules.BillBusinessRules;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class BillValidator extends BaseValidator<Bill, BillBusinessRules, BillRepository> {

    private BillBusinessRules rules;

    public BillValidator(BillBusinessRules rules) {
        super(rules);
    }

    @Override
    public boolean validateAddRequest(HttpServletRequest request) {

        if(request.getParameter("productId") == null){
            throw new BusinessException("Product Id cannot be null or empty", new IllegalArgumentException());
        }

        long amount = Long.parseLong(request.getParameter("amount"));

        if(!rules.isAmountValid(amount)){
            throw new BusinessException("The Amount of products has to be over 0", new IllegalArgumentException());
        }

        return true;
    }

    @Override
    public boolean validateUpdateRequest(HttpServletRequest request ) {

        if(request.getParameter("productId") == null){
            throw new BusinessException("Product Id cannot be null or empty", new IllegalArgumentException());
        }

        long amount = Long.parseLong(request.getParameter("amount"));
        if(!rules.isAmountValid(amount)){
            throw new BusinessException("The Amount of products has to be over 0", new IllegalArgumentException());
        }
        return true;
    }

    public boolean validateGetByProductIdRequest(HttpServletRequest request) {
        UUID productId = UUID.fromString(request.getParameter("productId"));
        return productId != null;
    }

    public boolean validateGetBySupplierIdRequest(HttpServletRequest request) {
        UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
        return supplierId != null;
    }

    public boolean validateGetByRetailerIdRequest(HttpServletRequest request){
        UUID retailerId = UUID.fromString(request.getParameter("retailerId"));
        return retailerId != null;
    }

}
