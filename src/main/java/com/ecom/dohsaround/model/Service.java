package com.ecom.dohsaround.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long id;
    private String serviceName;
    private String description;
    private int serviceFee;
    @ManyToOne
    @JoinColumn(name = "shop")
    private Shop shop;
    private boolean active;
}
