package pers.liaohaolong.mokulibserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDTO {

    public static final BorrowingDTO EMPTY = new BorrowingDTO(List.of(), List.of());

    private List<Book> books;

    private List<BorrowRecordWithBookId> borrowRecords;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class BorrowRecordWithBookId extends BorrowRecord {

        private Integer bookId;

        public static BorrowRecordWithBookId of(BorrowRecord borrowRecord, Integer bookId) {
            BorrowRecordWithBookId borrowRecordWithBookId = new BorrowRecordWithBookId();
            borrowRecordWithBookId.setId(borrowRecord.getId());
            borrowRecordWithBookId.setUserId(borrowRecord.getUserId());
            borrowRecordWithBookId.setBookCopyId(borrowRecord.getBookCopyId());
            borrowRecordWithBookId.setIsRenewed(borrowRecord.getIsRenewed());
            borrowRecordWithBookId.setCloseStatus(borrowRecord.getCloseStatus());
            borrowRecordWithBookId.setCreateTime(borrowRecord.getCreateTime());
            borrowRecordWithBookId.setDueTime(borrowRecord.getDueTime());
            borrowRecordWithBookId.setCloseTime(borrowRecord.getCloseTime());
            borrowRecordWithBookId.setBookId(bookId);
            return borrowRecordWithBookId;
        }

    }

}
