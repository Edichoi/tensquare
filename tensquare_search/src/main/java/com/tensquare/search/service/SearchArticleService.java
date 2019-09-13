package com.tensquare.search.service;

import com.tensquare.search.dao.SearchArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 */
@Service
@Transactional
public class SearchArticleService {

    @Autowired
    private SearchArticleDao articleDao;

    /**
     * 搜索
     */
    public Page<Article> search(String keyword, int page, int size){
        /**
         * 使用命名查询
         */
        //return articleDao.findByTitleLikeOrContentLike(keyword,keyword, PageRequest.of(page-1,size));
        return articleDao.findByTitleOrContentLike(keyword,keyword, PageRequest.of(page-1,size));
    }
}
