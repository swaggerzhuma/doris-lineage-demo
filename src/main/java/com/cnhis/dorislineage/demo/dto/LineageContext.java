package com.cnhis.dorislineage.demo.dto;

import com.cnhis.dorislineage.demo.neo4j.domain.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 血缘上下文
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LineageContext {
    private  List<DataSourceNode> dataSourceNodeList = new ArrayList<>();
    private  List<DbNode> dbNodeList = new ArrayList<>();
    private  List<TableNode> tableNodeList = new ArrayList<>();
    private  List<FieldNode> fieldNodeList = new ArrayList<>();
    private  List<RelationNode> relationNodeList = new ArrayList<>();
    private SqlMessage sqlMessage;
    private String lineageJson;
}
