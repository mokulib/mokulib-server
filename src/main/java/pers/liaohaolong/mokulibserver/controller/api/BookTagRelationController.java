package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.BookTagRelationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/books/{bookId}/tags")
@AllArgsConstructor
public class BookTagRelationController {

    private final BookTagRelationService bookTagRelationService;

    @PostMapping("/{tagId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void add(@PathVariable @NotNull Integer bookId, @PathVariable @NotNull Integer tagId) {
        bookTagRelationService.add(bookId, tagId);
    }

    @DeleteMapping("/{tagId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void delete(@PathVariable @NotNull Integer bookId, @PathVariable @NotNull Integer tagId) {
        bookTagRelationService.delete(bookId, tagId);
    }

    @GetMapping
    public List<Tag> getTags(@PathVariable @NotNull Integer bookId) {
        return bookTagRelationService.getTags(bookId);
    }

}
