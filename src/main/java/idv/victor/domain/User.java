package idv.victor.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {
    @Id
    private String userKey;

    private String userid;
    private String password;
    private boolean isEnable;
    private String email;
    private String priv;
    private String phoneNumber;
    private String idno;

}
