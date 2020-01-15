package edu.njucm.wp.mapper;

import edu.njucm.wp.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends ElasticsearchRepository<Item, Long> {
    /**
     * 价格区间查询
     * @param min
     * @param max
     * @return
     */
    List<Item> findByPriceBetween(double min, double max);
}
