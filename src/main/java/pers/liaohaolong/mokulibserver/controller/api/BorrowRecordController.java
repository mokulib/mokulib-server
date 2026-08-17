package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.ReturnBookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.BorrowRecordService;

@Slf4j
@RestController
@RequestMapping("/api/borrow-records")
@AllArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @PostMapping("/{id}/renew")
    @PreAuthorize("isAuthenticated()")
    @SuccessInfo(message = "续借成功")
    public BorrowRecord renew(@AuthenticationPrincipal User user, @PathVariable @NotNull Integer id) throws BusinessException {
        return borrowRecordService.renew(user, id);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "归还成功")
    public BookCopyAdminDTO returnBook(@PathVariable @NotNull Integer id, @RequestBody ReturnBookDTO returnBookDTO) throws BusinessException {
        return borrowRecordService.returnBook(id, returnBookDTO);
    }

}
