package pers.liaohaolong.mokulibserver.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

public class ReturnStatusValidator implements ConstraintValidator<ValidReturnStatus, BorrowRecord.Status> {

    @Override
    public boolean isValid(BorrowRecord.Status value, ConstraintValidatorContext context) {
        if (value == null)
            return false;

        // 不允许 BORROWING 状态，BORROWING 不是一个归还状态，RETURNED、LOST、DAMAGED 是归还状态
        return value != BorrowRecord.Status.BORROWING;
    }

}
