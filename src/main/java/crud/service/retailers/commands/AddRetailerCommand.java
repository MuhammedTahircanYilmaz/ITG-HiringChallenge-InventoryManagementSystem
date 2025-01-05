package crud.service.retailers.commands;

import crud.authorization.AuthService;
import crud.base.AbstractCommand;
import crud.base.BaseMapper;
import crud.dtos.retailers.requests.AddRetailerCommandDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.mapper.RetailerMapper;
import crud.model.entities.Retailer;
import crud.repository.RetailerRepository;
import crud.service.validation.RetailerValidator;
import crud.util.Logger;
import crud.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class AddRetailerCommand extends AbstractCommand {

    public AddRetailerCommand(RetailerRepository repository, RetailerMapper mapper, RetailerValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.mapper = mapper;
    }

    private String page = PAGE_RETAILER_LIST;
    private RetailerRepository repository;
    private RetailerValidator validator;
    private RetailerMapper mapper;

    @Override
    public String execute(HttpServletRequest request) {
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

            page = PAGE_RETAILER_FORM;
            setException(request, ex);
        }
        return page;
    }
}
