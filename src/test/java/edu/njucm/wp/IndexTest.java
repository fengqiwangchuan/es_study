package edu.njucm.wp;


import edu.njucm.wp.entity.Item;
import edu.njucm.wp.mapper.ItemRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Bootstrap.class)
public class IndexTest {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void create() {
        elasticsearchTemplate.createIndex(Item.class);
        elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void delete() {
        elasticsearchTemplate.deleteIndex("wp2");
    }

    @Test
    public void add() {
        Item item = new Item(1L, "测试数据item", "测试", "数据", 3333.00, "uri_test");
        itemRepository.save(item);
    }

    @Test
    public void testList() {
        List<Item> list = new ArrayList<Item>() {
            {
                this.add(new Item(2L, "测试数据item2", "测试", "数据", 2333.00, "uri_test2"));
                this.add(new Item(3L, "测试数据item3", "测试", "数据", 3133.00, "uri_test3"));
            }
        };
        itemRepository.saveAll(list);
    }

    @Test
    public void find() {
        Iterable<Item> items = itemRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        items.forEach(e -> System.out.println(e));
    }

    @Test
    public void testQuery() {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "数据");
        Iterable<Item> items = itemRepository.search(queryBuilder);
        items.forEach(System.out::println);
    }

    @Test
    public void testNativeQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "测试"));

        //分页
        int page = 0;
        int size = 2;
        queryBuilder.withPageable(PageRequest.of(page, size));

        Page<Item> items = itemRepository.search(queryBuilder.build());
        System.out.println(items.getTotalElements());
        System.out.println(items.getTotalPages());
        System.out.println(items.getSize());
        System.out.println(items.getNumber());
        items.forEach(System.out::println);
    }

    @Test
    public void testSort() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.termQuery("category", "测试"));
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        Page<Item> items = itemRepository.search(queryBuilder.build());
        System.out.println(items.getTotalElements());
        items.forEach(System.out::println);
    }

    @Test
    public void testAgg() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //不查询任何 size=0
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        //聚合类型terms 聚合名称brands 聚合字段 brand
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) itemRepository.search(queryBuilder.build());
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());
        }
    }

    @Test
    public void testSubAgg() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""}, null));
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand").subAggregation(AggregationBuilders.avg("priceAvg").field("price")));
        AggregatedPage<Item> aggPage = (AggregatedPage<Item>) itemRepository.search(queryBuilder.build());
        StringTerms agg = (StringTerms) aggPage.getAggregation("brands");
        List<StringTerms.Bucket> buckets = agg.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString() + ",共" + bucket.getDocCount());
            InternalAvg avg = (InternalAvg) bucket.getAggregations().asMap().get("priceAvg");
            System.out.println("平均售价: " + avg.getValue());
        }
    }
}
