表名：`${tableName}`

表描述：${tableComment}

| 字段 | 描述 | 类型 | 长度 | 必填 |
|----|----|:---:|:----:|:----:|
<#list columns as column>
|<#if column.columnName??>${column.columnName}<#else> - </#if>|<#if column.comment??>${column.comment}<#else> - </#if>|<#if column.dataType??>${column.dataType}<#else> - </#if>|<#if column.length??>${column.length}<#else> - </#if>|<#if column.nullable??>Y<#else> - </#if>|
</#list>
