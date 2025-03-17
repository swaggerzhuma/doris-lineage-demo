package com.cnhis.dorislineage.demo.neo4j.domain;

import com.cnhis.dorislineage.demo.constants.NeoConstant;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.Objects;
import java.util.Optional;

/**
 * Db Node
 * 存储字段：dbNode
 */
//@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@Data
@NodeEntity(NeoConstant.Type.NODE_DB)
public class DbNode extends BaseNodeEntity {

    private String sql;

    public DbNode(String dataSourceName, String dbName) {
        Optional.ofNullable(dataSourceName).ifPresent(this::setDataSourceName);
        this.setDbName(dbName);
        String pk = NodeQualifiedName.ofDb(this.getDataSourceName(), this.getDbName()).toString();
        this.setPk(pk);
        // displayName
        this.setName(this.getDbName());
    }


    public DbNode(String dbName) {
        this(null, dbName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (Objects.isNull(o)) {
            return false;
        }
        DbNode node = (DbNode) o;
        if (Objects.isNull(this.getDbName()) || Objects.isNull(node.getDbName())) {
            return false;
        }
        return this.getDbName().equals(node.getDbName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getDbName());
    }

}
