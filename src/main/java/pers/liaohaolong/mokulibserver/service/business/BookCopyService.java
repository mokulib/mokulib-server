package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

public interface BookCopyService {

    BookCopyAdminDTO add(Integer entryBy, BookCopyDTO bookCopyDTO);

    BookCopyAdminDTO borrow(Integer id, BorrowDTO borrowDTO) throws BusinessException;

}
