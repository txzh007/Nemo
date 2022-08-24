package link.tanxin.common.entity.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author tan
 */

@Data
@Table("user")
public class User {

    @Id
    @Column("id")
    private Long id;

    private String username;

    private String password;

    private String nickName;

    private Boolean sex;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer status;


}
