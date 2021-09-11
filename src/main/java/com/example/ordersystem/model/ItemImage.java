package com.example.ordersystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * This class is used for storing images of Items
 * They are linked to the Item table by ID and will be removed if the item they are linked to gets removed.
 */
@Entity
@Table(name="itemimages")
public class ItemImage {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference(value = "image-item")
    @JoinColumn(name = "item_id")
    private Item item;

    @Lob
    @Column
    private byte[] image;

    public ItemImage(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
