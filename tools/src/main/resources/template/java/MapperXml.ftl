<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${className}Mapper">
    <resultMap id="${className}" type="${className}Dto">
        <#list columns as column>
            <result property="${column.name}" column="${column.columnName}"/>
        </#list>
    </resultMap>
</mapper>