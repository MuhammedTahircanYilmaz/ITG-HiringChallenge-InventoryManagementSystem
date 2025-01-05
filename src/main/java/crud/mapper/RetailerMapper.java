package crud.mapper;

import crud.base.BaseMapper;
import crud.exception.MappingException;
import crud.model.entities.Retailer;
import crud.service.retailers.commands.ChangePasswordRetailerCommand;
import jakarta.servlet.http.HttpServletRequest;
import crud.dtos.retailers.requests.AddRetailerCommandDto;
import crud.dtos.retailers.requests.DeleteRetailerCommandDto;
import crud.dtos.retailers.requests.GetByIdRetailerQueryDto;
import crud.dtos.retailers.requests.UpdateRetailerCommandDto;
import crud.dtos.retailers.responses.RetailerResponseDto;

import java.util.UUID;

public class RetailerMapper implements BaseMapper<Retailer, AddRetailerCommandDto, UpdateRetailerCommandDto,
        DeleteRetailerCommandDto, GetByIdRetailerQueryDto, RetailerResponseDto> {

    private static final String MAPPER_ERROR_MESSAGE = "Error mapping request data.";

    protected RetailerMapper() {}

    @Override
    public AddRetailerCommandDto mapAddRequestDto(HttpServletRequest request, String userId) throws MappingException {
        AddRetailerCommandDto dto = new AddRetailerCommandDto();

        try {

            dto.setName(request.getParameter("name"));
            dto.setEmail(request.getParameter("email"));
            dto.setPassword(request.getParameter("password"));
            dto.setCreatedBy(userId);
            dto.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public UpdateRetailerCommandDto mapUpdateRequestDto(HttpServletRequest request, String userId) throws MappingException {
        UpdateRetailerCommandDto dto = new UpdateRetailerCommandDto();

        try {
            dto.setId(UUID.fromString(request.getParameter("id")));
            dto.setName(request.getParameter("name"));
            dto.setImageLocation(request.getParameter("imageLocation"));
            dto.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            dto.setUpdatedBy(userId);
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public DeleteRetailerCommandDto mapDeleteRequestDto(HttpServletRequest request, String userId) throws MappingException {
        DeleteRetailerCommandDto dto = new DeleteRetailerCommandDto();

        try {
            UUID RetailerId = UUID.fromString(request.getParameter("id"));
            dto.setId(RetailerId);
            dto.setDeletedBy(userId);
            dto.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public GetByIdRetailerQueryDto mapGetByIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByIdRetailerQueryDto dto = new GetByIdRetailerQueryDto();

        try {
            UUID RetailerId = UUID.fromString(request.getParameter("id"));
            dto.setId(RetailerId);
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public Retailer mapAddEntityDtoToEntity(AddRetailerCommandDto addRetailerCommandDto) throws MappingException {
        Retailer Retailer = new Retailer();

        try {
            Retailer.setName(addRetailerCommandDto.getName());
            Retailer.setEmail(addRetailerCommandDto.getEmail());
            Retailer.setPassword(addRetailerCommandDto.getPassword());
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return Retailer;
    }

    @Override
    public RetailerResponseDto mapEntityToEntityResponseDto(Retailer entity) throws MappingException {
        RetailerResponseDto dto = new RetailerResponseDto();

        try {
            dto.setName(entity.getName());
            dto.setEmail(entity.getEmail());
            dto.setImageLocation(entity.getImageLocation());
            dto.setRoleName(entity.getRoleName());
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }
}