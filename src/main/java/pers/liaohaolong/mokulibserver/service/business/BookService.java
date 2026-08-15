package pers.liaohaolong.mokulibserver.service.business;

import com.baomidou.mybatisplus.extension.service.IService;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;

import java.util.List;

public interface BookService extends IService<Book> {

    int add(BookDTO bookDTO) throws BusinessException;

    void delete(Integer id) throws BusinessException;

    Book update(Integer id, BookDTO bookDTO) throws BusinessException;

    Book get(String id) throws BusinessException;

    void uploadCover(Integer id, byte[] cover) throws BusinessException;

    List<BookCopyUserDTO> getUserBookCopies(Integer userId, Integer bookId) throws BusinessException;

    List<BookCopyAdminDTO> getAdminBookCopies(Integer userId, Integer bookId) throws BusinessException;

}
