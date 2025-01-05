package crud.mapper;

import crud.base.BaseMapper;
import crud.dtos.products.requests.AddProductCommandDto;
import crud.dtos.products.requests.DeleteProductCommandDto;
import crud.dtos.products.requests.GetByIdProductQueryDto;
import crud.dtos.products.requests.UpdateProductCommandDto;
import crud.dtos.products.responses.ProductResponseDto;
import crud.exception.MappingException;
import crud.model.entities.Product;
import crud.model.entities.Supplier;
import crud.repository.SupplierRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class    ProductMapper implements BaseMapper<Product, AddProductCommandDto, UpdateProductCommandDto,
        DeleteProductCommandDto, GetByIdProductQueryDto, ProductResponseDto> {

    private SupplierRepository supplierRepository;

    protected ProductMapper( SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public AddProductCommandDto mapAddRequestDto(HttpServletRequest request, String userId) throws MappingException {

        AddProductCommandDto dto = new AddProductCommandDto();

        try {
            dto.setName(request.getParameter("name"));
            dto.setDescription( request.getParameter("description"));
            dto.setSupplierId(UUID.fromString(userId));
            dto.setCreatedBy(userId);
            dto.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            dto.setPrice(Double.parseDouble(request.getParameter("price")));
            dto.setImageLocation( request.getParameter("imageLocation"));
            dto.setStockQuantity(Long.parseLong( request.getParameter("stockQuantity")));
            dto.setDiscount(Float.parseFloat(request.getParameter("discount")));
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public UpdateProductCommandDto mapUpdateRequestDto(HttpServletRequest request, String userId) throws MappingException {
        UpdateProductCommandDto dto = new UpdateProductCommandDto();
        try {
            dto.setId(UUID.fromString(request.getParameter("Id")));
            dto.setName(request.getParameter("name"));
            dto.setDescription(request.getParameter("description"));
            dto.setStockQuantity(Long.parseLong(request.getParameter("stockQuantity")));
            dto.setUpdatedBy(userId);
            dto.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            dto.setPrice(Double.parseDouble(request.getParameter("price")));
            dto.setDiscount(Float.parseFloat(request.getParameter("discount")));
            dto.setImageLocation(request.getParameter("imageLocation"));

        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public DeleteProductCommandDto mapDeleteRequestDto(HttpServletRequest request, String userId) throws MappingException {
        DeleteProductCommandDto dto = new DeleteProductCommandDto();
        try {
            UUID productId = UUID.fromString(request.getParameter("Id"));
            dto.setId(productId);
            dto.setDeletedBy(userId);
            dto.setDeletedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    @Override
    public GetByIdProductQueryDto mapGetByIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByIdProductQueryDto dto = new GetByIdProductQueryDto();
        try {
            UUID productId = UUID.fromString(request.getParameter("Id"));
            dto.setId(productId);
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    @Override
    public Product mapAddEntityDtoToEntity(AddProductCommandDto addProductCommandDto) throws MappingException {
        Product product = new Product();
        try {

            product.setSupplierId(addProductCommandDto.getSupplierId());
            product.setName(addProductCommandDto.getName());
            product.setDescription(addProductCommandDto.getDescription());
            product.setStockQuantity(addProductCommandDto.getStockQuantity());
            product.setPrice(addProductCommandDto.getPrice());
            product.setDiscount(addProductCommandDto.getDiscount());
            product.setImageLocation(addProductCommandDto.getImageLocation());

        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return product;
    }

    @Override
    public ProductResponseDto mapEntityToEntityResponseDto(Product entity) throws MappingException {
        try {
            ProductResponseDto dto = new ProductResponseDto();
            Supplier supplier = supplierRepository.findById(entity.getSupplierId());

            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setStockQuantity(entity.getStockQuantity());
            dto.setSupplierName(supplier.getName());
            dto.setPrice(entity.getPrice());
            dto.setDiscount(entity.getDiscount());
            dto.setImageLocation(entity.getImageLocation());

            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }
}
