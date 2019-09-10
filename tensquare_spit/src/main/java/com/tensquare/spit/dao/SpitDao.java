package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽dao接口
 * MongoRepository类似于JpaRepository，拥有CRUD方法
 */
public interface SpitDao extends MongoRepository<Spit,String> {

    public Page<Spit> findByParentid(String parentid, PageRequest of);
}
