package pers.liaohaolong.mokulibserver.dto.request;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SortModeDTO {

    PUBLISH_DATE_FROM_NEW_TO_OLD("PUBLISH_DATE_FROM_NEW_TO_OLD", "出版日期从新到旧"),
    PUBLISH_DATE_FROM_OLD_TO_NEW("PUBLISH_DATE_FROM_OLD_TO_NEW", "出版日期从旧到新"),
    PRICE_FROM_LOW_TO_HIGH("PRICE_FROM_LOW_TO_HIGH", "价格从低到高"),
    PRICE_FROM_HIGH_TO_LOW("PRICE_FROM_HIGH_TO_LOW", "价格从高到低");

    @EnumValue
    private final String code;

    private final String desc;

    SortModeDTO(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
