<#list tables as table>
    <span style="font-size:24px">表名：`${table.tableName}`</span><#lt>

    <span style="font-size:18px">表描述：${table.tableComment}</span><#lt>

    | 字段 | 描述 | 类型 | 长度 | 必填 |<#lt>
    |:-----|:-----|:----:|:----:|:----:|<#lt>
    <#list table.columns as column>
        |<#if column.columnName?has_content>`${column.columnName}`<#else>  </#if><#t>
        |<#if column.comment?has_content>${column.comment}<#else>  </#if><#t>
        |<#if column.dataType?has_content>`${column.dataType}`<#else>  </#if><#t>
        |<#if column.length?has_content>`${column.length}`<#else>  </#if><#t>
        |<#if column.nullable?has_content>Y<#else>  </#if><#t>
        |<#lt>
    </#list>
    <br/><#lt>
    <br/><#lt>
</#list>
