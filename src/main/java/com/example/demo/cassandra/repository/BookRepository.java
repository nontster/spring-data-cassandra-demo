package com.example.demo.cassandra.repository;

import com.example.demo.cassandra.model.Book;
import com.example.demo.cassandra.model.BookCategory;
import com.example.demo.cassandra.model.BookKey;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CustomCassandraRepository<Book, BookKey> {
    List<String> findDistinctCategory();
}
