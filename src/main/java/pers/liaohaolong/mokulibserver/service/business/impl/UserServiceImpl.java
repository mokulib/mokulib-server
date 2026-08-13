package pers.liaohaolong.mokulibserver.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.dao.*;
import pers.liaohaolong.mokulibserver.dto.response.BorrowingDTO;
import pers.liaohaolong.mokulibserver.dto.response.NonsensitiveUserDTO;
import pers.liaohaolong.mokulibserver.dto.response.UsernameDTO;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.model.*;
import pers.liaohaolong.mokulibserver.service.base.ImageService;
import pers.liaohaolong.mokulibserver.service.business.UserService;

import java.util.List;

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
    public void uploadAvatar(Integer id, byte[] avatar) throws BusinessException {
        imageService.save(ImageConfigurations.ImageType.AVATARS, String.valueOf(id), avatar);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsernameDTO> getUsernames(@NonNull List<Integer> ids) throws BusinessException {
        return listByIds(ids.stream().distinct().toList()).stream().map(user -> new UsernameDTO(user.getId(), user.getUsername())).toList();
    }

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
    @Transactional(readOnly = true)
    public BorrowingDTO getBorrowing(@NonNull Integer id) {
        // 查询借阅记录
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectList(new LambdaQueryWrapper<BorrowRecord>()
                .eq(BorrowRecord::getUserId, id)
                .eq(BorrowRecord::getCloseStatus, BorrowRecord.CloseStatus.OPEN)
        );

        if (borrowRecords.isEmpty())
            return BorrowingDTO.EMPTY;

        // 待查询的馆藏 ID 列表
        List<Integer> bookCopyIds = borrowRecords.stream().map(BorrowRecord::getBookCopyId).distinct().toList();
        // 批量查询
        List<BookCopy> bookCopies = bookCopyMapper.selectByIds(bookCopyIds);

        // 待查询的图书 ID 列表
        List<Integer> bookIds = bookCopies.stream().map(BookCopy::getBookId).distinct().toList();
        // 批量查询
        List<Book> books = bookMapper.selectByIds(bookIds);

        // 构造 BorrowRecordWithBookId 列表
        List<BorrowingDTO.BorrowRecordWithBookId> borrowRecordsWithBookIds = borrowRecords.stream().map(borrowRecord ->
            BorrowingDTO.BorrowRecordWithBookId.of(
                    borrowRecord,
                    bookCopies.stream()
                            .filter(bookCopy -> bookCopy.getId().equals(borrowRecord.getBookCopyId()))
                            .findFirst()
                            .orElseThrow()
                            .getBookId()
            )
        ).toList();

        // 构造 BorrowingDTO
        BorrowingDTO borrowingDTO = new BorrowingDTO();
        borrowingDTO.setBooks(books);
        borrowingDTO.setBorrowRecords(borrowRecordsWithBookIds);
        return borrowingDTO;
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

}
