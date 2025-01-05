package crud.service.products.commands;

import crud.base.AbstractCommand;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Product;
import crud.repository.ProductRepository;
import crud.service.validation.ProductValidator;
import jakarta.servlet.http.HttpServletRequest;

public class AddProductCommand extends AbstractCommand {
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ProductValidator validator;

    private String page = PAGE_PRODUCT_FORM;

    public AddProductCommand(ProductRepository repository, ProductRepository productRepository, ProductMapper mapper, ProductValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            validator.validateAddRequest(request);

            Product product = mapper.buildEntity(request);

            repository.add(product);
            addSuccessMessage(request, ADD_SUCCESS_MESSAGE);

        } catch(MappingException | DAOException ex)
        {
            //logger.error(ex.getMessage(), e);
            page = PAGE_PRODUCT_FORM;
            setException(request, ex);
        }
        return page;
    }
}
