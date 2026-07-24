package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddCategoryDTO {

    @NotNull(message = "分类名称不能为空")
    @NotBlank(message = "分类名称不能为空")
    private String name;

}
