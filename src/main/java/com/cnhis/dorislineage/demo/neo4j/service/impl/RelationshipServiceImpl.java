package com.cnhis.dorislineage.demo.neo4j.service.impl;

import com.alibaba.fastjson.JSON;
import com.cnhis.dorislineage.demo.neo4j.service.RelationshipService;
import com.cnhis.dorislineage.demo.utils.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * relation_input  table|field -> (relation_input) -> relation
 * 以多对一的方式合并去建立关系
 */
@Service
@Slf4j
public class RelationshipServiceImpl implements RelationshipService {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String DELIMITER = "','";

    /**
     * s -> (n:1) e
     */
    private static final String MERGE_RELATION_INPUTS = "MATCH (s),(e:Relation) WHERE " +
            "s.pk in [%s] and e.pk = '%s'  "
            + "MERGE (s)-[r:relation_input]->(e) set r.timestamp=timestamp() "
            + "RETURN id(r) as relId";

    @Override
    public void mergeRelRelationInputs(List<String> starts, String end) {
        if (CollectionUtils.isEmpty(starts)) {
            return;
        }

        String cql = String.format( MERGE_RELATION_INPUTS, starts.stream().map(s -> s.replace("'","\\'")).collect(Collectors.joining(DELIMITER, StringPool.SINGLE_QUOTE, StringPool.SINGLE_QUOTE)), end);
        log.info("execute cql is [{}]", cql);
        Session session = sessionFactory.openSession();
        Result result = session.query(cql, Collections.emptyMap());
        log.info("result:{}", JSON.toJSONString(result));
        List<Map<String, Object>> resultMapList = new ArrayList<>();
        result.forEach(resultMapList::add);
        if (resultMapList.size() != starts.size()) {
            log.warn("execute recode num [" + starts.size() + "] != success num [" + resultMapList.size() + "]. maybe neo4j question");
        }
    }
}
