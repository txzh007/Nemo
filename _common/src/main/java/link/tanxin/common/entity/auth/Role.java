package link.tanxin.common.entity.auth;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.Id;


/**
 * @author tan
 */
@Data
@Table("role")
public class Role {
    @Id
    @Column("id")
    private Long id;

    private String name;

    private String code;


}
