import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
// import jakarta.validation.constraints.*; // for newer version
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@TableName("${tableName}")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "${tableComment}")
public class ${className} extends BaseEntity<${className}> {

    private static final long serialVersionUID = 1L;

    <#list columns as column>
        <#if column.lengthValid??>
    ${column.lengthValid}
        </#if>
        <#if column.nullableValid??>
    ${column.nullableValid}
        </#if>
    @ApiModelProperty(value = "${column.comment}")
    private ${column.type} ${column.name};

    </#list>
}
