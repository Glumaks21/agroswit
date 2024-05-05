package ua.com.agroswit.productservice.dto.mapper;

import org.mapstruct.*;
import ua.com.agroswit.productservice.dto.PackageDTO;
import ua.com.agroswit.productservice.model.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface PackageMapper {

    PackageDTO toDTO(Package entity);
    Package toEntity(PackageDTO dto);

    @Named("update")
    @BeforeMapping
    default void updateOldPrice(PackageDTO dto, @MappingTarget Package entity) {
        entity.setOldPrice(entity.getPrice());
    }

    @BeanMapping(qualifiedByName = "update")
    void update(PackageDTO dto, @MappingTarget Package entity);

    default void updatePackages(Set<PackageDTO> dtos, @MappingTarget List<Package> packages) {
        var updatedPackages = new ArrayList<Package>(dtos.size());
        for (var dto : dtos) {
            for (var p : packages) {
                if (p.getId().equals(dto.id())) {
                    update(dto, p);
                    updatedPackages.add(p);
                    break;
                }
            }
            var p = toEntity(dto);
            updatedPackages.add(p);
        }
        packages.clear();
        packages.addAll(updatedPackages);
    }
}
