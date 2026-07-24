package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.dto.request.AddCategoryDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Category;
import pers.liaohaolong.mokulibserver.service.business.CategoryService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public Category add(@RequestBody @NotNull AddCategoryDTO addCategoryDTO) {
        return categoryService.add(addCategoryDTO.getName());
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable @NotNull Integer id) throws BusinessException {
        return categoryService.get(id);
    }

}
