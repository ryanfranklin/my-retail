package com.ryanfranklin.myretail.repository;

import com.ryanfranklin.myretail.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends MongoDbRepository<Product.CurrentPrice, String> {
    @Override
    Product.CurrentPrice findOne(String id);

    @Override
    void delete(Product.CurrentPrice deleted);
}
