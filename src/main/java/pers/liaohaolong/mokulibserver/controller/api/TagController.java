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
    public List<Tag> add(@RequestBody @NotNull @NotEmpty List<@NotNull @NotBlank String> tags) {
        return tagService.add(tags);
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

}
