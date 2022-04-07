package link.tanxin.common.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author tan
 */
@Entity
@Table(name = "role", schema = "auth")
public class AuthRole {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "code")
    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthRole authRole = (AuthRole) o;
        return id == authRole.id && Objects.equals(name, authRole.name) && Objects.equals(code, authRole.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code);
    }
}
