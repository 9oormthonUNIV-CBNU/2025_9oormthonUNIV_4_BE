package goormthon_group4.backend.domain.project.service;

import goormthon_group4.backend.domain.project.entity.Category;
import goormthon_group4.backend.domain.project.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category getOrCreateByTitle(String title) {
        String clean = title.trim();
        return categoryRepository.findByTitle(clean)
                .orElseGet(() -> categoryRepository.save(
                        Category.builder()
                                .title(clean)
                                .build()
                ));
    }
}
