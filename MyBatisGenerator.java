package com.crawler.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @Project TEST_JAR
 * @Package com.test.jar.mybatis
 * @FileName MyBatisGeneratorTest.java 
 * @ClassName MyBatisGeneratorTest 
 * @author: 苏志伟
 * @date 2017年5月20日 上午11:46:56 
 * @Description MyBatis代码自动生成
 */
public class MyBatisGenerator {
	
	/**
     **********************************使用前必读*******************
     **
     ** 使用前请将moduleName更改为自己模块的名称即可（一般情况下与数据库名一致），其他无须改动。
     **
     ***********************************************************
     */
 
    private final String type_char = "char";
 
    private final String type_date = "date";
 
    private final String type_timestamp = "timestamp";
 
    private final String type_int = "int";
 
    private final String type_bigint = "bigint";
 
    private final String type_text = "text";
 
    private final String type_bit = "bit";
 
    private final String type_decimal = "decimal";
 
    private final String type_blob = "blob";
 
    private final String moduleName = "crawler"; // 对应模块名称（根据自己模块做相应调整!!!务必修改^_^）
 
    private final String bean_path = "E:/Buffer/Eclipse/DATA_CENTER_CRAWLER_ENTITY/src/main/java/com/crawler/entity/bean";
 
    private final String mapper_path = "E:/Buffer/Eclipse/DATA_CENTER_CRAWLER_REPOSITORY/src/main/java/com/crawler/repository/mysql";
 
    private final String xml_path = "E:/Buffer/Eclipse/DATA_CENTER_CRAWLER_REPOSITORY/src/main/resources/mapper";
 
    private final String bean_package = "com.test.jar.mybatis." + moduleName + ".entity";
 
    private final String mapper_package = "com.test.jar.mybatis." + moduleName + ".mapper";
 
 
    private final String driverName = "com.mysql.jdbc.Driver";
 
    private final String user = "root";
 
    private final String password = "root";
 
    private final String url = "jdbc:mysql://localhost:3306/" + moduleName + "?characterEncoding=utf8";
 
    private String tableName = null;
 
    private String beanName = null;
 
    private String mapperName = null;
 
    private Connection conn = null;
 
 
    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
    }
 
 
    /**
     *  获取所有的表
     *
     * @return
     * @throws SQLException 
     */
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        PreparedStatement pstate = conn.prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
        while ( results.next() ) {
            String tableName = results.getString(1);
            //          if ( tableName.toLowerCase().startsWith("yy_") ) {
            tables.add(tableName);
            //          }
        }
        return tables;
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title processTableName  
     * @author 苏志伟
     * @Description 处理获取到的这张表名称，转为为驼峰命名
     * @param table  void   
     * @date 2017年5月20日 下午2:11:13
     * @modifitor 苏志伟
     * @reason
     */
    private void processTableName( String table ) {
        StringBuffer sb = new StringBuffer(table.length());
        String tableNew = table.toLowerCase();//转换为小写
        String[] tables = tableNew.split("_");
        String temp = null;
        for ( int i = 0 ; i < tables.length ; i++ ) {
            temp = tables[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        beanName = sb.toString();
        mapperName = beanName + "Mapper";
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title processType  
     * @author 苏志伟
     * @Description 处理数据类型，转换为对应的Java类型
     * @param type
     * @return  String   
     * @date 2017年5月20日 下午2:16:17
     * @modifitor 苏志伟
     * @reason
     */
    private String processType( String type ) {
        if ( type.indexOf(type_char) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_bigint) > -1 ) {
            return "Long";
        } else if ( type.indexOf(type_int) > -1 ) {
            return "Integer";
        } else if ( type.indexOf(type_date) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(type_text) > -1 ) {
            return "String";
        } else if ( type.indexOf(type_timestamp) > -1 ) {
            return "java.util.Date";
        } else if ( type.indexOf(type_bit) > -1 ) {
            return "Boolean";
        } else if ( type.indexOf(type_decimal) > -1 ) {
            return "java.math.BigDecimal";
        } else if ( type.indexOf(type_blob) > -1 ) {
            return "byte[]";
        }
        return null;
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title processField  
     * @author 苏志伟
     * @Description 处理数据库字段信息
     * @param field
     * @return  String   
     * @date 2017年5月20日 下午2:17:20
     * @modifitor 苏志伟
     * @reason
     */
    private String processField( String field ) {
        StringBuffer sb = new StringBuffer(field.length());
        //field = field.toLowerCase();
        String[] fields = field.split("_");
        String temp = null;
        sb.append(fields[0]);
        for ( int i = 1 ; i < fields.length ; i++ ) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title processResultMapId  
     * @author 苏志伟
     * @Description 将实体类名首字母改为小写
     * @param beanName
     * @return  String   
     * @date 2017年5月20日 下午2:18:45
     * @modifitor 苏志伟
     * @reason
     */
    private String processResultMapId( String beanName ) {
    	beanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        return beanName;
    }
 
 
    /**
     *  构建类上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    private BufferedWriter buildClassComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" **/");
        return bw;
    }
 
 
    /**
     *  构建方法上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException 
     */
    private BufferedWriter buildMethodComment( BufferedWriter bw, String text ) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t **/");
        return bw;
    }
 
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title buildEntityBean  
     * @author 苏志伟
     * @Description 根据表的字段名称、字段类型、字段注释、表注释生成实体类
     * @param columns
     * @param types
     * @param comments
     * @param tableComment
     * @throws IOException  void   
     * @date 2017年5月20日 下午2:12:28
     * @modifitor 苏志伟
     * @reason
     */
    private void buildEntityBean( List<String> columns, List<String> types, List<String> comments, String tableComment )
        throws IOException {
        
    	File folder = new File(bean_path);
        //文件夹不存在则创建文件夹
    	if ( !folder.exists() ) {
            folder.mkdir();
        }
    	
        File beanFile = new File(bean_path, beanName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.write("import java.io.Serializable;");
        bw.newLine();
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        bw.write("public class " + beanName + " {");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        for ( int i = 0 ; i < size ; i++ ) {
            bw.write("\t/**" + comments.get(i) + "**/");
            bw.newLine();
            						//处理数据类型							处理字段信息
            bw.write("\tprivate " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
        // 生成get 和 set方法
        String tempField = null;
        String _tempField = null;
        String tempType = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempType = processType(types.get(i));
            _tempField = processField(columns.get(i));
            //首字母转换为大写
            tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
            bw.newLine();
            bw.write("\tpublic void set" + tempField + "(" + tempType + " " + _tempField + "){");
            bw.newLine();
            bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            bw.write("\tpublic " + tempType + " get" + tempField + "(){");
            bw.newLine();
            bw.write("\t\treturn this." + _tempField + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
        }
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }
 
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title buildMapper  
     * @author 苏志伟
     * @Description 构建Mapper文件
     * @throws IOException  void   
     * @date 2017年5月20日 下午2:21:14
     * @modifitor 苏志伟
     * @reason
     */
    private void buildMapper() throws IOException {
        File folder = new File(mapper_path);
        //文件夹不存在则创建文件夹
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
        
        File mapperFile = new File(mapper_path, mapperName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + mapper_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import org.apache.ibatis.annotations.Param;");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
        //      bw.write("public interface " + mapperName + " extends " + mapper_extends + "<" + beanName + "> {");
        bw.write("public interface " + mapperName + "{");
        bw.newLine();
        bw.newLine();
        // ----------定义Mapper中的方法Begin----------

        bw = buildMethodComment(bw, "根据对象的不为null的值作为条件进行查找");
        bw.newLine();
        bw.write("\t List<" + beanName + ">  selectSelective(T record);");
        bw.newLine();
        bw = buildMethodComment(bw, "动态分页，筛选条件 - 默认为最新时间排序");
        bw.newLine();
        bw.write("\t List<" + beanName + ">  selectPageSelective(@Param(\"pageDto\") PageDto<" + beanName + "> pageDto);");
        bw.newLine();
        bw = buildMethodComment(bw, "查找数据总记录数");
        bw.newLine();
        bw.write("\t" + "Integer selectDataCountSize();");
        bw.newLine();
        bw = buildMethodComment(bw, "根据对象的不为null的值作为条件进行删除");
        bw.newLine();
        bw.write("\t" + "Integer deleteSelective( " + beanName + " record );");
        bw.newLine();
        bw = buildMethodComment(bw, "添加数据，只添加不为null的数据，其余数据则为null");
        bw.newLine();
        bw.write("\t" + "Integer insertSelective( " + beanName + " record );");
        bw.newLine();
        bw = buildMethodComment(bw, "更新不为null的数据，不会将其他字段更新为null");
        bw.newLine();
        bw.write("\t" + "Integer updateSelective(@Param(\"updateRecord\") T updateRecord,@Param(\"conditionRecord\") T conditionRecord);");
        bw.newLine();
 
        // ----------定义Mapper中的方法End----------
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }
 
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title buildMapperXml  
     * @author 苏志伟
     * @Description 构建对应的Mybatis Xml 映射文件
     * @param columns
     * @param types
     * @param comments
     * @throws IOException  void   
     * @date 2017年5月20日 下午2:33:33
     * @modifitor 苏志伟
     * @reason
     */
    private void buildMapperXml( List<String> columns, List<String> types, List<String> comments ) throws IOException {
        File folder = new File(xml_path);
        //文件夹不存在则创建文件夹
        if ( !folder.exists() ) {
            folder.mkdirs();
        }
 
        File mapperXmlFile = new File(xml_path, mapperName + ".xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        //基础信息
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        bw.newLine();
        bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + mapper_package + "." + mapperName + "\">");
        bw.newLine();
        bw.newLine();
        
        //实体映射
        bw.write("\t<!--实体映射-->");
        bw.newLine();
        bw.write("\t<resultMap id=\"" + "BaseResultMap\" type=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t<!--" + comments.get(0) + "-->");
        bw.newLine();
        //第一个属性
        bw.write("\t\t<id column=\"" + columns.get(0) + "\" jdbcType=\"" + processTypeNum(types.get(0))  + "\" property=\"" + this.processField(columns.get(0)) + "\" />");
        bw.newLine();
        int size = columns.size();
        //接下来的所有属性
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t\t<!--" + comments.get(i) + "-->");
            bw.newLine();
            bw.write("\t\t<id column=\"" + columns.get(i) 
            			+ "\" jdbcType=\"" + processTypeNum(types.get(i))
            			+ "\" property=\"" + this.processField(columns.get(i)) + "\" />");

            bw.newLine();
        }
        
        bw.write("\t</resultMap>");
        bw.newLine();
        bw.newLine();
        bw.newLine();
 
        // 下面开始写SqlMapper中的方法
        // this.outputSqlMapperMethod(bw, columns, types);
        buildSQL(bw, columns, types);
 
        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title buildSQL  
     * @author 苏志伟
     * @Description 对应的方法SQL编写
     * @param bw
     * @param columns
     * @param types
     * @throws IOException  void   
     * @date 2017年5月20日 下午3:08:13
     * @modifitor 苏志伟
     * @reason
     */
    private void buildSQL( BufferedWriter bw, List<String> columns, List<String> types ) throws IOException {
        int size = columns.size();
        // 通用结果列
        bw.write("\t<!-- 通用的字段 -->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");
        bw.newLine();
 
        bw.write("\t\t id,");
        for ( int i = 1 ; i < size ; i++ ) {
            bw.write("\t" + columns.get(i));
            if ( i != size - 1 ) {
                bw.write(",");
            }
        }
 
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
 
        
        // 查询 根据对象的值不为null的进行查找
        String tempField = null;
        bw.write("\t<!-- 根据对象的不为null的值作为条件进行查找 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectSelective\" resultMap=\"BaseResultMap\" parameterType=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();
        tempField = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t and " + columns.get(i) + " = #{" + tempField + "," +  " jdbcType=" + processTypeNum(types.get(i)) + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完
        
        //查找 动态分页，筛选条件 - 默认为最新时间排序
        bw.write("\t<!-- 动态分页，筛选条件 - 默认为最新时间排序 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectPageSelective\" resultMap=\"BaseResultMap\" parameterType=\"" + "com.datacenter.entity.dto.page.PageDto" + "\" >");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();
        bw.write("\t\t\t<if test=\"" + "pageDto.filtrate" + " != null\">");
        bw.newLine();
        bw.write("\t\t\t\t<foreach item=\"value\" index=\"key\" collection=\"pageDto.filtrate.entrySet()\" >");
        bw.newLine();
        bw.write("\t\t\t\t and ${key} = #{value}");
        bw.newLine();
        bw.write("\t\t\t\t</foreach>");
        bw.newLine();
        bw.newLine();
        bw.write("\t\t\t</if>");
        
        bw.write("\t\t<!-- 排序条件 - 默认为最新更新时间 -->");
        bw.newLine();
        bw.write("\t\t ORDER BY");
        bw.newLine();
        bw.write("\t\t<!-- 排序字段 -->");
        bw.newLine();
        bw.write("\t\t<choose>");
        bw.newLine();
        bw.write("\t\t\t<when test=\"null != pageDto.orderField \">");
        bw.newLine();
        bw.write("\t\t\t\t#{pageDto.orderField}");
        bw.newLine();
        bw.write("\t\t\t</when>");
        bw.newLine();
        bw.write("\t\t\t<otherwise>");
        bw.newLine();
        bw.write("\t\t\t\t update_time");
        bw.newLine();
        bw.write("\t\t\t</otherwise>");
        bw.newLine();
        bw.write("\t\t</choose>");
        bw.newLine();
        bw.write("\t\t<!-- 排序规则 -->");
        bw.newLine();
        bw.write("\t\t<choose>");
        bw.newLine();
        bw.write("\t\t\t<when test=\"null != pageDto.orderingRule \">");
        bw.newLine();
        bw.write("\t\t\t\t #{pageDto.orderingRule}");
        bw.newLine();
        bw.write("\t\t\t</when>");
        bw.newLine();
        bw.write("\t\t\t<otherwise>");
        bw.newLine();
        bw.write("\t\t\t\t DESC");
        bw.newLine();
        bw.write("\t\t\t</otherwise>");
        bw.newLine();
        bw.write("\t\t</choose>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完
        
        //查找 数据总记录数 
        bw.write("\t<!-- 根据对象的不为null的值作为条件进行查找 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectDataCountSize\" resultType=\"" + "java.lang.Integer" + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t count(id)");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完
        
        // 删除  根据对象中不为null的值进行删除
        bw.write("\t<!-- 根据对象中不为null的值进行删除 -->");
        bw.newLine();
        bw.write("\t<delete id=\"deleteSelective\" parameterType=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t DELETE FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();
        tempField = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t and " + columns.get(i) + " = #{" + tempField + "," +  " jdbcType=" + processTypeNum(types.get(i)) + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完
 
        //---------------  insert方法（匹配有值的字段）
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        bw.write("\t<insert id=\"insertSelective\" parameterType=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName);
        bw.newLine();
        bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();
 
        
        tempField = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
 
        bw.newLine();
        bw.write("\t\t </trim>");
        bw.newLine();
 
        bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();
 
        tempField = null;
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
 
        bw.write("\t\t </trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        //---------------  完毕
 
 
        // 修改update方法
        bw.write("\t<!-- 修 改-->");
        bw.newLine();
        bw.write("\t<update id=\"updateSelective\" parameterType=\"" + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t UPDATE " + tableName);
        bw.newLine();
        bw.write(" \t\t <set> ");
        bw.newLine();
 
        tempField = null;
        for ( int i = 1 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"updateRecord != null and updateRecord." + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + " = #{updateRecord." + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
 
        bw.newLine();
        bw.write(" \t\t </set>");
        bw.newLine();
        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();
        tempField = null;
        
        for ( int i = 0 ; i < size ; i++ ) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"conditionRecord != null and conditionRecord." + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t and " + columns.get(i) + " = #{conditionRecord." + tempField + "," +  " jdbcType=" + processTypeNum(types.get(i)) + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update方法完毕
 
        bw.newLine();
    }
    
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title processType  
     * @author 苏志伟
     * @Description 处理数据类型，值留下类型   varchar(20) = varchar  
     * @param type
     * @return  String   
     * @date 2017年5月20日 下午4:01:56
     * @modifitor 苏志伟
     * @reason
     */
    private String processTypeNum(String type) {
    	type = type.replaceAll("([0-9])", "").replace("(", "").replace(")", "").trim().toUpperCase();
    	if(type.equalsIgnoreCase("int")){
    		type = "INTEGER";
    	}
    	if(type.equalsIgnoreCase("text")){
    		type = "VARCHAR";
    	}
    	return type;
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title getTableComment  
     * @author 苏志伟
     * @Description  获取所有的数据库表注释
     * @return
     * @throws SQLException  Map<String,String>   
     * @date 2017年5月20日 下午1:56:45
     * @modifitor 苏志伟
     * @reason
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = conn.prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while ( results.next() ) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }
 
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title generate  
     * @author 苏志伟
     * @Description 生成，Bean文件、Dao文件、Xml文件
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException  void   
     * @date 2017年5月20日 下午1:57:02
     * @modifitor 苏志伟
     * @reason
     */
    public void generateForDatabase() throws ClassNotFoundException, SQLException, IOException {
        init(); //初始化数据库连接
        String prefix = "show full fields from "; //查找某张表的所有字段信息，字段名称、字段类型、字段备注
        List<String> columns = null;//字段名称
        List<String> types = null;//字段类型
        List<String> comments = null;//字段备注
        PreparedStatement pstate = null; 
        List<String> tables = getTables(); //获取到数据库的所有表
        Map<String, String> tableComments = getTableComment();//获取到所有表的注释
        for ( String table : tables ) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            //查找某张表的所有字段信息，字段名称、字段类型、字段备注
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while ( results.next() ) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTableName(table);//处理获取到的这张表名称，转为为驼峰命名
            //this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            //创建对应的EntityBean
            buildEntityBean(columns, types, comments, tableComment);
            buildMapper();//构建Mapper文件
            //构建对应的Mybatis Xml 映射文件
            buildMapperXml(columns, types, comments);
        }
        conn.close();
    }
    
    /**
     * 
     * @project TEST_JAR
     * @file MyBatisGenerator.java
     * @package com.test.jar.mybatis  
     * @Title generateForTable  
     * @author 苏志伟
     * @Description 只生成部分表的，Bean文件、Dao文件、Xml文件
     * @param tableNames
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException  void   
     * @date 2017年5月20日 下午4:12:46
     * @modifitor 苏志伟
     * @reason
     */
    public void generateForTable(List<String> tableNames) throws ClassNotFoundException, SQLException, IOException {
        init(); //初始化数据库连接
        String prefix = "show full fields from "; //查找某张表的所有字段信息，字段名称、字段类型、字段备注
        List<String> columns = null;//字段名称
        List<String> types = null;//字段类型
        List<String> comments = null;//字段备注
        PreparedStatement pstate = null; 
        Map<String, String> tableComments = getTableComment();//获取到所有表的注释
        for ( String table : tableNames ) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            //查找某张表的所有字段信息，字段名称、字段类型、字段备注
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while ( results.next() ) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTableName(table);//处理获取到的这张表名称，转为为驼峰命名
            //this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            //创建对应的EntityBean
            buildEntityBean(columns, types, comments, tableComment);
            buildMapper();//构建Mapper文件
            //构建对应的Mybatis Xml 映射文件
            buildMapperXml(columns, types, comments);
        }
        conn.close();
    }
 
    public static void main( String[] args ) {
    	
        try {
            new MyBatisGenerator().generateForDatabase();
            // 自动打开生成文件的目录
            Runtime.getRuntime().exec("cmd /c start explorer E:\\");
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        } catch ( SQLException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }

    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
