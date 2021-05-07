package maciej.grochowski.userloginregistration.user;

import lombok.AllArgsConstructor;
import maciej.grochowski.userloginregistration.registration.token.ConfToken;
import maciej.grochowski.userloginregistration.registration.token.ConfTokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "User with email %s was not found.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfTokenService confTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(User user) {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (userExists) {
            throw new IllegalStateException("This email is already registered. Please, try another one.");
        }

        String encodedPass = bCryptPasswordEncoder.encode(user.getPassword()); // make the password secure before signing it in the db
        user.setPassword(encodedPass);
        userRepository.save(user);


        String token = UUID.randomUUID().toString();
        ConfToken confToken = new ConfToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        // TODO: send email

        confTokenService.saveConfToken(confToken);

        return token;
    }
}
