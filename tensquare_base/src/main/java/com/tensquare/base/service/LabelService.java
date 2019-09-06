package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签service
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 查询一个
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }


    /**
     * 添加
     */
    public void add(Label label) {
        //设置id
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    /**
     * 修改
     */
    public void update(Label label) { // label必须带有数据库存在的id
        labelDao.save(label);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        labelDao.deleteById(id);
    }

    public  List<Label> findSearch(Map searchMap) {
        Specification<Label> specification = createSpecification(searchMap);
        return labelDao.findAll(specification);
    }

    /**
     * 创建Specification对象
     */
    private Specification<Label> createSpecification(Map searchMap) {
        //通常提供Specification接口的匿名内部类
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //1.准备一个List集合，用于存储Predicate条件对象
                List<Predicate> preList = new ArrayList<>();
                //2.根据客户输入的条件（searchMap），进行条件对象（Predicate）的拼装，把条件对象存入List集合
                if (searchMap.get("labelname") != null && !searchMap.get("labelname").equals("")) {
                    // labelname like '%xxx%'
                    preList.add(cb.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%"));
                }
                if (searchMap.get("state") != null && !searchMap.get("state").equals("")) {
                    // state = '1'
                    preList.add(cb.equal(root.get("state").as(String.class), searchMap.get("state")));
                }
                if (searchMap.get("recommend") != null && !searchMap.get("recommend").equals("")) {
                    // recommend = '1'
                    preList.add(cb.equal(root.get("recommend").as(String.class), searchMap.get("recommend")));
                }
                //3.把所有条件进行连接查询（and  or）
                // labelname like '%xxx%' and  state = '1' and recommend = '1'
                Predicate[] preArray = new Predicate[preList.size()];
                //preList.toArray(preArray): 把preList集合的每个元素取出，放入preArray数组中，返回preArray数组
                return cb.and(preList.toArray(preArray));
            }
        };
    }


    public  Page<Label> findSearch(int page, int size, Map searchMap) {
        Specification<Label> spec = createSpecification(searchMap);
        //注意：PageRequest的第一个参数，第一页从0开始
        return labelDao.findAll(spec, PageRequest.of(page-1,size));
    }
}
