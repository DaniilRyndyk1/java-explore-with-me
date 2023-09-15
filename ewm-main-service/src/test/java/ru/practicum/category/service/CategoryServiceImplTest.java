package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.handler.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class CategoryServiceImplTest {
    private final CategoryServiceImpl service;

    private final NewCategoryDto categoryDto = new NewCategoryDto("theatre");
    private final NewCategoryDto categoryDto2 = new NewCategoryDto("cinema");
    private final NewCategoryDto categoryDto3 = new NewCategoryDto("outdoor");

    private CategoryDto category1;
    private CategoryDto category2;

    @BeforeEach
    void setup() {
        category1 = service.create(categoryDto);
        category2 = service.create(categoryDto2);
    }

    @Test
    void shouldAddCategory() {
        var originalSize = service.getAll(0, 10).size();
        service.create(categoryDto3);
        var newSize = service.getAll(0, 10).size();
        assertEquals(originalSize + 1, newSize);
    }

    @Test
    void shouldNotAddCategoryWithSameName() {
        assertThrows(DataIntegrityViolationException.class,
                () -> service.create(categoryDto));
    }

    @Test
    void shouldNotAddCategoryWithNullName() {
        var categoryDto3 = new NewCategoryDto(null);
        assertThrows(DataIntegrityViolationException.class,
                () -> service.create(categoryDto3));
    }

    @Test
    void shouldDeleteCategory() {
        var originalSize = service.getAll(0, 10).size();
        service.delete(category1.getId());
        var newSize = service.getAll(0, 10).size();
        assertEquals(originalSize - 1, newSize);
    }

    @Test
    void shouldNotDeleteCategoryWithWrongId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.delete(999L));
    }

    @Test
    void shouldNotDeleteCategoryWithNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> service.delete(null));
    }

    @Test
    void shouldGetCategoryById() {
        var dto = service.getDtoById(category2.getId());
        assertEquals(category2.getId(), dto.getId());
        assertEquals(category2.getName(), dto.getName());
    }

    @Test
    void shouldNotGetCategoryByWrongId() {
        assertThrows(EntityNotFoundException.class,
                () -> service.getDtoById(999L));
    }

    @Test
    void shouldNotGetCategoryByNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> service.getDtoById(null));
    }

    @Test
    void shouldGetAllCategories() {
        var DTOs = service.getAll(0, 10);
        assertEquals(2, DTOs.size());
        assertEquals(DTOs.get(0).getId(), category1.getId());
        assertEquals(DTOs.get(0).getName(), category1.getName());
        assertEquals(DTOs.get(1).getId(), category2.getId());
        assertEquals(DTOs.get(1).getName(), category2.getName());
    }

    @Test
    void shouldNotGetAllCategoriesWithFromNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(-5, 10));
    }

    @Test
    void shouldNotGetAllCategoriesWithSizeNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(0, -10));
    }

    @Test
    void shouldNotGetAllCategoriesWithSizeEqualsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getAll(0, 0));
    }

    @Test
    void shouldNotGetAllCategoriesWithNullSize() {
        assertThrows(NullPointerException.class,
                () -> service.getAll(0, null));
    }

    @Test
    void shouldNotGetAllCategoriesWithNullFrom() {
        assertThrows(NullPointerException.class,
                () -> service.getAll(null, 10));
    }

    @Test
    void shouldUpdateCategory() {
        var dto = new CategoryDto(category1.getId(), "aaaaaoooooaaa");
        service.update(category1.getId(), dto);
        var newDto = service.getDtoById(category1.getId());
        assertNotNull(newDto);
        assertEquals(newDto.getId(), dto.getId());
        assertEquals(newDto.getName(), dto.getName());
    }

    @Test
    void shouldNotUpdateCategoryWithWrongId() {
        var dto = new CategoryDto(999L, "aaaaaoooooaaa");
        assertThrows(EntityNotFoundException.class,
                () -> service.update(999L, dto));
    }

    @Test
    void shouldNotUpdateCategoryWithNullName() {
        var dto = new CategoryDto(category1.getId(), null);
        service.update(category1.getId(), dto);
        var newDto = service.getDtoById(category1.getId());
        assertNotNull(newDto);
        assertEquals(newDto.getId(), dto.getId());
        assertEquals(newDto.getName(), dto.getName());
    }

    @Test
    void shouldNotUpdateCategoryWithNullId() {
        var dto = new CategoryDto(null, "test");
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> service.update(null, dto));
    }
}