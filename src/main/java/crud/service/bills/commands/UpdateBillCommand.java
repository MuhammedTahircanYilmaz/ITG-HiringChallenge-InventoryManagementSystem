package crud.service.bills.commands;

import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.dtos.bills.requests.AddBillCommandDto;
import crud.dtos.bills.requests.UpdateBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class UpdateBillCommand  extends AbstractCommand {

    private String page = PAGE_BILL_LIST;
    private BillRepository repository;
    private ProductRepository productRepository;
    private BillValidator validator;
    private BillMapper mapper;

    public UpdateBillCommand(BillRepository repository, BillMapper mapper , ProductRepository productRepository , BillValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
        this.productRepository = productRepository;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {

            validator.validateUpdateRequest(request);

            UpdateBillCommandDto dto = mapper.mapUpdateRequestDto(request);
            Bill bill = repository.findById(dto.getId());
            bill.
            double currentPrice = calculateCurrentPrice(request);
            dto.setCurrentPrice(currentPrice);

            repository.update(dto);
            addSuccessMessage(request, UPDATE_SUCCESS_MESSAGE);

        } catch (DAOException | MappingException ex) {

            //logger.error(ex.getMessage());
            page = PAGE_BILL_FORM;

            setException(request, ex);
        }
        return page;
    }

    private double calculateCurrentPrice(HttpServletRequest request) {
        double currentPrice = 0;
        try {
            Product product = productRepository.findById(UUID.fromString(request.getParameter("productId")));

           currentPrice = product.getPrice() * (100 - product.getDiscount()) / 100;

        } catch (DAOException ex){
            //logger.error(ex.getMessage());
            page = PAGE_BILL_FORM;

            setException(request, ex);
        }
        return currentPrice;
    }
}
