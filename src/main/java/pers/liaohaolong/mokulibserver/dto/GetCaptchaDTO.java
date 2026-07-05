package pers.liaohaolong.mokulibserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCaptchaDTO {

    private String token;

    private byte[] image;

}
