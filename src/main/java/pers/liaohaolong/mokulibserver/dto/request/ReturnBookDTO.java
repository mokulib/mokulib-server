package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.annotation.validation.ValidReturnStatus;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnBookDTO {

    @NotNull(message = "归还状态不能为空")
    @ValidReturnStatus(message = "归还状态错误")
    private BorrowRecord.CloseStatus closeStatus;

    @NotNull(message = "归还时间不能为空")
    private LocalDateTime closeTime;

    public BookCopy.WithdrawnReason toWithdrawnReason() {
        return switch (closeStatus) {
            case LOST -> BookCopy.WithdrawnReason.LOST;
            case DAMAGED -> BookCopy.WithdrawnReason.DAMAGED;
            default -> throw new RuntimeException("内部服务器错误");
        };
    }

}
