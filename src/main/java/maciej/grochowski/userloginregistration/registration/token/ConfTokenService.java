package maciej.grochowski.userloginregistration.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfTokenService {

    private final ConfTokenRepository confTokenRepository;

    public void saveConfToken(ConfToken token){
        confTokenRepository.save(token);
    }
}
