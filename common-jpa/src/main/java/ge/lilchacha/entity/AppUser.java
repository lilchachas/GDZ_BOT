package ge.lilchacha.entity;

import ge.lilchacha.entity.enums.UserView;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ge.lilchacha.entity.enums.UserState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String  firstName;
    private String  lastName;
    private String  username;
    private String profileById;//строка в которой первые два символа это класс шкилы потом, через разделитель проставлены предметы прим: 11MAT-RUS-ENG-HIS- и тп
    private Boolean isActive;  //cтатус который меняется в микросервисе капчи
    @Enumerated(EnumType.STRING)
    private UserState state;
    @Enumerated(EnumType.STRING)
    private UserView view;// возможные вьюшки
}
