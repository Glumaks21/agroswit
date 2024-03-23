package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.agroswit.dto.request.CategoryCreationDTO;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.model.Category;
import ua.com.agroswit.repository.CategoryRepository;
import ua.com.agroswit.service.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public Category create(CategoryCreationDTO dto) {
        if (repository.existsByName(dto.name())) {
            throw new ResourceInConflictStateException(String.format(
                    "Category with name \"%s\" already exists", dto.name())
            );
        }

        var category = new Category();
        category.setName(dto.name());

        log.info("Saving new category to db: {}", category);
        return repository.save(category);
    }
}
