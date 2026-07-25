package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookCopyMapper;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.BorrowRecordMapper;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyMyBorrowDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.BookService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookMapper bookMapper;

    private final BookCopyMapper bookCopyMapper;

    private final BorrowRecordMapper borrowRecordMapper;

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
    public Book update(Integer id, BookDTO bookDTO) throws BusinessException {
        Book book = Book.fromDTO(bookDTO);

        book.setId(id);

        if (bookMapper.exists(new LambdaQueryWrapper<Book>()
                .ne(Book::getId, book.getId())
                .eq(Book::getIsbn, book.getIsbn())
        ))
            throw new BusinessException("ISBN 重复");

        bookMapper.updateById(book);

        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public Book get(String id) throws BusinessException {
        Book book = bookMapper.selectById(id);

        if (book == null)
            throw new BusinessException("图书不存在");

        return book;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopyUserDTO> getUserBookCopies(Integer userId, Integer bookId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        // 获取全部非下架副本数据
        List<BookCopy> bookCopies = bookCopyMapper.selectList(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getBookId, bookId).ne(BookCopy::getStatus, BookCopy.Status.WITHDRAWN));

        // 查询每个副本的当前借阅信息（限定为当前用户借阅的，正常情况下，只会返回一条记录，即一个用户同时最多只借一本书）
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, userId)
                .in(BorrowRecord::getBookCopyId, bookCopies.stream().map(BookCopy::getId).toList())
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        // 将我借阅的记录，映射为 Map 以便后续处理
        Map<Integer, BorrowRecord> borrowRecordMap = borrowRecords.stream().collect(Collectors.toMap(BorrowRecord::getBookCopyId, Function.identity()));

        // 收集结果并返回
        return bookCopies.stream().map(bookCopy -> {
            // 获取此副本的我的借阅记录（不一定存在）
            BorrowRecord borrowRecord = borrowRecordMap.get(bookCopy.getId());
            // 创建 DTO
            BookCopyUserDTO bookCopyUserDTO = new BookCopyUserDTO();
            bookCopyUserDTO.setId(bookCopy.getId());
            bookCopyUserDTO.setRole(User.Role.USER);
            bookCopyUserDTO.setStatus(bookCopy.getStatus());
            bookCopyUserDTO.setMyBorrow(borrowRecord == null ? null : new BookCopyMyBorrowDTO(borrowRecord.getIsRenewed(), borrowRecord.getDueTime()));
            return bookCopyUserDTO;
        }).toList();
    }

    @Override
    public List<BookCopyAdminDTO> getAdminBookCopies(Integer userId, Integer bookId) throws BusinessException {
        if (!bookMapper.exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        // 获取全部副本数据
        List<BookCopy> bookCopies = bookCopyMapper.selectList(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getBookId, bookId));

        // 查询每个副本的当前借阅信息（不限定借阅用户）
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .in(BorrowRecord::getBookCopyId, bookCopies.stream().map(BookCopy::getId).toList())
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        // 映射为 Map 以便后续处理
        Map<Integer, BorrowRecord> borrowRecordMap = borrowRecords.stream().collect(Collectors.toMap(BorrowRecord::getBookCopyId, Function.identity()));

        // 收集结果并返回
        return bookCopies.stream().map(bookCopy -> {
            // 获取此副本的当前借阅记录（不一定存在）
            BorrowRecord borrowRecord = borrowRecordMap.get(bookCopy.getId());
            // 创建 DTO
            BookCopyAdminDTO bookCopyAdminDTO = new BookCopyAdminDTO();
            bookCopyAdminDTO.setId(bookCopy.getId());
            bookCopyAdminDTO.setRole(User.Role.ADMIN);
            bookCopyAdminDTO.setStatus(bookCopy.getStatus());
            bookCopyAdminDTO.setMyBorrow(borrowRecord == null || !Objects.equals(borrowRecord.getUserId(), userId) ? null : new BookCopyMyBorrowDTO(borrowRecord.getIsRenewed(), borrowRecord.getDueTime()));
            bookCopyAdminDTO.setPurchasePrice(bookCopy.getPurchasePrice());
            bookCopyAdminDTO.setPurchaseDate(bookCopy.getPurchaseDate());
            bookCopyAdminDTO.setSource(bookCopy.getSource());
            bookCopyAdminDTO.setEntryBy(bookCopy.getEntryBy());
            bookCopyAdminDTO.setWithdrawnReason(bookCopy.getWithdrawnReason());
            bookCopyAdminDTO.setCreateTime(bookCopy.getCreateTime());
            bookCopyAdminDTO.setWithdrawnTime(bookCopy.getWithdrawnTime());
            bookCopyAdminDTO.setCurrentBorrowRecord(borrowRecord);
            return bookCopyAdminDTO;
        }).toList();
    }

}
