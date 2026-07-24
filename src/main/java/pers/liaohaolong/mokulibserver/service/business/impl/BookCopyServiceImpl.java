package pers.liaohaolong.mokulibserver.service.business.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.liaohaolong.mokulibserver.dao.BookCopyMapper;
import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.model.BookCopy;
import pers.liaohaolong.mokulibserver.service.business.BookCopyService;

@Slf4j
@Service
@AllArgsConstructor
public class BookCopyServiceImpl implements BookCopyService {

    private final BookCopyMapper bookCopyMapper;

    @Override
    @Transactional
    public BookCopy add(Integer entryBy, BookCopyDTO bookCopyDTO) {
        BookCopy bookCopy = BookCopy.fromDTO(bookCopyDTO);

        bookCopy.setEntryBy(entryBy);

        bookCopyMapper.insert(bookCopy);

        return bookCopyMapper.selectById(bookCopy.getId());
    }

}
