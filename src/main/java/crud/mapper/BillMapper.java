package crud.mapper;

import crud.base.BaseMapper;
import crud.dtos.bills.requests.*;
import crud.dtos.bills.responses.BillResponseDto;
import crud.exception.DAOException;
import crud.exception.MappingException;
import crud.model.entities.Bill;
import crud.model.entities.Product;
import crud.model.entities.Retailer;
import crud.model.entities.Supplier;
import crud.model.enums.BillStatus;
import crud.repository.ProductRepository;
import crud.repository.RetailerRepository;
import crud.repository.SupplierRepository;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class BillMapper implements BaseMapper <Bill, AddBillCommandDto, UpdateBillCommandDto, DeleteBillCommandDto, GetByIdBillQueryDto, BillResponseDto>{

    private ProductRepository repository;
    private RetailerRepository retailerRepository;
    private SupplierRepository supplierRepository;

    public BillMapper(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public AddBillCommandDto mapAddRequestDto(HttpServletRequest request, String userId) throws MappingException {

        AddBillCommandDto dto = new AddBillCommandDto();

        try{
            UUID productId = UUID.fromString(request.getParameter("productId"));
            Product product = repository.findById(productId);
            LocalDateTime now = LocalDateTime.now();
            Timestamp sqlTimestamp = Timestamp.valueOf(now);


            dto.setSupplierId(product.getSupplierId());
            dto.setProductId(productId);
            dto.setAmount(Long.parseLong(request.getParameter("amount")));
            dto.setCurrentPrice( product.getPrice()*(100-product.getDiscount())/100* dto.getAmount());
            dto.setRetailerId(UUID.fromString(userId));
            dto.setCreatedBy(userId);
            dto.setDate(sqlTimestamp);
            dto.setCreatedAt(sqlTimestamp);
            dto.setStatus(BillStatus.valueOf(request.getParameter("status")));

        } catch (DAOException ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
        return dto;
    }

    @Override
    public UpdateBillCommandDto mapUpdateRequestDto(HttpServletRequest request, String userId) throws MappingException {
        UpdateBillCommandDto dto = new UpdateBillCommandDto();
        try{
            UUID productId = UUID.fromString(request.getParameter("productId"));
            Product product = repository.findById(productId);
            LocalDateTime now = LocalDateTime.now();
            Timestamp sqlTimestamp = Timestamp.valueOf(now);

            dto.setProductId(UUID.fromString(request.getParameter("productId")));
            dto.setAmount(Long.parseLong(request.getParameter("amount")));
            dto.setCurrentPrice( product.getPrice()*(100-product.getDiscount())/100*dto.getAmount());
            dto.setUpdatedAt(sqlTimestamp);
            dto.setUpdatedBy(userId);

            return dto;
        } catch (DAOException ex) {
        throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    @Override
    public DeleteBillCommandDto mapDeleteRequestDto(HttpServletRequest request, String userId) throws MappingException {
        DeleteBillCommandDto dto = new DeleteBillCommandDto();
        try {
            UUID billId = UUID.fromString(request.getParameter("billId"));
            dto.setId(billId);
            dto.setDeletedBy(userId);
            return dto;
        } catch (Exception ex) {
        throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    @Override
    public GetByIdBillQueryDto mapGetByIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByIdBillQueryDto dto = new GetByIdBillQueryDto();
        try {
            UUID billId = UUID.fromString(request.getParameter("billId"));
            dto.setId(billId);
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    public GetBySupplierIdBillQueryDto mapGetBySupplierIdRequestDto(HttpServletRequest request) throws MappingException {
        GetBySupplierIdBillQueryDto dto = new GetBySupplierIdBillQueryDto();
        try {
            UUID supplierId = UUID.fromString(request.getParameter("supplierId"));
            dto.setId(supplierId);
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    public GetByRetailerIdBillQueryDto mapGetByRetailerIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByRetailerIdBillQueryDto dto = new GetByRetailerIdBillQueryDto();
        try {
            UUID retailerId = UUID.fromString(request.getParameter("retailerId"));
            dto.setId(retailerId);
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    public GetByProductIdBillQueryDto mapGetByProductIdRequestDto(HttpServletRequest request) throws MappingException {
        GetByProductIdBillQueryDto dto = new GetByProductIdBillQueryDto();
        try {
            UUID productId = UUID.fromString(request.getParameter("productId"));
            dto.setId(productId);
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }

    @Override
    public Bill mapAddEntityDtoToEntity(AddBillCommandDto addBillCommandDto) throws MappingException {
        Bill bill = new Bill();

        bill.setProductId(addBillCommandDto.getProductId());
        bill.setAmount(addBillCommandDto.getAmount());
        bill.setCurrentPrice(addBillCommandDto.getCurrentPrice());
        bill.setDate(addBillCommandDto.getDate());
        bill.setRetailerId(addBillCommandDto.getRetailerId());
        bill.setSupplierId(addBillCommandDto.getSupplierId());
        bill.setCreatedBy(addBillCommandDto.getCreatedBy());
        bill.setCreatedAt(addBillCommandDto.getCreatedAt());
        bill.setStatus(addBillCommandDto.getStatus());

        return bill;
    }

    @Override
    public BillResponseDto mapEntityToEntityResponseDto(Bill entity) throws MappingException {
        try{
            BillResponseDto dto = new BillResponseDto();
            LocalDateTime date = entity.getDate().toLocalDateTime();
            Supplier supplier = supplierRepository.findById(entity.getSupplierId());
            Retailer retailer = retailerRepository.findById(entity.getRetailerId());
            Product product = repository.findById(entity.getProductId());

            dto.setDate(date);
            dto.setQuantity(entity.getAmount());
            dto.setTotalPrice(entity.getCurrentPrice());
            dto.setProductName(product.getName());
            dto.setRetailerName(retailer.getName());
            dto.setSupplierName(supplier.getName());
            return dto;
        } catch (Exception ex) {
            throw new MappingException(MAPPER_ERROR_MESSAGE, ex);
        }
    }


}
