package ua.com.agroswit.productservice.service.iml;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.agroswit.productservice.dto.mapper.PesticideMapper;
import ua.com.agroswit.productservice.dto.request.PesticideModifiableDTO;
import ua.com.agroswit.productservice.dto.response.PesticideCultureDTO;
import ua.com.agroswit.productservice.dto.response.PesticideDTO;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;
import ua.com.agroswit.productservice.model.*;
import ua.com.agroswit.productservice.repository.CultureRepository;
import ua.com.agroswit.productservice.repository.PestRepository;
import ua.com.agroswit.productservice.repository.PesticideRepository;
import ua.com.agroswit.productservice.repository.ProductRepository;
import ua.com.agroswit.productservice.service.PesticideService;
import ua.com.agroswit.productservice.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class PesticideServiceImpl implements PesticideService {

    private final MinioUploadService uploadService;
    private final ProductService productService;
    private final ProductRepository productRepo;
    private final PesticideRepository pesticideRepo;
    private final CultureRepository cultureRepo;
    private final PestRepository pestRepo;
    private final PesticideMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PesticideDTO> getAll(Pageable pageable) {
        return pesticideRepo.findAll(pageable)
                .map(p -> {
                    var combos = pesticideRepo.findAllCombinationsByPesticideId(p.getId());
                    var imageUrl = uploadService.getUrl(p.getImage());
                    System.out.println(combos);

                    return mapper.toDTO(p, imageUrl);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public PesticideDTO getById(Integer id) {
        return pesticideRepo.findById(id)
                .map(p -> {
                    var combos = pesticideRepo.findAllCombinationsByPesticideId(p.getId());
                    var imageUrl = uploadService.getUrl(p.getImage());
                    return mapper.toDTO(p, imageUrl);
                })
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Pesticide with id %d not found", id))
                );
    }

    @Override
    @Transactional
    public PesticideDTO create(PesticideModifiableDTO dto) {
        var createdProductDTO = productService.create(dto);
        var createdProduct = productRepo.findById(createdProductDTO.id()).get();

        var pesticide = mapper.toEntity(dto);
        mapper.merge(createdProduct, pesticide);

        var pesticideCultures = createPesticideCultures(pesticide, dto);

        var pests = pestRepo.findAllById(dto.getPestIds());
        for (var pid : dto.getPestIds()) {
            var doesContain = pests.stream()
                    .anyMatch(p -> pid.equals(p.getId()));
            if (!doesContain) throw new ResourceNotFoundException(String.format(
                    "Pest with id %d not found", pid)
            );
        }

        pesticide.setPests(pests);
        pesticide.setCultures(pesticideCultures);

        log.info("Saving pesticide to db: {}", pesticide);
        pesticideRepo.save(pesticide);
        return mapper.toDTO(pesticide, null);
    }

    private List<PesticideCulture> createPesticideCultures(Pesticide pesticide, PesticideModifiableDTO dto) {
        if (dto.getCultures() == null) {
            return null;
        }

//        var cultureIds = dto.cultures().stream().map(PesticideCultureDTO::id).toList();
//        var cultures = cultureRepo.findAllById(cultureIds).stream()
//                .collect(Collectors.toMap(Culture::getId, c -> c));
//
//        var pesticideCultures = new ArrayList<PesticideCulture>();
//        for (var cdto : dto.cultures()) {
//            var culture = cultures.get(cdto.id());
//            if (culture == null) {
//                throw new ResourceNotFoundException(String.format(
//                        "Culture with id %d not found", cdto.id())
//                );
//            }
//
//            var pestCult = new PesticideCulture();
//            pestCult.setMaxVolume(cdto.maxVolume());
//            pestCult.setMinVolume(cdto.minVolume());
//            pestCult.setCulture(culture);
//            pestCult.setPesticide(pesticide);
//            pesticideCultures.add(pestCult);
//        }

//        return pesticideCultures;
        return null;
    }
}
