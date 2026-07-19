package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer reviewId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CarRenID", nullable = false)
    private CarRental carRental;

    @Column(name = "ReviewStar", nullable = false)
    private Integer reviewStar;

    @Column(name = "Comment", columnDefinition = "NVARCHAR(500)", nullable = false)
    private String comment;

}
