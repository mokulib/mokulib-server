package pers.liaohaolong.mokulibserver.dto.response;

import lombok.Data;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.time.LocalDateTime;

@Data
public class HistoryDTO {

    private Integer id;

    private Integer bookCopyId;

    private Integer bookId;

    private LocalDateTime borrowTime;

    private LocalDateTime returnTime;

    private BorrowRecord.CloseStatus closeStatus;

    private Boolean isRenewed;

    private LocalDateTime dueTime;

}
