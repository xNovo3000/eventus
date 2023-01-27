package io.github.xnovo3000.eventus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2048)
    private String description;

    @CreatedBy
    @ManyToOne
    @JoinColumn(nullable = false)
    private User creator;

    @Column(nullable = false)
    private OffsetDateTime start;

    @Column(nullable = false)
    private OffsetDateTime end;

    @Column(nullable = false)
    private Boolean approved;

}