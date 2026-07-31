package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.AddBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.request.UpdateBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.BookCopyService;

@Slf4j
@RestController
@RequestMapping("/api/book-copies")
@AllArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "添加成功")
    public BookCopyAdminDTO add(@AuthenticationPrincipal User user, @RequestBody AddBookCopyDTO addBookCopyDTO) {
        return bookCopyService.add(user.getId(), addBookCopyDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "修改成功")
    public BookCopyAdminDTO update(@PathVariable @NotNull Integer id, @RequestBody UpdateBookCopyDTO updateBookCopyDTO) throws BusinessException {
        return bookCopyService.update(id, updateBookCopyDTO);
    }

    @PostMapping("/{id}/borrow")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "借阅成功")
    public BookCopyAdminDTO borrow(@PathVariable @NotNull Integer id, @RequestBody BorrowDTO borrowDTO) throws BusinessException {
        return bookCopyService.borrow(id, borrowDTO);
    }

    @PostMapping("/{id}/withdrawn")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "下架成功")
    public BookCopyAdminDTO withdrawn(@PathVariable @NotNull Integer id) throws BusinessException {
        return bookCopyService.withdrawn(id);
    }

    @PostMapping("/{id}/relist")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "重新上架成功")
    public BookCopyAdminDTO relist(@PathVariable @NotNull Integer id) throws BusinessException {
        return bookCopyService.relist(id);
    }

}
