package io.github.xnovo3000.eventus.bean.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
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
    private List<Participation> holdings = List.of();

}