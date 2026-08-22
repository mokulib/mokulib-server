package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.dao.BookCopyMapper;
import pers.liaohaolong.mokulibserver.dao.BookMapper;
import pers.liaohaolong.mokulibserver.dao.BorrowRecordMapper;
import pers.liaohaolong.mokulibserver.dao.HotSearchMapper;
import pers.liaohaolong.mokulibserver.dto.request.BookDTO;
import pers.liaohaolong.mokulibserver.dto.request.SortModeDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.Book;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.HotSearch;
import pers.liaohaolong.mokulibserver.service.base.ImageService;
import pers.liaohaolong.mokulibserver.service.business.BookService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final BookCopyMapper bookCopyMapper;
    private final BorrowRecordMapper borrowRecordMapper;
    private final HotSearchMapper hotSearchMapper;
    private final ImageService imageService;

    @Override
    @Transactional
    public int add(BookDTO bookDTO) throws BusinessException {
        if (exists(new LambdaQueryWrapper<Book>().eq(Book::getIsbn, bookDTO.getIsbn())))
            throw new BusinessException("图书已存在");

        Book book = Book.fromDTO(bookDTO);

        save(book);

        return book.getId();
    }

    @Override
    @Transactional
    public void delete(Integer id) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Book>().eq(Book::getId, id)))
            throw new BusinessException("图书不存在");
        removeById(id);
    }

    @Override
    @Transactional
    public Book update(Integer id, BookDTO bookDTO) throws BusinessException {
        Book book = Book.fromDTO(bookDTO);

        book.setId(id);

        if (exists(new LambdaQueryWrapper<Book>()
                .ne(Book::getId, book.getId())
                .eq(Book::getIsbn, book.getIsbn())
        ))
            throw new BusinessException("ISBN 重复");

        updateById(book);

        return book;
    }

    @Override
    @Transactional
    public void uploadCover(Integer id, byte[] cover) throws BusinessException {
        imageService.save(ImageConfigurations.ImageType.BOOKS, String.valueOf(id), cover);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopyUserDTO> getUserBookCopies(Integer userId, Integer bookId) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        // 获取全部非下架副本数据
        List<BookCopy> bookCopies = bookCopyMapper.selectList(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getBookId, bookId).ne(BookCopy::getStatus, BookCopy.Status.WITHDRAWN));

        // 过滤已借出副本
        List<BookCopy> unavailableBookCopies = bookCopies.stream().filter(bookCopy -> bookCopy.getStatus() == BookCopy.Status.UNAVAILABLE).toList();

        // 判空
        if (unavailableBookCopies.isEmpty())
            return bookCopies.stream().map(BookCopyUserDTO::fromBookCopy).toList();

        // 查询已借出副本的当前借阅信息（限定为当前用户借阅的，正常情况下，只会返回一条记录，即一个用户同时最多只借一本书）
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, userId)
                .in(BorrowRecord::getBookCopyId, unavailableBookCopies.stream().map(BookCopy::getId).toList())
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        // 将我借阅的记录，映射为 Map 以便后续处理
        Map<Integer, BorrowRecord> borrowRecordMap = borrowRecords.stream().collect(Collectors.toMap(BorrowRecord::getBookCopyId, Function.identity()));

        // 收集结果并返回
        return bookCopies.stream().map(bookCopy -> {
            BookCopyUserDTO bookCopyUserDTO = BookCopyUserDTO.fromBookCopy(bookCopy);
            bookCopyUserDTO.setCurrentBorrowRecord(borrowRecordMap.get(bookCopy.getId())); // 获取此副本的我的借阅记录（不一定存在）
            return bookCopyUserDTO;
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookCopyAdminDTO> getAdminBookCopies(Integer userId, Integer bookId) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<Book>().eq(Book::getId, bookId)))
            throw new BusinessException("图书不存在");

        // 获取全部副本数据
        List<BookCopy> bookCopies = bookCopyMapper.selectList(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getBookId, bookId));

        // 过滤已借出副本
        List<BookCopy> unavailableBookCopies = bookCopies.stream().filter(bookCopy -> bookCopy.getStatus() == BookCopy.Status.UNAVAILABLE).toList();

        // 判空
        if (unavailableBookCopies.isEmpty())
            return bookCopies.stream().map(BookCopyAdminDTO::fromBookCopy).toList();

        // 查询已借出副本的当前借阅信息（不限定借阅用户）
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .in(BorrowRecord::getBookCopyId, unavailableBookCopies.stream().map(BookCopy::getId).toList())
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        // 映射为 Map 以便后续处理
        Map<Integer, BorrowRecord> borrowRecordMap = borrowRecords.stream().collect(Collectors.toMap(BorrowRecord::getBookCopyId, Function.identity()));

        // 收集结果并返回
        return bookCopies.stream().map(bookCopy -> {
            BookCopyAdminDTO bookCopyAdminDTO = BookCopyAdminDTO.fromBookCopy(bookCopy);
            bookCopyAdminDTO.setCurrentBorrowRecord(borrowRecordMap.get(bookCopy.getId())); // 获取此副本的当前借阅记录（不一定存在）
            return bookCopyAdminDTO;
        }).toList();
    }

    @Override
    @Transactional
    public IPage<Integer> search(String keyword, SortModeDTO sortMode, Integer pageNum) throws BusinessException {
        // 搜索业务
        Page<Book> page = page(new Page<>(pageNum, 5), SortModeDTO.apply(new LambdaQueryWrapper<Book>().like(Book::getTitle, keyword), sortMode));
        // 热搜统计，只对有结果的、默认状态的搜索进行统计
        if (page.getTotal() > 0 && sortMode == SortModeDTO.PUBLISH_DATE_FROM_NEW_TO_OLD && pageNum == 1) {
            HotSearch hotSearch = hotSearchMapper.selectOne(new LambdaQueryWrapper<HotSearch>().eq(HotSearch::getKeyword, keyword));
            if (hotSearch == null) {
                hotSearch = new HotSearch();
                hotSearch.setKeyword(keyword);
                hotSearch.setCount(1);
            } else {
                hotSearch.setCount(hotSearch.getCount() + 1);
            }
            hotSearch.setUpdateTime(LocalDateTime.now());
            hotSearchMapper.insertOrUpdate(hotSearch);
        }
        // 返回搜索结果
        return page.convert(Book::getId);
    }

}
