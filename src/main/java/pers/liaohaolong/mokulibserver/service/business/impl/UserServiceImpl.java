package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.dao.*;
import pers.liaohaolong.mokulibserver.dto.response.BorrowRecordWithBookIdDTO;
import pers.liaohaolong.mokulibserver.dto.response.HistoryDTO;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.*;
import pers.liaohaolong.mokulibserver.service.base.ImageService;
import pers.liaohaolong.mokulibserver.service.business.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final ImageService imageService;
    private final BookMapper bookMapper;
    private final BookCopyMapper bookCopyMapper;
    private final BorrowRecordMapper borrowRecordMapper;
    private final FavoriteMapper favoriteMapper;

    @Override
    @Transactional(readOnly = true)
    public NonsensitiveUserDTO get(@NonNull Integer id) throws BusinessException {
        User user = getById(id);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public NonsensitiveUserDTO get(@NonNull String email) throws BusinessException {
        User user = getBaseMapper().selectByEmail(email);

        if (user == null)
            throw new BusinessException("用户不存在");

        return NonsensitiveUserDTO.fromUser(user);
    }

    @Override
    public void uploadAvatar(Integer id, byte[] avatar) throws BusinessException {
        imageService.save(ImageConfigurations.ImageType.AVATARS, String.valueOf(id), avatar);
    }

    @Override
    public void updateUsername(Integer id, String username) throws BusinessException {
        update(new LambdaUpdateWrapper<User>().eq(User::getId, id).set(User::getUsername, username));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordWithBookIdDTO> getBorrowing(@NonNull Integer id) {
        // 查询借阅记录
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, id)
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        if (borrowRecords.isEmpty())
            return List.of();

        // 待查询的馆藏 ID 列表
        List<Integer> bookCopyIds = borrowRecords.stream().map(BorrowRecord::getBookCopyId).distinct().toList();
        // 批量查询
        List<BookCopy> bookCopies = bookCopyMapper.selectByIds(bookCopyIds);
        // 构造 bookCopyId -> bookId 的映射
        Map<Integer, Integer> bookCopyIdToBookId = bookCopies.stream().collect(Collectors.toMap(BookCopy::getId, BookCopy::getBookId));

        // 构造 BorrowRecordWithBookId 列表
        return borrowRecords.stream().map(borrowRecord ->
            BorrowRecordWithBookIdDTO.of(borrowRecord, bookCopyIdToBookId.get(borrowRecord.getBookCopyId()))
        ).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getFavorites(@NonNull Integer id) {
        // 查询收藏的图书 ID 列表
        List<Integer> bookIds = favoriteMapper.selectList(new LambdaQueryWrapper<Favorite>()
                .eq(Favorite::getUserId, id)
        ).stream().map(Favorite::getBookId).toList();

        if (bookIds.isEmpty())
            return List.of();

        // 批量查询收藏的图书
        return bookMapper.selectByIds(bookIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryDTO> getHistory(@NonNull Integer id) {
        // 查询已完成的借阅记录
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, id)
                .ne(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        if (borrowRecords.isEmpty())
            return List.of();

        // 待查询的图书副本 ID 列表
        List<Integer> bookCopyIds = borrowRecords.stream().map(BorrowRecord::getBookCopyId).distinct().toList();
        // 批量查询图书副本
        List<BookCopy> bookCopies = bookCopyMapper.selectByIds(bookCopyIds);
        // 构造 图书副本 ID -> 图书 ID 的映射
        Map<Integer, Integer> bookCopyIdToBookId = bookCopies.stream().collect(Collectors.toMap(BookCopy::getId, BookCopy::getBookId));

        // 构造历史记录
        return borrowRecords.stream().map(borrowRecord -> {
            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setId(borrowRecord.getId());
            historyDTO.setBookCopyId(borrowRecord.getBookCopyId());
            historyDTO.setBookId(bookCopyIdToBookId.get(borrowRecord.getBookCopyId()));
            historyDTO.setBorrowTime(borrowRecord.getCreateTime());
            historyDTO.setReturnTime(borrowRecord.getCloseTime());
            historyDTO.setCloseStatus(borrowRecord.getCloseStatus());
            historyDTO.setIsRenewed(borrowRecord.getIsRenewed());
            historyDTO.setDueTime(borrowRecord.getDueTime());
            return historyDTO;
        }).toList();
    }

}
