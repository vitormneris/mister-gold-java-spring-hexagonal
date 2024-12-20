package com.mistergold.mistergold.adapters.persistence.services.product;

import com.mistergold.mistergold.adapters.persistence.entities.category.CategoryEntity;
import com.mistergold.mistergold.adapters.persistence.entities.product.ProductEntity;
import com.mistergold.mistergold.adapters.persistence.mappers.ProductPersistenceMapper;
import com.mistergold.mistergold.adapters.persistence.repositories.CategoryRepository;
import com.mistergold.mistergold.adapters.persistence.repositories.ProductRepository;
import com.mistergold.mistergold.application.domain.product.Product;
import com.mistergold.mistergold.application.ports.out.product.DeleteProductPort;
import com.mistergold.mistergold.configuration.web.advice.exception.DataIntegratyViolationException;
import com.mistergold.mistergold.configuration.web.advice.exception.ResourceNotFoundException;
import com.mistergold.mistergold.configuration.web.enums.RunErrorEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeleteProductPersistenceService implements DeleteProductPort {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPersistenceMapper mapper;

    @Override
    public void delete(String id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RunErrorEnum.ERR0005));
        List<CategoryEntity> categoryEntityList = categoryRepository.findAllById(productEntity.getCategoriesId());

        categoryEntityList.forEach(category -> category.setProductsId(category.getProductsId().stream()
                        .filter(productId -> !productId.equals(productEntity.getId())).collect(Collectors.toSet())));

        categoryRepository.saveAll(categoryEntityList);
        productRepository.deleteById(id);
    }

    @Override
    public Product inactivate(String id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RunErrorEnum.ERR0005));
        if (product.getInfoActivation().getIsActive()) {
            product.getInfoActivation().setIsActive(false);
            product.getInfoActivation().setDeactivationDate(Instant.now());
        } else throw new DataIntegratyViolationException(RunErrorEnum.ERR0004);
        return mapper.mapToDomain(productRepository.save(product));
    }
}
