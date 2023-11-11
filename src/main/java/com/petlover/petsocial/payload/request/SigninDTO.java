package com.petlover.petsocial.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SigninDTO {
    private String email;
    private String password;

    public static SigninDTO fromOAuth2User(OAuth2User oAuth2User) {
        return new SigninDTO(
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("email")
        );
    }
}
