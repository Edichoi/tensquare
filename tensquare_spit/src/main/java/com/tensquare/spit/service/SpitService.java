package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

/*
* 吐槽service
* */
@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    //mongoTemplate: 封装了MongoDB对集合所有操作的方法
    @Autowired
    private MongoTemplate mongoTemplate;

    /*
    * 查找所有
    * */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /*
    * 查询一个
    * */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /*添加
    * */
    public void add(Spit spit){
        // 设置id
        spit.setId(idWorker.nextId()+"");
        spitDao.save(spit);

        // 如果该信息为评论, 则添加评论对应的吐槽的回复数+1
        if(spit.getParentid() != null
                && !spit.getParentid().equals("")) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            Update update = new Update();
            update.inc("comment", 1);

            mongoTemplate.updateFirst(query, update, "spit");
        }
    }

    /**
     * 修改
     */
    public void update(Spit spit){
        //spit 必须带有数据库存在的id
        spitDao.save(spit);
    }

    /*删除
    * */
    public void delete(String id){
        spitDao.deleteById(id);
    }

    /*
    * 根据上级ID查询吐槽
    * */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        /*
        使用命名查询+Pageable
        */
        return spitDao.findByParentid(parentid, PageRequest.of(page-1, size));
    }

    public void thumbup(String id) {
        //创建Query对象封装： {"_id":"1086835055109640192"}
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        /**
         * {"thumbup":{$gt:NumberInt(10)}}
         */
   /*  Query query = new Query();
     query.addCriteria(Criteria.where("thumbup").gt(10));*/

        //创建更新对象：{$inc:{"thumbup":NumberInt(1)}}
        Update update = new Update();
        update.inc("thumbup",1);

        //底层：db.spit.update(  {"_id":"1086835055109640192"},{$inc:{"thumbup":NumberInt(1)}}  )
        mongoTemplate.updateFirst(query,update,"spit");

    }

}
