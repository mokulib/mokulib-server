package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.annotation.SuccessInfo;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.BookTagRelationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books/{bookId}/tags")
@AllArgsConstructor
public class BookTagRelationController {

    private final BookTagRelationService bookTagRelationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "添加成功")
    public void addTags(@PathVariable @NotNull Integer bookId, @RequestBody @NotNull @NotEmpty List<@NotNull Integer> tagIds) throws BusinessException {
        bookTagRelationService.add(bookId, tagIds);
    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SuccessInfo(message = "删除成功")
    public void deleteTag(@PathVariable @NotNull Integer bookId, @PathVariable @NotNull Integer tagId) throws BusinessException {
        bookTagRelationService.delete(bookId, tagId);
    }

    @GetMapping
    public List<Tag> getTags(@PathVariable @NotNull Integer bookId) {
        return bookTagRelationService.getTags(bookId);
    }

}
