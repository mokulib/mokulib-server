package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
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
    public BookCopyAdminDTO add(@AuthenticationPrincipal User user, @RequestBody BookCopyDTO bookCopyDTO) {
        return bookCopyService.add(user.getId(), bookCopyDTO);
    }

    @PostMapping("/{id}/borrow")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "借阅成功")
    public BookCopyAdminDTO borrow(@PathVariable @NotNull Integer id, @RequestBody BorrowDTO borrowDTO) throws BusinessException {
        return bookCopyService.borrow(id, borrowDTO);
    }

}
