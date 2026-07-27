package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCopyUserDTO {

    private Integer id;

    private User.Role role;

    private BookCopy.Status status;

    private BorrowRecord currentBorrowRecord;

    public static BookCopyUserDTO fromBookCopy(BookCopy bookCopy) {
        BookCopyUserDTO bookCopyUserDTO = new BookCopyUserDTO();

        bookCopyUserDTO.setId(bookCopy.getId());
        bookCopyUserDTO.setRole(User.Role.USER);
        bookCopyUserDTO.setStatus(bookCopy.getStatus());
        // currentBorrowRecord 不进行设置

        return bookCopyUserDTO;
    }

}
