package pers.liaohaolong.mokulibserver.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.service.business.RankService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/ranks")
@AllArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/borrow")
    public List<Integer> borrow() {
        return rankService.borrow();
    }

}
