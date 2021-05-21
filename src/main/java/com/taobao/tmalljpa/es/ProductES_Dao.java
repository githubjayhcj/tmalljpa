package com.taobao.tmalljpa.es;

import com.taobao.tmalljpa.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductES_Dao extends ElasticsearchRepository<Product,Integer> {

}
