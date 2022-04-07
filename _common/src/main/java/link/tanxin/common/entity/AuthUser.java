package link.tanxin.common.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author tan
 */
@Entity
@Table(name = "user", schema = "auth")
public class AuthUser {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "nick_name")
    private String nickName;
    @Basic
    @Column(name = "sex")
    private Boolean sex;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getSex() {
        return sex;
    }


    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthUser authUser = (AuthUser) o;
        return id == authUser.id && Objects.equals(username, authUser.username) && Objects.equals(password, authUser.password) && Objects.equals(nickName, authUser.nickName) && Objects.equals(sex, authUser.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, nickName, sex);
    }
}
