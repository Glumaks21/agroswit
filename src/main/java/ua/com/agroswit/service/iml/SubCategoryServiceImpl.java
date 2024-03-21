package ua.com.agroswit.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.com.agroswit.model.SubCategory;
import ua.com.agroswit.repository.SubCategoryRepository;
import ua.com.agroswit.service.SubCategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository repository;

    @Override
    public List<SubCategory> getAllByCategoryId(Integer categoryId) {
        return repository.findAllByCategoryId(categoryId);
    }
}
