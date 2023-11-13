package com.ecom.dohsaround.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    private boolean is_deleted;
    private boolean is_activated;
    @ManyToOne
    @JoinColumn(name = "shop")
    private Shop shop;

    public Category(String name, Shop shop){
        this.name = name;
        this.is_activated = true;
        this.is_deleted = false;
        this.shop = shop;
    }

}
