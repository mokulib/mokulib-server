package pers.liaohaolong.mokulibserver.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.model.BookCopy;
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
    public BookCopy add(@AuthenticationPrincipal User user, @RequestBody BookCopyDTO bookCopyDTO) {
        log.info("用户 {} 添加图书副本 {}", user.getUsername(), bookCopyDTO);
        return bookCopyService.add(user.getId(), bookCopyDTO);
    }

}
