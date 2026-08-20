package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookCopyMapper;
import pers.liaohaolong.mokulibserver.dao.BorrowRecordMapper;
import pers.liaohaolong.mokulibserver.dto.request.AddBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.request.UpdateBookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.service.business.BookCopyService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BookCopyServiceImpl extends ServiceImpl<BookCopyMapper, BookCopy> implements BookCopyService {

    private final BorrowRecordMapper borrowRecordMapper;

    @Override
    @Transactional
    public BookCopyAdminDTO add(Integer entryBy, AddBookCopyDTO addBookCopyDTO) {
        BookCopy bookCopy = BookCopy.fromDTO(addBookCopyDTO);

        bookCopy.setEntryBy(entryBy);

        save(bookCopy);

        return BookCopyAdminDTO.fromBookCopy(getById(bookCopy.getId()));
    }

    @Override
    @Transactional
    public BookCopyAdminDTO update(Integer id, UpdateBookCopyDTO updateBookCopyDTO) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getId, id)))
            throw new BusinessException("图书不存在，修改失败");

        update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, id)
                .set(BookCopy::getPurchasePrice, updateBookCopyDTO.getPurchasePrice())
                .set(BookCopy::getPurchaseDate, updateBookCopyDTO.getPurchaseDate())
                .set(BookCopy::getSource, updateBookCopyDTO.getSource())
        );

        BookCopyAdminDTO bookCopyAdminDTO = BookCopyAdminDTO.fromBookCopy(getById(id));
        bookCopyAdminDTO.setCurrentBorrowRecord(borrowRecordMapper.selectOne(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getBookCopyId, id)
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        ));
        return bookCopyAdminDTO;
    }

    @Override
    public BookCopy get(Integer id) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getId, id)))
            throw new BusinessException("图书不存在，获取失败");
        return getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecord> getBorrowRecords(Integer id) throws BusinessException {
        if (!exists(new LambdaQueryWrapper<BookCopy>().eq(BookCopy::getId, id)))
            throw new BusinessException("图书不存在，获取借阅记录失败");

        return borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getBookCopyId, id)
                .orderByDesc(BorrowRecord::getCreateTime)
        );
    }

    @Override
    @Transactional
    public BookCopyAdminDTO borrow(Integer id, BorrowDTO borrowDTO) throws BusinessException {
        // 获取图书副本
        BookCopy bookCopy = getById(id);
        // 验证
        if (bookCopy == null)
            throw new BusinessException("图书不存在，借阅失败");
        if (bookCopy.getStatus() == BookCopy.Status.UNAVAILABLE)
            throw new BusinessException("该书已借出，借阅失败");
        if (bookCopy.getStatus() == BookCopy.Status.WITHDRAWN)
            throw new BusinessException("该书已下架，借阅失败");

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setUserId(borrowDTO.getUserId());
        borrowRecord.setBookCopyId(id);
        borrowRecord.setIsRenewed(borrowDTO.getIsRenewed());
        borrowRecord.setCreateTime(LocalDateTime.now());
        borrowRecord.setDueTime(borrowRecord.getCreateTime().plusDays(borrowDTO.getIsRenewed() ? 14 : 7)); // 借阅 7 / 14 天

        // 插入借阅记录
        borrowRecordMapper.insert(borrowRecord);

        // 更新图书副本状态
        update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, id)
                .set(BookCopy::getStatus, BookCopy.Status.UNAVAILABLE)
        );

        // 构造返回值
        BookCopyAdminDTO bookCopyAdminDTO = BookCopyAdminDTO.fromBookCopy(getById(id));
        bookCopyAdminDTO.setCurrentBorrowRecord(borrowRecordMapper.selectById(borrowRecord.getId()));
        return bookCopyAdminDTO;
    }

    @Override
    @Transactional
    public BookCopyAdminDTO withdrawn(Integer id) throws BusinessException {
        BookCopy bookCopy = getById(id);

        if (bookCopy == null)
            throw new BusinessException("图书不存在，下架失败");
        if (bookCopy.getStatus() == BookCopy.Status.WITHDRAWN)
            throw new BusinessException("该书已下架，下架失败");
        if (bookCopy.getStatus() == BookCopy.Status.UNAVAILABLE)
            throw new BusinessException("该书已借出，下架失败");

        update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, id)
                .set(BookCopy::getStatus, BookCopy.Status.WITHDRAWN)
                .set(BookCopy::getWithdrawnReason, BookCopy.WithdrawnReason.OTHER)
                .set(BookCopy::getWithdrawnTime, LocalDateTime.now())
        );

        return BookCopyAdminDTO.fromBookCopy(getById(id));
    }

    @Override
    @Transactional
    public BookCopyAdminDTO relist(Integer id) throws BusinessException {
        BookCopy bookCopy = getById(id);

        if (bookCopy == null)
            throw new BusinessException("图书不存在，重新上架失败");
        if (bookCopy.getStatus() == BookCopy.Status.AVAILABLE)
            throw new BusinessException("该书已上架，重新上架失败");
        if (bookCopy.getStatus() == BookCopy.Status.UNAVAILABLE)
            throw new BusinessException("该书已借出，重新上架失败");
        if (bookCopy.getWithdrawnReason() != BookCopy.WithdrawnReason.OTHER)
            throw new BusinessException("非其他图书馆原因下架，重新上架失败");

        update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, id)
                .set(BookCopy::getStatus, BookCopy.Status.AVAILABLE)
                .set(BookCopy::getWithdrawnReason, null)
                .set(BookCopy::getWithdrawnTime, null)
        );

        return BookCopyAdminDTO.fromBookCopy(getById(id));
    }

}
