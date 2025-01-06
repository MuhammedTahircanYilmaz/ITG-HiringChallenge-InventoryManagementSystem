package crud.mapper;

import crud.base.BaseMapper;
import crud.exception.MappingException;
import crud.model.entities.Supplier;
import jakarta.servlet.http.HttpServletRequest;
import crud.dtos.suppliers.requests.AddSupplierCommandDto;
import crud.dtos.suppliers.requests.DeleteSupplierCommandDto;
import crud.dtos.suppliers.requests.GetByIdSupplierQueryDto;
import crud.dtos.suppliers.requests.UpdateSupplierCommandDto;
import crud.dtos.suppliers.responses.SupplierResponseDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierMapper implements BaseMapper<Supplier, AddSupplierCommandDto, UpdateSupplierCommandDto,
        DeleteSupplierCommandDto, GetByIdSupplierQueryDto, SupplierResponseDto> {

    private static final String MAPPER_ERROR_MESSAGE = "Error mapping request data.";


    @Override
    public AddSupplierCommandDto mapAddRequestDto(HttpServletRequest request, String userId) throws MappingException {
        AddSupplierCommandDto dto = new AddSupplierCommandDto();

        try {
            dto.setName(request.getParameter("name"));
            dto.setEmail(request.getParameter("email"));
            dto.setPassword(request.getParameter("password"));
            dto.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            dto.setCreatedBy(userId);

        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public UpdateSupplierCommandDto mapUpdateRequestDto(HttpServletRequest request, String userId) throws MappingException {
        UpdateSupplierCommandDto dto = new UpdateSupplierCommandDto();

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
    public DeleteSupplierCommandDto mapDeleteRequestDto(HttpServletRequest request, String userId) throws MappingException {
        DeleteSupplierCommandDto dto = new DeleteSupplierCommandDto();

        try {
            UUID supplierId = UUID.fromString(request.getParameter("id"));
            dto.setId(supplierId);
            dto.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            dto.setDeletedBy(userId);

        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public GetByIdSupplierQueryDto mapGetByIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByIdSupplierQueryDto dto = new GetByIdSupplierQueryDto();

        try {
            UUID supplierId = UUID.fromString(request.getParameter("id"));
            dto.setId(supplierId);
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public Supplier mapAddEntityDtoToEntity(AddSupplierCommandDto addSupplierCommandDto) throws MappingException {
        Supplier supplier = new Supplier();

        try {
            supplier.setName(addSupplierCommandDto.getName());
            supplier.setEmail(addSupplierCommandDto.getEmail());
            supplier.setPassword(addSupplierCommandDto.getPassword());
            supplier.setCreatedBy(addSupplierCommandDto.getEmail());
            supplier.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return supplier;
    }

    @Override
    public SupplierResponseDto mapEntityToEntityResponseDto(Supplier entity) throws MappingException {
        SupplierResponseDto dto = new SupplierResponseDto();

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