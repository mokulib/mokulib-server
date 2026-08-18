package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankDTO {

    private List<Integer> rank;

    private LocalDateTime updateTime;

}
