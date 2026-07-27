package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookCopyMapper;
import pers.liaohaolong.mokulibserver.dao.BorrowRecordMapper;
import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.dto.request.BorrowDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.service.business.BookCopyService;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyMapper bookCopyMapper;

    private final BorrowRecordMapper borrowRecordMapper;

    @Override
    @Transactional
    public BookCopyAdminDTO add(Integer entryBy, BookCopyDTO bookCopyDTO) {
        BookCopy bookCopy = BookCopy.fromDTO(bookCopyDTO);

        bookCopy.setEntryBy(entryBy);

        bookCopyMapper.insert(bookCopy);

        return BookCopyAdminDTO.fromBookCopy(bookCopyMapper.selectById(bookCopy.getId()));
    }

    @Override
    @Transactional
    public BookCopyAdminDTO borrow(Integer id, BorrowDTO borrowDTO) throws BusinessException {
        // 获取图书副本
        BookCopy bookCopy = bookCopyMapper.selectById(id);
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
        bookCopyMapper.update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, id)
                .set(BookCopy::getStatus, BookCopy.Status.UNAVAILABLE)
        );

        // 构造返回值
        BookCopyAdminDTO bookCopyAdminDTO = BookCopyAdminDTO.fromBookCopy(bookCopyMapper.selectById(id));
        bookCopyAdminDTO.setCurrentBorrowRecord(borrowRecordMapper.selectById(borrowRecord.getId()));
        return bookCopyAdminDTO;
    }

}
