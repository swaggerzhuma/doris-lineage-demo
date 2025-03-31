package com.cnhis.dorislineage.demo.handle.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SQL 解析
 */
@SourceHandler(NeoConstant.SourceType.SQL)
@Slf4j
public class SqlMessageHandler implements BaseMessageHandler {

    private static final String[] IGNORE_PROP = new String[]{"ext"};


    @Override
    public LineageContext handle(DorisSqlAudit audit) {
        SqlMessage sqlMessage = new SqlMessage();
        sqlMessage.setSql(audit.getStmt());
        sqlMessage.setDataSourceName(NeoConstant.Node.DEFAULT_DATASOURCE);
        sqlMessage.setDbType(Constants.DEFAULT_DB_TYPE);
        LineageContext context = new LineageContext();
        context.setSqlMessage(sqlMessage);

        SqlParserService parserService = SqlParserFactory.getParser(SqlEngineEnum.DORIS_LISTENER);
        List<FieldLineageModel> fieldLineageModels = parserService.parseSqlFieldLineage(audit.getStmt());
        log.info("字段血缘:\n{}", JSON.toJSONString(fieldLineageModels, SerializerFeature.PrettyFormat));

        String dataSourceName = sqlMessage.getDataSourceName();
        Set<DbNode> dbNodes = new HashSet<>();
        Set<TableNode> tableNodes = new HashSet<>();
        List<FieldNode> fieldNodes = new ArrayList<>();
        List<RelationNode> relationNodes = new ArrayList<>();
        String tableTargetNodePk = null;
        Set<String> tableSourceNodePkSet = new HashSet<>();
        for (FieldLineageModel lineage : fieldLineageModels) {
            FieldNameModel targetField = lineage.getTargetField();
            if (targetField == null) {
                log.error("targetField为空,{}", JSON.toJSONString(lineage));
                continue;
            }
            String targetDbName = targetField.getDbName();
            String targetTableName = targetField.getTableName();
            String targetFieldName = targetField.getFieldName();
            if (targetDbName == null || targetTableName == null || targetFieldName == null) {
                log.error("targetField成员为空:{}", JSON.toJSONString(lineage));
                continue;
            }
            dbNodes.add(new DbNode(dataSourceName, targetDbName));
            TableNode targetTableNode = new TableNode(dataSourceName, targetDbName, targetTableName);
            tableNodes.add(targetTableNode);
            tableTargetNodePk = targetTableNode.getPk();
            FieldNode targetFieldNode = new FieldNode(dataSourceName, targetDbName, targetTableName, targetFieldName);
            fieldNodes.add(targetFieldNode);
            HashSet<FieldNameWithProcessModel> sourceFields = lineage.getSourceFields();
            List<FieldNode> sourceFieldList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(sourceFields)) {
                for (FieldNameWithProcessModel sourceField : sourceFields) {
                    String sourceDbName = sourceField.getDbName();
                    String sourceTableName = sourceField.getTableName();
                    String sourceFieldName = sourceField.getFieldName();
                    if (sourceDbName == null || sourceTableName == null || sourceFieldName == null) {
                        log.error("sourceField成员为空:{}", JSON.toJSONString(lineage));
                        continue;
                    }
                    dbNodes.add(new DbNode(dataSourceName, sourceDbName));
                    TableNode sourceTableNode = new TableNode(dataSourceName, sourceDbName, sourceTableName);
                    tableNodes.add(sourceTableNode);
                    tableSourceNodePkSet.add(sourceTableNode.getPk());
                    sourceFieldList.add(new FieldNode(dataSourceName, sourceDbName, sourceTableName, sourceFieldName));
                }
            }
            fieldNodes.addAll(sourceFieldList);
            List<String> filedSourceNodePkList = sourceFieldList.stream().map(BaseNodeEntity::getPk).distinct().collect(Collectors.toList());
            // 字段关系节点
            RelationNode fieldRelationNode = new RelationNode(NeoConstant.RelationType.FIELD_RELATION, HandlerConstant.SOURCE_TYPE_SQL_PARSER, filedSourceNodePkList, targetFieldNode.getPk());
            // 执行的SQL
            fieldRelationNode.getExtra().put("sql", audit.getStmt());
            relationNodes.add(fieldRelationNode);
        }
        RelationNode tableRelationNode = new RelationNode(NeoConstant.RelationType.TABLE_RELATION, HandlerConstant.SOURCE_TYPE_SQL_PARSER, new ArrayList<>(tableSourceNodePkSet), tableTargetNodePk);
        tableRelationNode.getExtra().put("sql", audit.getStmt());
        relationNodes.add(tableRelationNode);
        context.setDataSourceNodeList(Collections.singletonList(new DataSourceNode(dataSourceName)));
        context.setDbNodeList(new ArrayList<>(dbNodes));
        context.setTableNodeList(new ArrayList<>(tableNodes));
        context.setFieldNodeList(fieldNodes);
        context.setRelationNodeList(relationNodes);
        return context;
    }

}
