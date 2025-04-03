package com.cnhis.dorislineage.demo.handle.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.cnhis.dorislineage.demo.dto.LineageContext;
import com.cnhis.dorislineage.demo.handle.BaseStorageHandler;
import com.cnhis.dorislineage.demo.neo4j.dao.*;
import com.cnhis.dorislineage.demo.neo4j.domain.RelationNode;
import com.cnhis.dorislineage.demo.neo4j.service.RelationshipService;
import com.cnhis.dorislineage.demo.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 从处理上下文中，取出元数据进行存储
 * merge的方式： 存在则更新，不存在则新增
 */
@Component
public class MergeStorageHandler implements BaseStorageHandler {

    @Autowired
    private DbRepository dbRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void handle(LineageContext lineageMapping) {
        //存储血缘json到doris
        String sql = "insert into test.lineage_json(id,stmt,lineage_json)values('" + IdUtil.simpleUUID() + "','" + StringUtil.escapeSingleQuotes(JSON.toJSONString(lineageMapping.getSqlMessage().getSql())) + "','" + lineageMapping.getLineageJson() + "')";
        jdbcTemplate.execute(sql);
        // 创建或更新节点信息
        createOrUpdateNode(lineageMapping);
        // 创建或更新节点关系
        createOrUpdateRelationShip(lineageMapping);
    }

    private void createOrUpdateRelationShip(LineageContext lineageMapping) {
        // dataSource -> dbs
        dataSourceRepository.mergeRelWithDb();
        // db -> tables
        dbRepository.mergeRelWithTable();
        // table -> fields
        tableRepository.mergeRelWithField();
        // 创建输入输出关系
        this.createOrUpdateRelationship(lineageMapping);
    }

    private void createOrUpdateRelationship(LineageContext lineageMapping) {
        List<RelationNode> relationNodeList = lineageMapping.getRelationNodeList();
        if (CollectionUtils.isEmpty(relationNodeList)) {
            return;
        }
        relationNodeList.forEach(relationNode -> {
            // table | fields -> (relation_input) -> relation
            relationshipService.mergeRelRelationInputs(relationNode.getSourceNodePkList(), relationNode.getPk());
            relationRepository.mergeRelRelationOutput(relationNode.getPk(), relationNode.getTargetNodePk());
        });
    }

    private void createOrUpdateNode(LineageContext lineageMapping) {
        // dataSource
        dataSourceRepository.saveAll(lineageMapping.getDataSourceNodeList());
        // db
        dbRepository.saveAll(lineageMapping.getDbNodeList());
        // table
        tableRepository.saveAll(lineageMapping.getTableNodeList());
        // field
        fieldRepository.saveAll(lineageMapping.getFieldNodeList());
        // relation
        relationRepository.saveAll(lineageMapping.getRelationNodeList());
    }
}
