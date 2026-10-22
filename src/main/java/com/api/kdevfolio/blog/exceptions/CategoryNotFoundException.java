package com.api.kdevfolio.blog.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String message) {
        super(
                message != null && !message.isBlank()
                        ? message
                        : "Category not found"
        );
    }
}