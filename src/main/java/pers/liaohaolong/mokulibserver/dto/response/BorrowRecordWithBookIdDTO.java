package pers.liaohaolong.mokulibserver.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

@Data
@EqualsAndHashCode(callSuper = true)
public class BorrowRecordWithBookIdDTO extends BorrowRecord {

    private Integer bookId;

    public static BorrowRecordWithBookIdDTO of(BorrowRecord borrowRecord, Integer bookId) {
        BorrowRecordWithBookIdDTO borrowRecordWithBookIdDTO = new BorrowRecordWithBookIdDTO();
        borrowRecordWithBookIdDTO.setId(borrowRecord.getId());
        borrowRecordWithBookIdDTO.setUserId(borrowRecord.getUserId());
        borrowRecordWithBookIdDTO.setBookCopyId(borrowRecord.getBookCopyId());
        borrowRecordWithBookIdDTO.setIsRenewed(borrowRecord.getIsRenewed());
        borrowRecordWithBookIdDTO.setCloseStatus(borrowRecord.getCloseStatus());
        borrowRecordWithBookIdDTO.setCreateTime(borrowRecord.getCreateTime());
        borrowRecordWithBookIdDTO.setDueTime(borrowRecord.getDueTime());
        borrowRecordWithBookIdDTO.setCloseTime(borrowRecord.getCloseTime());
        borrowRecordWithBookIdDTO.setBookId(bookId);
        return borrowRecordWithBookIdDTO;
    }

}
