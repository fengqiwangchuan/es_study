package edu.njucm.wp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "item", type = "docs", shards = 1, replicas = 0)
public class Item {
    @Id
    Long id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    String title;
    @Field(type = FieldType.Keyword)
    String category;
    @Field(type = FieldType.Keyword)
    String brand;
    @Field(type = FieldType.Double)
    Double price;
    @Field(index = false, type = FieldType.Keyword)
    String images;
}
