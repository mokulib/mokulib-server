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
import pers.liaohaolong.mokulibserver.dto.request.ReturnBookDTO;
import pers.liaohaolong.mokulibserver.dto.response.BookCopyAdminDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;
import pers.liaohaolong.mokulibserver.model.User;
import pers.liaohaolong.mokulibserver.service.business.BorrowRecordService;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class BorrowRecordServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowRecordService {

    private final BookCopyMapper bookCopyMapper;

    @Override
    @Transactional
    public BorrowRecord renew(User user, Integer id) throws BusinessException {
        // 获取借阅记录
        BorrowRecord borrowRecord = getById(id);

        // 验证
        if (borrowRecord == null)
            throw new BusinessException("借阅记录不存在");
        if (user.getRole() == User.Role.USER && !Objects.equals(borrowRecord.getUserId(), user.getId()))
            throw new BusinessException("您没有权限续借此图书");
        if (borrowRecord.getCloseStatus() != BorrowRecord.CloseStatus.OPEN)
            throw new BusinessException("图书已归还，无法续借");
        if (borrowRecord.getIsRenewed())
            throw new BusinessException("已续借一次，不能再次续借");

        // 续借
        update(new LambdaUpdateWrapper<BorrowRecord>()
                .eq(BorrowRecord::getId, id)
                .set(BorrowRecord::getIsRenewed, true) // 设置已续借
                .set(BorrowRecord::getDueTime, borrowRecord.getDueTime().plusDays(7)) // 续借 7 天
        );

        return getById(id);
    }

    @Override
    @Transactional
    public BookCopyAdminDTO returnBook(Integer id, ReturnBookDTO returnBookDTO) throws BusinessException {
        // 获取借阅记录
        BorrowRecord borrowRecord = getById(id);

        // 验证
        if (borrowRecord == null)
            throw new BusinessException("借阅记录不存在");
        if (borrowRecord.getCloseStatus() != BorrowRecord.CloseStatus.OPEN)
            throw new BusinessException("图书已归还，请勿重复操作");
        if (borrowRecord.getCreateTime().isAfter(returnBookDTO.getCloseTime()))
            throw new BusinessException("归还时间不能早于借阅时间");

        // 归还
        update(new LambdaUpdateWrapper<BorrowRecord>()
                .eq(BorrowRecord::getId, id)
                .set(BorrowRecord::getCloseStatus, returnBookDTO.getCloseStatus())
                .set(BorrowRecord::getCloseTime, returnBookDTO.getCloseTime())
        );

        // 正常归还
        if (returnBookDTO.getCloseStatus() == BorrowRecord.CloseStatus.CLOSED) {
            bookCopyMapper.update(new LambdaUpdateWrapper<BookCopy>()
                    .eq(BookCopy::getId, borrowRecord.getBookCopyId())
                    .set(BookCopy::getStatus, BookCopy.Status.AVAILABLE)
            );
            return BookCopyAdminDTO.fromBookCopy(bookCopyMapper.selectById(borrowRecord.getBookCopyId()));
        }

        // 异常归还
        bookCopyMapper.update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, borrowRecord.getBookCopyId())
                .set(BookCopy::getStatus, BookCopy.Status.WITHDRAWN)
                .set(BookCopy::getWithdrawnReason, returnBookDTO.toWithdrawnReason())
                .set(BookCopy::getWithdrawnTime, returnBookDTO.getCloseTime())
        );
        return BookCopyAdminDTO.fromBookCopy(bookCopyMapper.selectById(borrowRecord.getBookCopyId()));
    }

    @Override
    @Transactional
    public void rollbackReturn(Integer id) throws BusinessException {
        BorrowRecord borrowRecord = getById(id);

        if (borrowRecord == null)
            throw new BusinessException("拒绝操作，借阅记录不存在");
        if (exists(new LambdaQueryWrapper<BorrowRecord>().eq(BorrowRecord::getBookCopyId, borrowRecord.getBookCopyId()).gt(BorrowRecord::getCreateTime, borrowRecord.getCreateTime())))
            throw new BusinessException("拒绝操作，只能撤销最近一次的归还操作");
        if (borrowRecord.getCloseStatus() == BorrowRecord.CloseStatus.OPEN)
            throw new BusinessException("拒绝操作，图书尚未归还");

        // 撤销归还
        update(new LambdaUpdateWrapper<BorrowRecord>()
                .eq(BorrowRecord::getId, id)
                .set(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
                .set(BorrowRecord::getCloseTime, null)
        );
        // 恢复馆藏状态
        bookCopyMapper.update(new LambdaUpdateWrapper<BookCopy>()
                .eq(BookCopy::getId, borrowRecord.getBookCopyId())
                .set(BookCopy::getStatus, BookCopy.Status.UNAVAILABLE)
                .set(BookCopy::getWithdrawnReason, null)
                .set(BookCopy::getWithdrawnTime, null)
        );
    }
}
