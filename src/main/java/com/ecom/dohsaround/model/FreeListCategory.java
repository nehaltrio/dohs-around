package com.ecom.dohsaround.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "freelist_categories", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class FreeListCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String name;
    private boolean is_deleted;
    private boolean is_activated;

    public FreeListCategory(String name){
        this.name = name;
        this.is_activated = true;
        this.is_deleted = false;
    }

}
