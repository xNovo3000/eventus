package io.github.xnovo3000.eventus.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * 'Event table'
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = false)
    private OffsetDateTime creationDate;

    @Column(nullable = false)
    private OffsetDateTime start;

    @Column(name = "_end", nullable = false)  // '_end' because on some databases 'end' is a reserved keyword
    private OffsetDateTime end;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Boolean approved;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Subscription> holdings = List.of();

}