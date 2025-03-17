package com.cnhis.dorislineage.demo.handle.impl;


import com.alibaba.fastjson.JSON;
import com.cnhis.dorislineage.demo.annotation.SourceHandler;
import com.cnhis.dorislineage.demo.constants.Constants;
import com.cnhis.dorislineage.demo.constants.HandlerConstant;
import com.cnhis.dorislineage.demo.constants.NeoConstant;
import com.cnhis.dorislineage.demo.dto.DorisSqlAudit;
import com.cnhis.dorislineage.demo.dto.LineageContext;
import com.cnhis.dorislineage.demo.dto.SqlMessage;
import com.cnhis.dorislineage.demo.handle.BaseMessageHandler;
import com.cnhis.dorislineage.demo.neo4j.domain.*;
import com.sql.parser.lineage.SqlParserFactory;
import com.sql.parser.lineage.SqlParserService;
import com.sql.parser.lineage.enums.SqlEngineEnum;
import com.sql.parser.lineage.model.lineage.FieldLineageModel;
import com.sql.parser.lineage.model.lineage.FieldNameModel;
import com.sql.parser.lineage.model.lineage.FieldNameWithProcessModel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL 解析
 */
@SourceHandler(NeoConstant.SourceType.SQL)
@Slf4j
public class SqlMessageHandler implements BaseMessageHandler {

    private static final String DATA_SOURCE_NAME = "test_doris";
    private static final String[] IGNORE_PROP = new String[]{"ext"};


    @Override
    public LineageContext handle(DorisSqlAudit audit) {
        SqlMessage sqlMessage = new SqlMessage();
        sqlMessage.setSql(audit.getStmt());
        sqlMessage.setDataSourceName(DATA_SOURCE_NAME);
        sqlMessage.setDbType(Constants.DEFAULT_DB_TYPE);
        LineageContext context = new LineageContext();
        context.setSqlMessage(sqlMessage);

        SqlParserService parserService = SqlParserFactory.getParser(SqlEngineEnum.DORIS_LISTENER);
        List<FieldLineageModel> fieldLineageModels = parserService.parseSqlFieldLineage(audit.getStmt());
        log.info("字段血缘:{}", JSON.toJSONString(fieldLineageModels));

        String dataSourceName = sqlMessage.getDataSourceName();
        Set<DbNode> dbNodes = new HashSet<>();
        Set<TableNode> tableNodes = new HashSet<>();
        List<FieldNode> fieldNodes = new ArrayList<>();
        List<RelationNode> relationNodes = new ArrayList<>();
        fieldLineageModels.forEach(lineage -> {
            FieldNameModel targetField = lineage.getTargetField();
            if (targetField == null) {
                log.error("targetField为空,{}", JSON.toJSONString(lineage));
                return;
            }
            String targetDbName = targetField.getDbName();
            String targetTableName = targetField.getTableName();
            String targetFieldName = targetField.getFieldName();
            if (targetDbName == null || targetTableName == null || targetFieldName == null) {
                log.error("targetField成员为空:{}", JSON.toJSONString(lineage));
                return;
            }
            dbNodes.add(new DbNode(dataSourceName, targetDbName));
            tableNodes.add(new TableNode(dataSourceName, targetDbName, targetTableName));
            FieldNode targetFieldNode = new FieldNode(dataSourceName, targetDbName, targetTableName, targetFieldName);
            fieldNodes.add(targetFieldNode);
            HashSet<FieldNameWithProcessModel> sourceFields = lineage.getSourceFields();
            List<FieldNode> sourceFieldList = new ArrayList<>();
            for (FieldNameWithProcessModel sourceField : sourceFields) {
                String sourceDbName = sourceField.getDbName();
                String sourceTableName = sourceField.getTableName();
                String sourceFieldName = sourceField.getFieldName();
                if (sourceDbName != null || sourceTableName == null || sourceFieldName == null) {
                    log.error("sourceField成员为空:{}", JSON.toJSONString(lineage));
                    continue;
                }
                dbNodes.add(new DbNode(dataSourceName, sourceDbName));
                tableNodes.add(new TableNode(dataSourceName, sourceDbName, sourceTableName));
                sourceFieldList.add(new FieldNode(dataSourceName, sourceDbName, sourceTableName, sourceFieldName));
            }
            fieldNodes.addAll(sourceFieldList);
            List<String> filedSourceNodePkList = sourceFieldList.stream().map(BaseNodeEntity::getPk).distinct().collect(Collectors.toList());
            // 字段关系节点
            RelationNode fieldRelationNode = new RelationNode(NeoConstant.RelationType.FIELD_RELATION, filedSourceNodePkList, targetFieldNode.getPk());
            fieldRelationNode.setType(HandlerConstant.SOURCE_TYPE_SQL_PARSER);
            // 执行的SQL
            fieldRelationNode.getExtra().put("sql", audit.getStmt());
            relationNodes.add(fieldRelationNode);
        });
        context.setDataSourceNodeList(Collections.singletonList(new DataSourceNode(dataSourceName)));
        context.setDbNodeList(new ArrayList<>(dbNodes));
        context.setTableNodeList(new ArrayList<>(tableNodes));
        context.setFieldNodeList(fieldNodes);
        context.setRelationNodeList(relationNodes);
        return context;
    }

}
