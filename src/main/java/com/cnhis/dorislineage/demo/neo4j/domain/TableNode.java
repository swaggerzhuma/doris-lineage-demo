package com.cnhis.dorislineage.demo.neo4j.domain;

import com.cnhis.dorislineage.demo.constants.NeoConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.Optional;

/**
 * Table Node
 */
//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_TABLE)
public class TableNode extends BaseNodeEntity {

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public TableNode(String dataSourceName, String dbName, String tableName) {
        Optional.ofNullable(dataSourceName).ifPresent(this::setDataSourceName);
        Optional.ofNullable(tableName).ifPresent(this::setTableName);
        Optional.ofNullable(this.getTableName()).ifPresent(this::setName);
        this.setDbName(dbName);
        // dataSource/db/table
        String pk = NodeQualifiedName.ofTable(this.getDataSourceName(), this.getDbName(), this.getTableName()).toString();
        this.setPk(pk);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (Objects.isNull(o)) {
            return false;
        }
        TableNode node = (TableNode) o;
        if (Objects.isNull(this.getDbName()) || Objects.isNull(node.getDbName()) || Objects.isNull(this.getTableName()) || Objects.isNull(node.getTableName())) {
            return false;
        }
        return this.getDbName().equals(node.getDbName()) && this.getTableName().equals(node.getTableName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDbName(), this.getTableName());
    }
}
