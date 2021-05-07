package maciej.grochowski.userloginregistration.registration.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import maciej.grochowski.userloginregistration.user.User;


import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "confirmation_token_sequence")
    @SequenceGenerator(sequenceName = "confirmation_token_sequence", name = "confirmation_token_sequence", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime creationTime;

    @Column(nullable = false)
    private LocalDateTime expirationTime;

    private LocalDateTime confirmationTime;

    @ManyToOne  // user can have many confirmationTokens
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfToken(String token, LocalDateTime creationTime, LocalDateTime expirationTime, User user) {
        this.token = token;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.user = user;
    }
}
