package com.cnhis.dorislineage.demo.parser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SqlRequestContext {

    private String dbType;

    private String sql;

    private String dataSourceName;
}
