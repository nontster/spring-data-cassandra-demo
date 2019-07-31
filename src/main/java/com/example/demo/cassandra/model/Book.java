package com.example.demo.cassandra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Table
public class Book {
    @PrimaryKey
    private BookKey key;

    @Column
    public Set<String> authors;

    @Column
    public int year;

    @Column
    private Set<String> tags = new HashSet<>();
}
