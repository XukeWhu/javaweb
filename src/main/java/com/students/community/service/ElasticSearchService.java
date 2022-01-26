package com.students.community.service;

import com.students.community.dao.elasticsearch.DiscussPostRepository;
import com.students.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Autowired
    private DiscussPostRepository discussRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void saveDiscussPost(DiscussPost discussPost) {
        discussRepository.save(discussPost);
    }

    public void deleteDiscussPost(int id) {
        discussRepository.deleteById(id);
    }

    public Map<String, Object> searchDiscussPost(String keywords, int current, int limit) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keywords, "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                )
                .build();
        SearchHits<DiscussPost> hits = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        Map<String, Object> map = new HashMap<>();
        map.put("rows", (int)hits.getTotalHits());
        if (hits.getTotalHits() <= 0) return map;
        List<DiscussPost> discussPosts = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : hits) {
            Map<String, List<String>> highLightFields = hit.getHighlightFields();
            // 将高亮的内容填充到content中
            hit.getContent().setTitle(highLightFields.get("title") == null ? hit.getContent().getTitle() : highLightFields.get("title").get(0));
            hit.getContent().setContent(highLightFields.get("content") == null ? hit.getContent().getContent() : highLightFields.get("content").get(0));
            DiscussPost post = hit.getContent();
            discussPosts.add(post);
        }
        map.put("discussPosts", discussPosts);
        return map;
    }
}
