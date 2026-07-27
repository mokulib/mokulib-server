package pers.liaohaolong.mokulibserver.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDTO {

    @NotNull(message = "用户 ID 不能为空")
    private Integer userId;

    @NotNull(message = "是否续借不能为空")
    private Boolean isRenewed;

}
