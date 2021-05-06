package maciej.grochowski.userloginregistration.registration;

import lombok.AllArgsConstructor;
import maciej.grochowski.userloginregistration.user.User;
import maciej.grochowski.userloginregistration.user.UserRole;
import maciej.grochowski.userloginregistration.user.UserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final EmailValidator emailValidator;
    // both instances are used by the constructor

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
}