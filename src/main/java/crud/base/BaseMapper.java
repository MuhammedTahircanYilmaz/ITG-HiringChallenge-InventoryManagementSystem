package crud.base;

import crud.exception.MappingException;
import jakarta.servlet.http.HttpServletRequest;

public interface BaseMapper <
        TEntity extends BaseEntity, TAddEntityDto, TUpdateEntityDto,
        TDeleteEntityDto, TGetByIdEntityDto, TEntityResponseDto> {

    public static final String MAPPER_ERROR_MESSAGE = "An error occured while mapping the object";

    public TAddEntityDto mapAddRequestDto(HttpServletRequest request, String userId) throws MappingException;
    public TUpdateEntityDto mapUpdateRequestDto(HttpServletRequest request, String userId) throws MappingException;
    public TDeleteEntityDto mapDeleteRequestDto(HttpServletRequest request, String userId) throws MappingException;
    public TGetByIdEntityDto mapGetByIdRequestDto(HttpServletRequest request) throws MappingException;

    public TEntity mapAddEntityDtoToEntity(TAddEntityDto addEntityDto) throws MappingException;

    public TEntityResponseDto mapEntityToEntityResponseDto(TEntity entity) throws MappingException;



}
