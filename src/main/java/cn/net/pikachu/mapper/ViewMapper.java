package cn.net.pikachu.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import cn.net.pikachu.model.View;

public interface ViewMapper {
    int insert(View record);

    int insertSelective(View record);

    List<View> selectByTableSchema(@Param("tableSchema")String tableSchema);


}