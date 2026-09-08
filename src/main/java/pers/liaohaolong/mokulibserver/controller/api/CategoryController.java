package pers.liaohaolong.mokulibserver.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
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
    public List<Category> add(@RequestBody @NotEmpty List<@NotBlank String> categories) {
        return categoryService.add(categories);
    }

    @DeleteMapping
    public List<Category> delete(@RequestParam(value = "ids") @NotNull List<Integer> ids) {
        return categoryService.delete(ids);
    }

    @PutMapping
    public List<Category> update(@RequestBody @NotNull List<Category> categories) {
        return categoryService.update(categories);
    }

    @GetMapping
    public List<Category> list() {
        return categoryService.list();
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable @NotNull Integer id) throws BusinessException {
        return categoryService.get(id);
    }

    @GetMapping("/{id}/books/page")
    public IPage<Integer> getBooks(@PathVariable @NotNull Integer id, @RequestParam @NotNull Integer pageNum, @RequestParam @NotNull SortModeDTO sortMode) throws BusinessException {
        return categoryService.getBooks(id, pageNum, sortMode);
    }

}
