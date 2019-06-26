package com.gysoft.codegenerate2.service;

import com.gysoft.codegenerate2.dao.DataBaseDAO;
import com.gysoft.codegenerate2.model.TableDO;
import com.gysoft.codegenerate2.multiDataSource.DynamicDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 万强
 * @date 2019/5/30 16:12
 * @desc
 */
@Service
public class CodeService {

    @Resource
    private DataBaseDAO dataBaseDao;

    private static Map<String,String> map = new HashMap<>();

    static{
        map.put("gy_project","gy_project");
        map.put("gy_account","gy_account");
        map.put("gy_core","gy_core");
        map.put("gy_model","gy_model");

    }

    /**
     * 根据dataBase查出所有表名
     * @param dataBase
     * @return
     */
    public List<Map<String,String>> getListTableName(String dataBase){

        //设置数据源
        DynamicDataSource.setDataSource(map.get(dataBase));

        List<Map<String,String>> list = new ArrayList<>();
        List<TableDO> listTableDO = dataBaseDao.getListTableName(dataBase);
        listTableDO.forEach(t -> {
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("dataBase",dataBase);
            resultMap.put("tableName",t.getTableName());
            list.add(resultMap);
        });

        return list;

    }



}
