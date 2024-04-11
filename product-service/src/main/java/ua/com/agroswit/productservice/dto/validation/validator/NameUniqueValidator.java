package ua.com.agroswit.productservice.dto.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ua.com.agroswit.productservice.dto.CategoryDTO;
import ua.com.agroswit.productservice.dto.ProductDTO;
import ua.com.agroswit.productservice.dto.validation.constraint.NameUnique;

import java.util.Set;

public class NameUniqueValidator implements ConstraintValidator<NameUnique, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        if (obj == null) return true;
        if (obj instanceof Set<?> set && !set.isEmpty()) {
            var elements = set.toArray();

            if (elements[0] instanceof CategoryDTO.PropertyDTO) {
                return isCategoryPropsValid((CategoryDTO.PropertyDTO[]) elements);
            } else if (elements[0] instanceof ProductDTO.PropertyDTO) {
                return isProductPropsValid((ProductDTO.PropertyDTO[]) elements);
            }
        }

        return true;
    }

    private boolean isCategoryPropsValid(CategoryDTO.PropertyDTO[] props) {
        for (int i = 0; i < props.length - 1; i++) {
            for (int j = i + 1; j < props.length; j++) {
                if (props[i].name().equals(props[j].name())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isProductPropsValid(ProductDTO.PropertyDTO[] props) {
        for (int i = 0; i < props.length - 1; i++) {
            for (int j = i + 1; j < props.length; j++) {
                if (props[i].name().equals(props[j].name())) {
                    return false;
                }
            }
        }
        return true;
    }
}
