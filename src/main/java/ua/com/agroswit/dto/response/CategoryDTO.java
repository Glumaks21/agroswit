package ua.com.agroswit.dto.response;

import java.util.Collection;

public record CategoryDTO(Integer id, String name, Collection<SubCategoryDTO> subcategories) {
}
