package com.cnhis.dorislineage.demo.neo4j.domain;

import com.cnhis.dorislineage.demo.constants.NeoConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.Optional;

/**
 * Node Field
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_FIELD)
public class FieldNode extends BaseNodeEntity {

    private String tableName;
    private String fieldName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldNode(String dataSourceName, String dbName, String tableName, String fieldName) {
        Optional.ofNullable(dataSourceName).ifPresent(this::setDataSourceName);
        this.setDbName(dbName);
        this.setTableName(tableName);
        this.setFieldName(fieldName);
        Optional.ofNullable(this.getFieldName()).ifPresent(this::setName);
        String pk = NodeQualifiedName.ofField(this.getDataSourceName(), this.getDbName(), this.getTableName(), this.getFieldName()).toString();
        this.setPk(pk);
        this.setNodeType(NeoConstant.Type.NODE_FIELD);
    }


}
