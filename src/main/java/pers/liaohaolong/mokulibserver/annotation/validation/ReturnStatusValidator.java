package pers.liaohaolong.mokulibserver.annotation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pers.liaohaolong.mokulibserver.model.BorrowRecord;

public class ReturnStatusValidator implements ConstraintValidator<ValidReturnStatus, BorrowRecord.CloseStatus> {

    @Override
    public boolean isValid(BorrowRecord.CloseStatus value, ConstraintValidatorContext context) {
        if (value == null)
            return false;

        // 不允许 OPEN 状态
        return value != BorrowRecord.CloseStatus.OPEN;
    }

}
