package link.tanxin.common.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * @author tan
 */

@Data
@Table("user")
public class AuthUser {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private Boolean sex;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer status;


}
