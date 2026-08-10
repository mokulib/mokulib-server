package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.ReturnBookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;

public interface BorrowRecordService extends IService<BorrowRecord> {

    BorrowRecord renew(User user, Integer id) throws BusinessException;

    BookCopyAdminDTO returnBook(Integer id, ReturnBookDTO returnBookDTO) throws BusinessException;

}
