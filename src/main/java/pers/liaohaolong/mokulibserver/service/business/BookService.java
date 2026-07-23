package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;

public interface BookService {

    int add(BookDTO bookDTO) throws BusinessException;

    void delete(Integer id) throws BusinessException;

    Book update(Integer id, BookDTO bookDTO) throws BusinessException;

    Book get(String id) throws BusinessException;

}
