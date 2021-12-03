|id|name|<#list ranks as rank>R${rank}|</#list>
|:---:|:---:|<#list ranks as rank>:---:|</#list>
<#list datas?keys as key>
    |${key}|<span style="font-size: 14px">${datas[key].name}<span>|<#list datas[key].ghz as ghz><#if ghz??>${ghz}<#else>  </#if>|</#list>|<#lt>
</#list>



|id|name|<#list ranks as rank>R${rank}|</#list>
|:---:|:---:|<#list ranks as rank>:---:|</#list>
<#list datas?keys as key>
    |${key}|<span style="font-size: 14px">${datas[key].name}<span>|<#list datas[key].jjc as jjc><#if jjc??>${jjc}<#else>  </#if>|</#list>|<#lt>
</#list>
