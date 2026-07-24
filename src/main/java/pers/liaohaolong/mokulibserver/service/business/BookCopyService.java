package pers.liaohaolong.mokulibserver.service.business;

import pers.liaohaolong.mokulibserver.dto.request.BookCopyDTO;
import pers.liaohaolong.mokulibserver.model.BookCopy;

public interface BookCopyService {

    BookCopy add(Integer entryBy, BookCopyDTO bookCopyDTO);

}
