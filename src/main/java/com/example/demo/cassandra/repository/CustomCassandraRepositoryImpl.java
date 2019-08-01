package com.example.demo.cassandra.repository;

import com.example.demo.cassandra.model.Book;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.core.mapping.BasicCassandraPersistentEntity;
import org.springframework.data.cassandra.core.mapping.CassandraPersistentProperty;
import org.springframework.data.cassandra.repository.query.CassandraEntityInformation;
import org.springframework.data.cassandra.repository.support.SimpleCassandraRepository;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class CustomCassandraRepositoryImpl<T, ID> extends SimpleCassandraRepository<T, ID> implements CustomCassandraRepository<T,ID> {

    private final InsertOptions INSERT_NULLS = InsertOptions.builder().withInsertNulls().build();

    private final AbstractMappingContext<BasicCassandraPersistentEntity<?>, CassandraPersistentProperty> mappingContext;

    private final CassandraEntityInformation<T, ID> entityInformation;

    private final CassandraOperations operations;


    public CustomCassandraRepositoryImpl(CassandraEntityInformation<T, ID> metadata, CassandraOperations operations) {
        super(metadata, operations);

        this.entityInformation = metadata;
        this.operations = operations;
        this.mappingContext = operations.getConverter().getMappingContext();
    }

    @Override
    public <S extends T> S save(S entity, int ttl) {

        Assert.notNull(entity, "Entity must not be null");

        InsertOptions insertOptions = InsertOptions.builder().ttl(ttl).build();

        BasicCassandraPersistentEntity<?> persistentEntity = this.mappingContext.getPersistentEntity(entity.getClass());

        if (persistentEntity != null && persistentEntity.hasVersionProperty()) {

            if (!entityInformation.isNew(entity)) {
                return this.operations.update(entity);
            }
        }

        if(ttl > 0 ) {
            return operations.insert(entity, insertOptions).getEntity();
        } else {
            return operations.insert(entity, INSERT_NULLS).getEntity();
        }
    }

    @Override
    public <S extends T> List<S> insert(Iterable<S> entities, int ttl) {
        Assert.notNull(entities, "The given Iterable of entities must not be null");

        InsertOptions insertOptions = InsertOptions.builder().ttl(ttl).build();
        List<S> result = new ArrayList<>();

        if(ttl > 0 ) {
            for (S entity : entities) {
                result.add(operations.insert(entity, insertOptions).getEntity());
            }
            return result;
        } else {
            for (S entity : entities) {
                result.add(operations.insert(entity, INSERT_NULLS).getEntity());
            }
            return result;
        }
    }

    @Override
    public List<String> findDistinctCategory() {
        return operations.select("SELECT DISTINCT category FROM book", String.class);
    }

}
