package crud.service.products.commands;

import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import jakarta.servlet.http.HttpServletRequest;

public class UpdateProductCommand extends AbstractCommand {

    public UpdateProductCommand(ProductRepository repository, BaseMapper<Product> mapper, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = PAGE_PRODUCT_LIST;
    private ProductRepository repository;
    private ProductValidator validator ;
    private BaseMapper<Product> mapper;

    @Override
    public String execute(HttpServletRequest request) {

        try{
            validator.validateUpdateRequest(request);

            Product Product = mapper.buildEntity(request);

            repository.update(Product);

            addSuccessMessage(request,UPDATE_SUCCESS_MESSAGE);

        } catch(DAOException | MappingException ex){

            //logger.error(ex.getMessage());
            page = PAGE_PRODUCT_FORM;

            setException(request, ex);
        }
        return page;
    }

}