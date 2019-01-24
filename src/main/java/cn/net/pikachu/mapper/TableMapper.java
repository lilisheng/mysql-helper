package cn.net.pikachu.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.net.pikachu.model.Table;

public interface TableMapper {
    int insert(Table record);

    int insertSelective(Table record);

    List<Table> selectByTableSchemaAndTableType(@Param("tableSchema")String tableSchema,@Param("tableType")String tableType);


}