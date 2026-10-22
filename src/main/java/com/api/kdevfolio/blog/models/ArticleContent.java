
package com.api.kdevfolio.blog.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "article_contents",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_article_content_language",
                        columnNames = {"article_id", "language"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ArticleContent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private ArticleLanguage language;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 2048)
    private String imageUrl;
}
