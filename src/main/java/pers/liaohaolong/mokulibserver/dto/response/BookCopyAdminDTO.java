package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyAdminDTO extends BookCopyUserDTO {

    private BigDecimal purchasePrice;

    private LocalDate purchaseDate;

    private String source;

    private Integer entryBy;

    private BookCopy.WithdrawnReason withdrawnReason;

    private LocalDateTime createTime;

    private LocalDateTime withdrawnTime;

    private BorrowRecord currentBorrowRecord;

}
