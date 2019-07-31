package com.example.demo.cassandra.repository;

import com.example.demo.cassandra.model.Book;
import com.example.demo.cassandra.model.BookKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CustomCassandraRepository <Book, BookKey>{
}
