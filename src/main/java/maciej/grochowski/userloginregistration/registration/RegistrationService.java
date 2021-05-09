package maciej.grochowski.userloginregistration.registration;

import lombok.AllArgsConstructor;
import maciej.grochowski.userloginregistration.registration.token.ConfToken;
import maciej.grochowski.userloginregistration.registration.token.ConfTokenService;
import maciej.grochowski.userloginregistration.user.User;
import maciej.grochowski.userloginregistration.user.UserRole;
import maciej.grochowski.userloginregistration.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    private final ConfTokenService confTokenService;
    // these instances are used by the constructor

    @Transactional
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException();
        }
        return userService.signUpUser(
                new User(request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.ADMIN
                )
        );
    }

    @Transactional
    public String confirmToken(String token) {
        ConfToken confToken = confTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confToken.getConfirmationTime() != null) {
            throw new IllegalStateException("Email already confirmed");
        }

        LocalDateTime expirationTime = confToken.getExpirationTime();

        if (expirationTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token has expired");
        }

        confTokenService.setConfirmedAt(token);
        userService.enableUser(confToken.getUser().getEmail());
        return "Token confirmed";
    }
}