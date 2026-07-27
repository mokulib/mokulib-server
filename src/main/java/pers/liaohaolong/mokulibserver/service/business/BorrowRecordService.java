package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.request.ReturnBookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;

public interface BorrowRecordService {

    BorrowRecord renew(User user, Integer id) throws BusinessException;

    BookCopyAdminDTO returnBook(Integer id, ReturnBookDTO returnBookDTO) throws BusinessException;

}
