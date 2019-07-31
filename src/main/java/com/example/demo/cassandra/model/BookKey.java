package com.example.demo.cassandra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@PrimaryKeyClass
public class BookKey {
    @PrimaryKeyColumn(name = "isbn", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private UUID id;

    @PrimaryKeyColumn(name = "title", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String title;

    @PrimaryKeyColumn(name = "publisher", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String publisher;
}
