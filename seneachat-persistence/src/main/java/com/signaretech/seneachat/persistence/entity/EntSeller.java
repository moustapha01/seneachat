package com.signaretech.seneachat.persistence.entity;

import com.google.common.base.MoreObjects;
import com.signaretech.seneachat.common.validation.Patterns;
import com.signaretech.seneachat.common.validation.ValidPhone;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "seller")
public class EntSeller extends AuditableEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "seller_uuid", nullable = false, updatable = false)
    private UUID id;

    @Basic
    //@NotBlank(message = "first name cannot be null or empty")
    @Column( name = "first_name")
    private String firstName;

    @Basic
    //@NotBlank(message = "last name cannot be null or empty")
    @Column( name = "last_name")
    private String lastName;

    @Basic
    @NotBlank(message = "email cannot be null or empty")
    @Column( name = "email", unique = true)
    private String email;

    @Transient
    private String password;

    @Transient
    private String password2;

    @Basic
    @ValidPhone(pattern = "\\d{9,10}")
    @Column( name = "home_phone", length = 12)
    private String homePhone;

    @Basic
    @ValidPhone(pattern = "\\d{9,10}")
    @Column( name = "cell_phone", length = 12)
    private String cellPhone;

    @Column( name = "status", nullable = false, length = 1)
    private String status;

    @Basic
    @Column( name = "activation_code", nullable = false, length = 6)
    private String activationCode;

    @Basic
    @Column( name = "secret", nullable = false)
    private String secret;

    @OneToMany(mappedBy = "seller", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EntAdvertisement> ads;

    public EntSeller() {
    }

    public EntSeller(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
        EntSeller entSeller = (EntSeller) o;
        return Objects.equals(id, entSeller.id);
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
                .add("email", email)
                .add("status", status).toString();
    }
}

