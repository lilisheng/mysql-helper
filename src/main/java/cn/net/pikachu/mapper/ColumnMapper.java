package cn.net.pikachu.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.net.pikachu.model.Column;

public interface ColumnMapper {
    int insert(Column record);

    int insertSelective(Column record);

    List<Column> selectByTableSchemaAndTableName(@Param("tableSchema")String tableSchema,@Param("tableName")String tableName);


}