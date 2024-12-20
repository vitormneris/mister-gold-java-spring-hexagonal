package com.mistergold.mistergold.adapters.web.in.category.mapper;

import com.mistergold.mistergold.adapters.web.PageResponseDTO;
import com.mistergold.mistergold.adapters.web.in.InfoActivationDTO;
import com.mistergold.mistergold.adapters.web.in.category.dto.CategoryDTO;
import com.mistergold.mistergold.adapters.web.in.product.dto.ProductDTO;
import com.mistergold.mistergold.application.domain.InfoActivation;
import com.mistergold.mistergold.application.domain.PageResponse;
import com.mistergold.mistergold.application.domain.category.Category;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryWebMapper {
    Set<CategoryDTO> mapToListDTO(Set<Category> categories);

    default CategoryDTO mapToDTO(Category category) {
        Set<ProductDTO> productDTOS = category.getProducts().stream().map(product -> ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .infoActivation(mapToDTO(product.getInfoActivation()))
                .build()).collect(Collectors.toSet());

        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .products(productDTOS)
                .infoActivation(mapToDTO(category.getInfoActivation()))
                .build();
    }


    Category mapToDomain(CategoryDTO categoryDTO);
    PageResponseDTO<CategoryDTO> mapToPageResponseDto(PageResponse<Category> pageResponse);
    InfoActivationDTO mapToDTO(InfoActivation infoActivation);
    InfoActivation mapToDomain(InfoActivationDTO infoActivation);
}
