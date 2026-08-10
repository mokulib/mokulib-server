package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddCategoryDTO {

    @NotBlank(message = "分类名称不能为空")
    private String name;

}
