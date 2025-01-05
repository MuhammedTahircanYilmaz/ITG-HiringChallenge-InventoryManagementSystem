package crud.service.products.commands;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteProductCommand extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private String page = PAGE_PRODUCT_LIST;

    public DeleteProductCommand(ProductRepository repository, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try{
            validator.validateDeleteRequest(request);

            repository.delete(getId(request));
            addSuccessMessage(request,DELETE_SUCCESS_MESSAGE);

        } catch (DAOException | ServletException ex){
            //logger.error(ex.getMessage);
            page = PAGE_PRODUCT_FORM;
            setException(request, ex);
        }
        return page;
    }
}