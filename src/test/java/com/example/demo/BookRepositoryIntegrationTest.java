package com.example.demo;


import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.example.demo.cassandra.config.CassandraConfiguration;
import com.example.demo.cassandra.model.Book;
import com.example.demo.cassandra.model.BookCategory;
import com.example.demo.cassandra.model.BookKey;
import com.example.demo.cassandra.repository.BookRepository;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(classes = {CassandraConfiguration.class})
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ComponentScan("com.example.demo")
@EnableAutoConfiguration
public class BookRepositoryIntegrationTest {

    @Autowired
    private Session session;

    @Autowired
    private BookRepository repository;

    @Value("${cassandra.keyspace}")
    private String cassandraKeyspace;

    @AfterEach
    public void clearContent(){
        repository.deleteAll();
    }

    @Test
    public void getConnectedKeySpace() throws FileNotFoundException {
        assertNotNull(cassandraKeyspace);
        assertNotNull(session);
        assertEquals(cassandraKeyspace, session.getLoggedKeyspace());
    }

    @Test
    public void saveBookTest() {

        BookKey key = new BookKey(UUIDs.timeBased(), "Programming", "Head First Java", "O'Reilly Media");
        Book javaBook = new Book(key, ImmutableSet.of("Kathy Sierra", "Bert Bates"), 2008, ImmutableSet.of("Computer", "Software"));

        javaBook = repository.save(javaBook, 600);

        assertEquals(javaBook.getKey().getTitle(), "Head First Java");

        Optional<Book> aBook = repository.findById(key);

        assertEquals(aBook.get().getKey().getPublisher(), "O'Reilly Media");
    }

    @Test
    public void distinctTest() {

        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(new BookKey(UUIDs.timeBased(), "Computer Science", "Cracking the Coding Interview: 189 Programming Questions and Solutions 6th Edition", "CareerCup"), ImmutableSet.of("Gayle Laakmann McDowell"), 2015, ImmutableSet.of("Computer", "Software", "Coding", "Interview")));
        bookList.add(new Book(new BookKey(UUIDs.timeBased(), "Programming", "Automate the Boring Stuff with Python: Practical Programming for Total Beginners", "No Starch Press"), ImmutableSet.of("Al Sweigart"), 2015, ImmutableSet.of("Computer", "Software", "Automation", "Python")));
        bookList.add(new Book(new BookKey(UUIDs.timeBased(), "Software Testing", "Clean Code: A Handbook of Agile Software Craftsmanship", "No Starch Press"), ImmutableSet.of("Robert C. Martin"), 2015, ImmutableSet.of("Computer", "Software", "Automation", "Python")));
        bookList.add(new Book(new BookKey(UUIDs.timeBased(), "Machine Theory", "The Hundred-Page Machine Learning Book", "Andriy Burkov"), ImmutableSet.of("Andriy Burkov"), 2019, ImmutableSet.of("Computer", "Machine")));
        bookList.add(new Book(new BookKey(UUIDs.timeBased(), "Machine Theory", "Code: The Hidden Language of Computer Hardware and Software (Developer Best Practices)", "Microsoft Press"), ImmutableSet.of("Charles Petzold"), 2000, ImmutableSet.of("Computer", "Code", "Machine", "Theory")));

        bookList = repository.insert(bookList);
        Assert.assertEquals(5,repository.count());

        List<String> categories = repository.findDistinctCategory();
        Assert.assertEquals(4,categories.size());

        categories.forEach(System.out::println);

    }
}
