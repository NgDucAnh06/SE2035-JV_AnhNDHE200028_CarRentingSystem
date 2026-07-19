package org.crs.se2035jv_anhndhe200028_carrentingsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CarProducer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarProducer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProducerID")
    private Integer producerID;

    @Column(name = "ProducerName", columnDefinition = "NVARCHAR(100)", nullable = false)
    @NotBlank(message = "Producer name is required!")
    @Size(max = 100, message = "Producer name must not exceed 100 characters!")
    private String producerName;

    @Column(name = "Address", columnDefinition = "NVARCHAR(200)", nullable = false)
    @NotBlank(message = "Address is required!")
    @Size(max = 200, message = "Address must not exceed 200 characters!")
    private String address;

    @Column(name = "Country", columnDefinition = "NVARCHAR(100)", nullable = false)
    @NotBlank(message = "Country is required!")
    @Size(max = 100, message = "Country must not exceed 100 characters!")
    private String country;

    @OneToMany(mappedBy = "producer")
    private List<Car> carList;
}
