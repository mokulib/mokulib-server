package pers.liaohaolong.mokulibserver.controller.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pers.liaohaolong.mokulibserver.model.Tag;
import pers.liaohaolong.mokulibserver.service.business.TagService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Tag> add(@RequestBody @NotEmpty List<@NotBlank String> tags) {
        return tagService.add(tags);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Tag> delete(@RequestParam(value = "ids") @NotEmpty List<Integer> ids) {
        return tagService.delete(ids);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<Tag> update(@RequestBody @NotEmpty List<@NotNull Tag> tags) {
        return tagService.update(tags);
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.list();
    }

}
