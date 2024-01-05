package com.epam.training.ticketservice.lib.user.persistence.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//TODO make sure the uid sequence is shared
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity //TODO needs this, but creates extraneous parent table?
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(
        uniqueConstraints = {
                // The naming is kind of funky; the columns joined/used from other tables use the database
                // naming strategy but the columns used from this entity use the java-side names
                @UniqueConstraint(name = "unique_uname", columnNames = {"username"})
        })
public class UserBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="application_uid_seq")
    @SequenceGenerator(name="application_uid_seq", sequenceName="application_uid_seq", allocationSize=1)
    public Long uid;

    @Column(nullable = false)
    public String username;

    @Column(nullable = false)
    public String password;
}
