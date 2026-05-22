package com.yomu.gamification.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reading_catalog_projection", schema = "gamification")
public class ReadingCatalogProjection {

    @Id
    @Column(name = "reading_id")
    private UUID readingId;

    @Column(length = 255)
    private String title;

    @Column(name = "category_name", length = 100)
    private String categoryName;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public UUID getReadingId() { return readingId; }
    public void setReadingId(UUID readingId) { this.readingId = readingId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Boolean getDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}