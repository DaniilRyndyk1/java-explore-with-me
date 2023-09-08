package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CategoryServiceTest {
    private final CategoryService service;

    private final NewCategoryDto categoryDto = new NewCategoryDto("theatre");
    private final NewCategoryDto categoryDto2 = new NewCategoryDto("cinema");
    private final NewCategoryDto categoryDto3 = new NewCategoryDto("outdoor");

    private CategoryDto category1;
    private CategoryDto category2;
    private CategoryDto category3;

//    private final EndpointHitInputDto hit4 =
//            new EndpointHitInputDto("ewm-main-service",
//                    "/events/1",
//                    "192.163.1.1",
//                    "2023-06-10 10:00:00");

    @BeforeEach
    void setup() {
        category1 = service.create(categoryDto);
        category2 = service.create(categoryDto2);
    }

    @Test
    void shouldAddCategory() {
        var originalSize = service.getAll(0, 10).toList().size();
        category3 = service.create(categoryDto3);
        var newSize = service.getAll(0, 10).toList().size();
        assertEquals(originalSize + 1, newSize);
    }

    @Test
    void shouldDeleteCategory() {
        var originalSize = service.getAll(0, 10).toList().size();
        service.delete(category1.getId());
        var newSize = service.getAll(0, 10).toList().size();
        assertEquals(originalSize - 1, newSize);
    }

    @Test
    void shouldGetCategoryById() {
        var dto = service.getById(category2.getId());
        assertEquals(category2.getId(), dto.getId());
        assertEquals(category2.getName(), dto.getName());
    }

    @Test
    void shouldGetAllCategories() {
        var DTOs = service.getAll(0, 10);
        var dtosList = DTOs.toList();
        assertEquals(2, dtosList.size());
        assertEquals(dtosList.get(0).getId(), category1.getId());
        assertEquals(dtosList.get(0).getName(), category1.getName());
        assertEquals(dtosList.get(1).getId(), category2.getId());
        assertEquals(dtosList.get(1).getName(), category2.getName());
    }

    @Test
    void shouldUpdateCategory() {
        var dto = new CategoryDto(category1.getId(), "aaaaaoooooaaa");
        service.update(category1.getId(), dto);
        var newDto = service.getById(category1.getId());
        assertNotNull(newDto);
        assertEquals(newDto.getId(), dto.getId());
        assertEquals(newDto.getName(), dto.getName());
    }
}