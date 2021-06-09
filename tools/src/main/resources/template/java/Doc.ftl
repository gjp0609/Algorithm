
### ${tableComment}

| 字段 | 描述 | 类型 | 长度 | 是否为空 |
|----|----|:---:|:----:|:----:|
<#list columns as column>
|<#if column.columnName??>${column.columnName}<#else> - </#if>|<#if column.comment??>${column.comment}<#else> - </#if>|<#if column.dataType??>${column.dataType}<#else> - </#if>|<#if column.length??>${column.length}<#else> - </#if>|<#if column.nullable??>${column.nullable}<#else> - </#if>|
</#list>
