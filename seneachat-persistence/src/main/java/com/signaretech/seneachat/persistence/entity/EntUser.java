package com.signaretech.seneachat.persistence.entity;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "users")
public class EntUser extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID id;

    @Basic
    @NotBlank(message = "{firstname.notblank}")
    @Column( name = "first_name")
    private String firstName;

    @Basic
    @NotBlank(message = "{lastname.notblank}")
    @Column( name = "last_name")
    private String lastName;

    @Basic
    @NotBlank(message = "{email.notblank}")
    @Column( name = "email", unique = true)
    private String username;

    @NotBlank(message = "{password.notblank}")
/*    @Length(min = 8, max = 16, message = "{password.invalid}")*/
    @Column( name = "secret")
    private String password;

    @Transient
/*    @NotBlank(message = "{password.notblank}")
    @Length(min = 8, max = 16, message = "{password.invalid}")*/
    private String password2;

    @Basic
/*    @ValidPhone(pattern = "\\d{9,10}")*/
    @Column( name = "home_phone", length = 12)
    private String homePhone;

    @Basic
 /*   @ValidPhone(pattern = "\\d{9,10}")*/
    @Column( name = "cell_phone", length = 12)
    private String cellPhone;

    @Column( name = "status", nullable = false, length = 1)
    private String status;

    @Basic
    @Column( name = "activation_code", nullable = false, length = 6)
    private String activationCode;

    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EntAdvertisement> ads;

    public EntUser() {
    }

    public EntUser(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
        if(homePhone == null) {
            homePhone = cellPhone;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public List<EntAdvertisement> getAds() {
        return ads;
    }

    public void setAds(List<EntAdvertisement> ads) {
        this.ads = ads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntUser entUser = (EntUser) o;
        return Objects.equals(id, entUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {

        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("username", username)
                .add("status", status).toString();
    }
}

