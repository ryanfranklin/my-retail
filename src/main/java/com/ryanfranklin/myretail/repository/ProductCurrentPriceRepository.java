package com.ryanfranklin.myretail.repository;

import com.ryanfranklin.myretail.model.Product;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductCurrentPriceRepository extends MongoRepository<Product.CurrentPrice, String> {

}
