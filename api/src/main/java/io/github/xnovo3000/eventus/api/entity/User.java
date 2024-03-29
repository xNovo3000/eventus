package io.github.xnovo3000.eventus.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 'User' table
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "_user")  // '_user' because on some databases 'user' is a reserved keyword
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Authority> authorities = List.of();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Event> events = List.of();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Subscription> holdings = List.of();

}