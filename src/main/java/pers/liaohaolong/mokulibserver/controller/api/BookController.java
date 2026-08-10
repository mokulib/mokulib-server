package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.dto.response.SearchResultsDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.BookService;

import java.util.List;

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
    public Book get(@PathVariable @NotBlank String id) throws BusinessException {
        return bookService.get(id);
    }

    @PostMapping(value = "/{id}/cover", consumes = "application/octet-stream")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "上传成功")
    public void uploadCover(@PathVariable @NotNull Integer id, @RequestBody byte[] cover) {
        bookService.uploadCover(id, cover);
    }

    @GetMapping("/{id}/book-copies")
    @PreAuthorize("isAuthenticated()")
    public List<?> getBookCopies(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer id) throws BusinessException {
        if (user.getRole() == User.Role.USER)
            return bookService.getUserBookCopies(user.getId(), id);
        else if (user.getRole() == User.Role.ADMIN)
            return bookService.getAdminBookCopies(user.getId(), id);
        throw new BusinessException("用户角色错误");
    }

    @GetMapping("/search")
    public SearchResultsDTO<Book> search(@RequestParam @NotBlank String keyword, @RequestParam @NotNull SortModeDTO sortMode, @RequestParam @NotNull @Min(1) Integer pageNum) {
        return SearchResultsDTO.of(keyword, sortMode, bookService.search(keyword, sortMode, pageNum));
    }

}
