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
@Table("user_roles_ relation")
public class AuthUserRolesRelation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column("id")
    private Long id;

    private Long userId;

    private Long roleId;


}
