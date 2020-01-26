package pro.antonshu.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pro.antonshu.market.entities.PasswordResetToken;
import pro.antonshu.market.entities.User;
import pro.antonshu.market.repositories.PasswordTokenRepository;

import java.util.Arrays;
import java.util.Calendar;

@Service
public class SecurityService {

    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    public void setPasswordTokenRepository(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    public String validatePasswordResetToken(long id, String token) {
        PasswordResetToken passToken =
                passwordTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
                .getId() != id)) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "tokenExpired";
        }

        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, Arrays.asList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }
}
