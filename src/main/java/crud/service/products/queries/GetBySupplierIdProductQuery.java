package crud.service.products.queries;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.bills.responses.BillResponseDto;
import crud.dtos.products.responses.ProductResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.ProductMapper;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.entities.Supplier;
import crud.repository.ProductRepository;
import crud.repository.SupplierRepository;
import crud.service.validation.ProductValidator;
import crud.util.Logger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.UUID;

public class GetBySupplierIdProductQuery extends AbstractCommand {

    private final ProductRepository repository;
    private final ProductValidator validator;
    private final ProductMapper mapper;
    private final AuthService authService;

    private String page = "";

    public GetBySupplierIdProductQuery(ProductRepository repository, ProductValidator validator, AuthService authService, ProductMapper mapper) {
        this.repository = repository;
        this.validator = validator;
        this.authService = authService;
        this.mapper = mapper;
    }

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try {
            String token = authService.extractToken(request);
            authService.isAuthenticated(token);

            UUID supplierId = UUID.fromString(request.getParameter("supplierId"));

            validator.validateGetBySupplierIdRequest(request);

            ArrayList<Product> products = repository.findBySupplierId(supplierId);

            ArrayList<ProductResponseDto> response = new ArrayList<ProductResponseDto>();
            for (Product product : products) {
                ProductResponseDto dto = mapper.mapEntityToEntityResponseDto(product);
                response.add(dto);
            }

            return createJsonResponse(response);


        } catch (DAOException | IllegalArgumentException | MappingException ex) {

            Logger.error(this.getClass().getName(), ex.getMessage());

            page = "";

            setException(request, ex);
        }
        return createView(page);
    }

}

