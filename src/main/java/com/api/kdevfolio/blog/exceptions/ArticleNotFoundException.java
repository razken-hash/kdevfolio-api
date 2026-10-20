package com.api.kdevfolio.blog.exceptions;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String message) {
        super(
                message != null && !message.isBlank()
                        ? message
                        : "Article not found"
        );
    }
}