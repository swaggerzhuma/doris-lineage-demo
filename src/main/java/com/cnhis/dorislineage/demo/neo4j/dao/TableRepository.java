package com.cnhis.dorislineage.demo.neo4j.dao;

import com.cnhis.dorislineage.demo.neo4j.domain.TableNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 字段关系
 */
@Repository
public interface TableRepository extends Neo4jRepository<TableNode, String> {

    /**
     * field_from_table Merge: if not exists create,otherwise,update it
     */
    @Query("MATCH (field:Field),(table:Table) " +
            "WHERE field.dataSourceName = table.dataSourceName " +
            "AND field.dbName = table.dbName " +
            "AND field.tableName = table.tableName " +
            "MERGE (field)-[:field_from_table]->(table)")
    void mergeRelWithField();


    /**
     * 获取表的上游血缘关系
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return 表血缘关系数据
     */
    @Query("MATCH (t:Table) " +
            "WHERE t.dbName = $dbName AND t.name = $tableName " +
            "OPTIONAL MATCH (t)-[:table_from_db]->(db:Db) " +
            "OPTIONAL MATCH (db)-[:db_from_datasource]->(ds:Datasource) " +
            "OPTIONAL MATCH (t)<-[:relation_output]-(r:Relation) " +
            "OPTIONAL MATCH (r)<-[:relation_input]-(input:Table) " +
            "OPTIONAL MATCH (t)-[:relation_input]->(r2:Relation) " +
            "OPTIONAL MATCH (r2)-[:relation_output]->(output:Table) " +
            "RETURN ds AS datasource, db AS database, t AS table, " +
            "collect(DISTINCT r) AS input_relations, collect(DISTINCT r2) AS output_relations , collect(DISTINCT input) AS inputs, collect(DISTINCT output) AS outputs ")
    List<Map<String, Object>> getTableBloodLine(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName);


    /**
     * 获取表的所有层级依赖关系，包括表信息
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return 表的完整血缘依赖路径
     */
    @Query("MATCH (t:Table {dbName: $dbName, name: $tableName}) " +
            "MATCH path = (t)-[:relation_input|relation_output*..20]-(related:Table) " +
            "RETURN t AS table, " +
            "collect(DISTINCT nodes(path)) AS allNodes, collect(DISTINCT relationships(path)) AS allRelationships ")
    List<Map<String, Object>> getFullTableBloodLine(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName);


    /**
     * 获取表的所有层级依赖关系,包括表，库，数据源等信息
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @return 表的完整血缘依赖路径
     */
    @Query("MATCH (t:Table) " +
            "WHERE t.dbName =$dbName AND t.name = $tableName " +
            "OPTIONAL MATCH (t)-[:table_from_db]->(db:Db) " +
            "OPTIONAL MATCH (db)-[:db_from_datasource]->(ds:Datasource) " +
            "WITH ds, db, t " +
            "MATCH path = (t)-[:relation_input|relation_output*..20]-(related:Table) " +
            "RETURN ds AS datasource, db AS database, t AS table, " +
            "collect(DISTINCT nodes(path)) AS allNodes, collect(DISTINCT relationships(path)) AS allRelationships")
    List<Map<String, Object>> getFullTableBloodLine2(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName);


}
