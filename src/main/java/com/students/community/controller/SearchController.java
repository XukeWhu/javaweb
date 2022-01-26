package com.students.community.controller;

import com.students.community.entity.DiscussPost;
import com.students.community.entity.Page;
import com.students.community.service.ElasticSearchService;
import com.students.community.service.LikeService;
import com.students.community.service.UserService;
import com.students.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {
    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=xxx
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search(String keyword, Page page, Model model){
        // 搜索帖子
        Map<String, Object> result = elasticSearchService.searchDiscussPost(keyword, page.getCurrent()-1, page.getLimit());
        List<DiscussPost> discussPostList = (List<DiscussPost>) result.get("discussPosts");

        // 聚合数据
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if(discussPostList!=null){
            for(DiscussPost post:discussPostList){
                Map<String, Object> map = new HashMap<>();
                // 帖子
                map.put("post", post);
                // 作者
                map.put("user", userService.findUserById(post.getUserId()));
                // 点赞的数量
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        model.addAttribute("keyword",keyword);

        // 分页信息
        page.setPath("/search?keyword="+keyword);
        page.setRows((Integer)result.get("rows"));

        return "/site/search";
    }
}
