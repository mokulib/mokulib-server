package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookCopyDTO {

    @NotNull(message = "图书 ID 不能为空")
    private Integer bookId;

    @NotNull(message = "采购价格不能为空")
    private BigDecimal purchasePrice;

    @NotNull(message = "采购日期不能为空")
    private LocalDate purchaseDate;

    @NotNull(message = "来源不能为空")
    @NotBlank(message = "来源不能为空")
    private String source;

}
