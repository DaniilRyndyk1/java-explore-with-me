package ru.practicum.event.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
public class EventMapperTest {
    private final EventMapper mapper;

    private final String name = "theatre";
    private final CategoryDto categoryDto = new CategoryDto(-1L, name);
    private final NewCategoryDto newCategoryDto = new NewCategoryDto(name);
    private final Category category = new Category(15L, name);

//    @Test
//    void shouldConvertNewCategoryDtoToCategory() {
//        var result = mapper.toCategory(
//                category.getId(),
//                newCategoryDto
//        );
//        assertEquals(result.getId(), category.getId());
//        assertEquals(result.getName(), category.getName());
//    }

//    @Test
//    void shouldConvertCategoryDtoToCategory() {
//        var result = mapper.toCategory(
//                category.getId(),
//                categoryDto
//        );
//        assertEquals(result.getId(), category.getId());
//        assertEquals(result.getName(), category.getName());
//    }

//    @Test
//    void shouldConvertCategoryToCategoryDto() {
//        var result = mapper.toCategoryDto(category);
//        assertEquals(result.getName(), categoryDto.getName());
//    }
}
