package com.cnhis.dorislineage.demo.neo4j.service;

import java.util.List;

/**
 * 关联创建
 */
public interface RelationshipService {

    /**
     * 批量合并relation 以多对一的方式合并去建立关系 table|field -(relation_input)> relation
     * @param starts 开始节点的列表
     * @param end    结束节点
     */
    void mergeRelRelationInputs(List<String> starts, String end);
}
