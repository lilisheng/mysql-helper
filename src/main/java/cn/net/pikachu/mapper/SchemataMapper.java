package cn.net.pikachu.mapper;

import cn.net.pikachu.model.Schemata;

public interface SchemataMapper {
    int insert(Schemata record);

    int insertSelective(Schemata record);
}