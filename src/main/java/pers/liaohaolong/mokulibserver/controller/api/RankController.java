package pers.liaohaolong.mokulibserver.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.liaohaolong.mokulibserver.dto.response.RankDTO;
import pers.liaohaolong.mokulibserver.service.business.RankService;

@Slf4j
@RestController
@RequestMapping("/api/ranks")
@AllArgsConstructor
public class RankController {

    private final RankService rankService;

    @GetMapping("/borrow")
    public RankDTO borrow() {
        return rankService.borrow();
    }

    @GetMapping("/favorite")
    public RankDTO favorite() {
        return rankService.favorite();
    }

    @GetMapping("/new-monthly")
    public RankDTO newMonthly() {
        return rankService.newMonthly();
    }

    @GetMapping("/new-store")
    public RankDTO newStore() {
        return rankService.newStore();
    }

}
