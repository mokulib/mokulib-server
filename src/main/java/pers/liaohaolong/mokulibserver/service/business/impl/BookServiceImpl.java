package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.service.business.BookService;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public int add(BookDTO bookDTO) throws BusinessException {
        if (bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, bookDTO.getIsbn())))
            throw new BusinessException("图书已存在");

        Book book = Book.fromDTO(bookDTO);

        bookMapper.insert(book);

        return book.getId();
    }

    @Override
    @Transactional
    public void delete(Integer id) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, id)))
            throw new BusinessException("图书不存在");

        bookMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Integer id, BookDTO bookDTO) throws BusinessException {
        Book book = Book.fromDTO(bookDTO);

        book.setId(id);

        bookMapper.updateById(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Book get(String id) throws BusinessException {
        Book book = bookMapper.selectById(id);

        if (book == null) {
            throw new BusinessException("图书不存在");
        }

        return book;
    }

}
