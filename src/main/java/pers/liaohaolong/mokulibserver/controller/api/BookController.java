package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.service.business.BookService;

@Slf4j
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public int add(@RequestBody @Valid BookDTO bookDTO) {
        return bookService.add(bookDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(@PathVariable @NotNull Integer id) {
        bookService.delete(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "更新成功")
    public Book update(@PathVariable @NotNull Integer id, @RequestBody @Valid BookDTO bookDTO) {
        return bookService.update(id, bookDTO);
    }

    @GetMapping("/{id}")
    public Book get(@PathVariable @NotNull @NotBlank String id) throws BusinessException {
        return bookService.get(id);
    }

}
