package crud.service.bills.commands;

import crud.base.AbstractCommand;
import crud.dtos.bills.requests.AddBillCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.BillMapper;
import crud.model.entities.Bill;
import crud.repository.BillRepository;
import crud.repository.ProductRepository;
import crud.service.validation.BillValidator;
import jakarta.servlet.http.HttpServletRequest;


public class AddBillCommand extends AbstractCommand {

    private final BillRepository repository;
    private final BillMapper mapper;
    private final ProductRepository productRepository;
    private final BillValidator validator;
    private String page = PAGE_BILL_FORM;

    public AddBillCommand(BillRepository repository, ProductRepository productRepository, BillMapper mapper, BillValidator validator) {
        this.repository = repository;
        this.mapper = mapper;
        this.productRepository = productRepository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            AddBillCommandDto dto = mapper.mapAddRequestDto(request);
            Bill bill = mapper.mapAddEntityDtoToEntity(dto);
            repository.add(bill);

            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);
        } catch(MappingException| DAOException ex)
        {
            //logger.error(ex.getMessage(), e);
            page = PAGE_BILL_FORM;
            setException(request, ex);
        }
        return page;

    }
}

