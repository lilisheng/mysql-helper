package cn.net.pikachu

import cn.net.pikachu.mapper.ColumnMapper
import cn.net.pikachu.mapper.SchemataMapper
import cn.net.pikachu.mapper.TableMapper
import cn.net.pikachu.mapper.ViewMapper
import cn.net.pikachu.model.Column
import cn.net.pikachu.model.Table
import cn.net.pikachu.model.View
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.lang.StringBuilder
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
@MapperScan("cn.net.pikachu.mapper")
class MysqlHelperApplication

fun main(args: Array<String>) {
    runApplication<MysqlHelperApplication>(*args)
}
@Controller
class MainController(
        private val columnMapper: ColumnMapper,
        private val schemataMapper: SchemataMapper,
        private val tableMapper: TableMapper,
        private val viewMapper: ViewMapper
) {
    @GetMapping("/")
    fun index(response: HttpServletResponse) {
        val sb = StringBuilder()
        // 使用其他的数据库，修改这里
        val dbName = "demo"
        val title = "${dbName}数据库设计文档"
        sb.append("# $title")
        val tableList = getTableList(dbName)
        if (tableList.isNotEmpty()) {
sb.append("""
## 表 (共${tableList.size}张)
""")
            for(table in tableList) {
                val tableName = table.tableName
                sb.append("""
### ${tableName} (${table.tableComment})
| 字段               | 类型          | 特殊属性               | 注释                        |
| ------------------ | ------------- | ---------------------- | --------------------------- |""")
                val tableColumnList = getTableColumnList(dbName, tableName)
                for(col in tableColumnList) {
                    sb.append("""
| ${col.columnName}| ${col.columnType}| ${col.getSpecialPropertity()}| ${col.columnComment}|""")
                }
            }
        }
        val viewList = getViewList(dbName)
        if (viewList.isNotEmpty()){
            sb.append("""
## 视图 (共${viewList.size}张)
""")
            for (view in viewList) {
                sb.append("""
### ${view.tableName}
```sql
${view.viewDefinition}
```
                """)
            }
        }
        val file = File("out.md")
        file.writeText(sb.toString())
        response.characterEncoding = "UTF-8"
        response.setHeader("content-type", "text/txt;charset=UTF-8")
        val writer = response.writer
        writer.write(sb.toString())
        writer.close()
    }
    fun getTableList(dbName: String):List<Table> {
        return tableMapper.selectByTableSchemaAndTableType(dbName, "BASE TABLE")
    }
    fun getTableColumnList(dbName: String, tableName: String): List<Column> {
        return columnMapper.selectByTableSchemaAndTableName(dbName, tableName)
    }
    fun getViewList(dbName: String): List<View> {
        return viewMapper.selectByTableSchema(dbName)
    }
}
fun Column.getSpecialPropertity():String {
    val extra = when(this.extra) {
        "auto_increment" -> "自增"
        "on update CURRENT_TIMESTAMP" -> "自动更新时间戳"
        else -> this.extra
    }
    val key = when (this.columnKey) {
        "PRI" -> "主键"
        "UNI" -> "唯一索引"
        else -> this.columnKey
    }
    if (extra.isNullOrEmpty()) {
        return key
    } else if (key.isNullOrEmpty()) {
        return extra
    } else {
        return "${extra}, ${key}"
    }
}