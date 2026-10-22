
package com.api.kdevfolio.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 180)
    private String slug;

    @Column(nullable = false)
    private Integer readingTimeMinutes;

    @Column(nullable = false, length = 150)
    private String authorName;

    @ManyToMany
    @JoinTable(
            name = "article_categories",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(
            mappedBy = "article",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ArticleContent> translations = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void addTranslation(ArticleContent translation) {
        translations.add(translation);
        translation.setArticle(this);
    }

    public void removeTranslation(ArticleContent translation) {
        translations.remove(translation);
        translation.setArticle(null);
    }
}
