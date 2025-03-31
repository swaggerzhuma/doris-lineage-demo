package com.cnhis.dorislineage.demo.neo4j.dao;

import com.cnhis.dorislineage.demo.neo4j.domain.FieldNode;
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
public interface FieldRepository extends Neo4jRepository<FieldNode, String> {

    @Query("MATCH (f:Field),(p:Relation) " +
            "WHERE f.pk = $sourceFieldNodePk " +
            "and p.pk= $relationNodePk " +
            "MERGE (f)-[r:relation_input]->(p)")
    void mergeRelWithRelation(@Param("sourceFieldNodePk") String sourceFieldNodePk, @Param("relationNodePk") String relationNodePk);

    /**
     * 获取字段的上游血缘关系
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param fieldName 字段名
     * @return
     */
    @Query("MATCH (f:Field {dbName: $dbName, tableName: $tableName, fieldName: $fieldName}) " +
            "OPTIONAL MATCH (f)-[:field_from_table]->(t:Table) " +
            "OPTIONAL MATCH (t)-[:table_from_db]->(db:Db) " +
            "OPTIONAL MATCH (db)-[:db_from_datasource]->(ds:Datasource) " +
            "OPTIONAL MATCH (f)<-[:relation_output]-(r:Relation) " +
            "OPTIONAL MATCH (r)<-[:relation_input]-(input:Field) " +
            "OPTIONAL MATCH (f)-[:relation_input]->(r2:Relation) " +
            "OPTIONAL MATCH (r2)-[:relation_output]->(output:Field) " +
            "RETURN ds AS datasource, db AS database, t AS table,f AS field, " +
            "collect(DISTINCT r) AS input_relations,collect(DISTINCT r2) AS output_relations, collect(DISTINCT input) AS inputs, collect(DISTINCT output) AS outputs ")
    List<Map<String, Object>> getFieldBloodLine(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName,
            @Param("fieldName") String fieldName
    );


    /**
     * 获取字段所有层级血缘关系
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param fieldName 字段名
     * @return
     */
    @Query("MATCH (f:Field {dbName: $dbName, tableName: $tableName, name: $fieldName}) " +
            "MATCH path = (f)-[:relation_input|relation_output*..20]-(related:Field) " +
            "RETURN f AS Field, " +
            "collect(DISTINCT nodes(path)) AS allNodes, collect(DISTINCT relationships(path)) AS allRelationships ")
    List<Map<String, Object>> getFullFieldBloodLine(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName,
            @Param("fieldName") String fieldName
    );


    /**
     * 获取字段所有层级血缘关系,包括表，库，数据源等信息
     *
     * @param dbName    数据库名
     * @param tableName 表名
     * @param fieldName 字段名
     * @return
     */
    @Query("MATCH (f:Field) " +
            "WHERE f.dbName =$dbName AND f.tableName = $tableName AND f.name = $fieldName " +
            "OPTIONAL MATCH (f)-[:field_from_table]->(t:Table) " +
            "OPTIONAL MATCH (t)-[:table_from_db]->(db:Db) " +
            "OPTIONAL MATCH (db)-[:db_from_datasource]->(ds:Datasource) " +
            "WITH ds, db, t, f " +
            "MATCH path = (f)-[:relation_input|relation_output*..20]-(related:Field) " +
            "RETURN ds AS datasource, db AS database, t AS table, f AS field ," +
            "collect(DISTINCT nodes(path)) AS allNodes, collect(DISTINCT relationships(path)) AS allRelationships")
    List<Map<String, Object>> getFullFieldBloodLine2(
            @Param("dbName") String dbName,
            @Param("tableName") String tableName,
            @Param("fieldName") String fieldName
    );

}
