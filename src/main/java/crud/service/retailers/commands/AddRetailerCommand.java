package crud.service.retailers.commands;

import crud.base.AbstractCommand;
import crud.base.ServiceResult;
import crud.dtos.retailers.requests.AddRetailerCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.retailer.RetailerRepositoryImpl;
import crud.service.validation.RetailerValidator;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AddRetailerCommand extends AbstractCommand {

    public AddRetailerCommand(RetailerRepositoryImpl repository, RetailerMapper mapper, RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = REGISTER;
    private RetailerRepositoryImpl repository;
    private RetailerValidator validator;
    private RetailerMapper mapper;

    @Override
    public ServiceResult execute(HttpServletRequest request) {
        try{

            validator.validateAddRequest(request);
            UUID userId = UUID.randomUUID();
            AddRetailerCommandDto dto = mapper.mapAddRequestDto(request, userId.toString());
            Retailer retailer = mapper.mapAddEntityDtoToEntity(dto);

            String hashedPassword = PasswordUtils.hashPassword(retailer.getPassword());
            retailer.setPassword(hashedPassword);

            repository.add(retailer);

            addSuccessMessage(request,ADD_SUCCESS_MESSAGE);

        } catch (DAOException| MappingException ex){
            Logger.error(this.getClass().getName(), ex.getMessage());

            page = REGISTER;
            setException(request, ex);
        }
        return createView( page);
    }
}
