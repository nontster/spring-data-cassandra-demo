package com.example.demo;


import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.example.demo.cassandra.config.CassandraConfiguration;
import com.example.demo.cassandra.model.Book;
import com.example.demo.cassandra.model.BookKey;
import com.example.demo.cassandra.repository.BookRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.io.FileNotFoundException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes={CassandraConfiguration.class})
@TestPropertySource(locations={"classpath:application-test.properties"})
@ComponentScan("com.example.demo")
@EnableAutoConfiguration
public class BookRepositoryIntegrationTest {

    @Autowired
    private Session session;

    @Autowired
    private BookRepository repository;

    @Value("${cassandra.keyspace}")
    private String cassandraKeyspace;

    @Test
    public void getConnectedKeySpace() throws FileNotFoundException {
        assertNotNull(cassandraKeyspace);
        assertNotNull(session);
        assertEquals(cassandraKeyspace, session.getLoggedKeyspace());
    }

    @Test
    public void saveBookTest(){

        BookKey key = new BookKey( UUIDs.timeBased(), "Head First Java", "O'Reilly Media");
        Book javaBook = new Book(key, ImmutableSet.of("Kathy Sierra","Bert Bates"),2008, ImmutableSet.of("Computer", "Software"));

        javaBook = repository.save(javaBook, 600);

        assertEquals(javaBook.getKey().getTitle(), "Head First Java");

        Optional<Book> aBook = repository.findById(key);

        assertEquals(aBook.get().getKey().getPublisher(), "O'Reilly Media");
    }
}
