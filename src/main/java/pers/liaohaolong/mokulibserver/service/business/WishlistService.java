package pers.liaohaolong.mokulibserver.service.business;

public interface WishlistService {

    void add(Integer userId, Integer bookId);

    void delete(Integer userId, Integer bookId);

    boolean isInWishlist(Integer userId, Integer bookId);

}
