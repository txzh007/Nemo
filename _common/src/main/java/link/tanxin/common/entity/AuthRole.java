package link.tanxin.common.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


/**
 * @author tan
 */
@Data
@Table("role")
public class AuthRole {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column("id")
    private Long id;

    private String name;

    private String code;


}
