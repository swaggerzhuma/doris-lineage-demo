package com.cnhis.dorislineage.demo.neo4j.domain;

import com.cnhis.dorislineage.demo.constants.NodeStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;

import java.time.LocalDateTime;

import static com.cnhis.dorislineage.demo.constants.NeoConstant.Node.DEFAULT_DATASOURCE;


/**
 * Node attribute abstraction
 */
@Data
public abstract class BaseNodeEntity extends BaseEntity {

    @Id
    @Index(unique = true)
    private String pk;

    private String status = NodeStatus.ACTIVE.name();
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime = LocalDateTime.now();

    // 图的展示名称
    private String name;

    private String dataSourceName = DEFAULT_DATASOURCE;

    private String dbName;

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getPk() {
        return pk;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public String getName() {
        return name;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
