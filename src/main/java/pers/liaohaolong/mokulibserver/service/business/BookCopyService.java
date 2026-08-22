package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.AddBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.request.UpdateBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

import java.util.List;

public interface BookCopyService extends IService<BookCopy> {

    BookCopyAdminDTO add(Integer entryBy, AddBookCopyDTO addBookCopyDTO);

    BookCopyAdminDTO update(Integer id, UpdateBookCopyDTO updateBookCopyDTO) throws BusinessException;

    List<BorrowRecord> getBorrowRecords(Integer id) throws BusinessException;

    BookCopyAdminDTO borrow(Integer id, BorrowDTO borrowDTO) throws BusinessException;

    BookCopyAdminDTO withdrawn(Integer id) throws BusinessException;

    BookCopyAdminDTO relist(Integer id) throws BusinessException;

}
