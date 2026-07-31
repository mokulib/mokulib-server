package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.request.AddBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.request.UpdateBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

public interface BookCopyService {

    BookCopyAdminDTO add(Integer entryBy, AddBookCopyDTO addBookCopyDTO);

    BookCopyAdminDTO update(Integer id, UpdateBookCopyDTO updateBookCopyDTO) throws BusinessException;

    BookCopyAdminDTO borrow(Integer id, BorrowDTO borrowDTO) throws BusinessException;

}
