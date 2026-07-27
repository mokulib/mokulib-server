package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.User;

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

    public static BookCopyAdminDTO fromBookCopy(BookCopy bookCopy) {
        BookCopyAdminDTO bookCopyAdminDTO = new BookCopyAdminDTO();

        bookCopyAdminDTO.setId(bookCopy.getId());
        bookCopyAdminDTO.setRole(User.Role.ADMIN);
        bookCopyAdminDTO.setStatus(bookCopy.getStatus());
        // currentBorrowRecord 不进行设置
        bookCopyAdminDTO.setPurchasePrice(bookCopy.getPurchasePrice());
        bookCopyAdminDTO.setPurchaseDate(bookCopy.getPurchaseDate());
        bookCopyAdminDTO.setSource(bookCopy.getSource());
        bookCopyAdminDTO.setEntryBy(bookCopy.getEntryBy());
        bookCopyAdminDTO.setWithdrawnReason(bookCopy.getWithdrawnReason());
        bookCopyAdminDTO.setCreateTime(bookCopy.getCreateTime());
        bookCopyAdminDTO.setWithdrawnTime(bookCopy.getWithdrawnTime());

        return bookCopyAdminDTO;
    }

}
