package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

@Data
public class BookDTO {

    @NotNull(message = "ISBN不能为空")
    @NotBlank(message = "ISBN不能为空")
    private String isbn;

    @NotNull(message = "分类不能为空")
    private Integer categoryId;

    @NotNull(message = "书名不能为空")
    @NotBlank(message = "书名不能为空")
    private String title;

    private String subtitle;

    @NotNull(message = "作者不能为空")
    @NotBlank(message = "作者不能为空")
    private String author;

    @NotNull(message = "出版社不能为空")
    @NotBlank(message = "出版社不能为空")
    private String publisher;

    @NotNull(message = "出版日期不能为空")
    private LocalDate publishDate;

    @NotNull(message = "版次不能为空")
    @NotBlank(message = "版次不能为空")
    private String edition;

    @NotNull(message = "页数不能为空")
    private Integer pageCount;

    @NotNull(message = "语言不能为空")
    private Locale language;

    @NotNull(message = "描述不能为空")
    @NotBlank(message = "描述不能为空")
    private String description;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

}
