package com.gysoft.codegenerate2.controller;

import com.alibaba.fastjson.JSONObject;
import com.gysoft.codegenerate2.disconf.DisconfBeanFactoryPostProcessor;
import com.gysoft.codegenerate2.handler.GenerateTableSqlHandler;
import com.gysoft.codegenerate2.param.CodeParam;
import com.gysoft.codegenerate2.service.CodeService;
import com.gysoft.codegenerate2.util.IDUtils;
import com.gysoft.codegenerate2.util.ThreadLocalUtils;
import com.gysoft.codegenerate2.util.ZipUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author 万强
 * @date 2019/5/30 15:50
 * @desc code生成Controller
 */
@RestController
@RequestMapping("code")
public class CodeController {

    @Autowired
    private GenerateTableSqlHandler handler;

    @Autowired
    private CodeService codeService;

    @Resource
    private HttpServletRequest request;

    @Resource
    private HttpServletResponse response;

    /**
     * 批量生成代码
     * @param codeParam
     * @return
     */
    @PostMapping("/test1")
    public JSONObject test1(@RequestBody CodeParam codeParam) {
        System.out.println(codeParam);
        JSONObject jsonObject = new JSONObject();
        if (CollectionUtils.isEmpty(codeParam.getListParam())) {
            jsonObject.put("status",201);
            jsonObject.put("msg","参数不能为空！");
            return jsonObject;
        }

        //文件识别码，用于后续文件下载和删除
        String id = IDUtils.getId();
        ThreadLocalUtils.set("fileId",id);
        codeParam.getListParam().forEach(TableParam -> {
            handler.handler2(TableParam);
        });

        jsonObject.put("status",200);
        jsonObject.put("msg","操作成功！");
        //将fileId返回给前端，用于后续文件下载和删除
        jsonObject.put("fileId",id);

        //请求完成，删除缓存
        ThreadLocalUtils.remove("fileId");
        return jsonObject;
    }

    /**
     * 下载指定fileId为文件，下载后为zip压缩文件，需要自行解压。
     * 同时将删除本地源文件，避免文件积压占用空间
     * @param fileId
     */
    @GetMapping("/download")
    public void download(String fileId) {
        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + fileId + ".zip");
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            /*//本地压缩文件后下载，缺点是会在本地留存一份，客户端下载成功后还得自行删除
            ZipUtils.toZip(ZipUtils.sourcePath + File.separator + fileId,
                    ZipUtils.targetPath,
                    fileId + ".zip", true);
            //下载压缩文件
            File file = new File(ZipUtils.targetPath + File.separator + fileId + ".zip" );
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());*/

            //直接在线压缩，然后输出到客户端，不会有本地留存文件
            ZipUtils.toZip(ZipUtils.sourcePath + File.separator + fileId,
                    response.getOutputStream(),
                    true
            );

            //输出到客户端后删除table目录下的源文件夹，避免文件积压占用空间
            File file = new File(ZipUtils.sourcePath + File.separator + fileId);
            FileUtils.deleteDirectory(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据选择的库查询该库所有表名
     * @param dataBase
     * @return
     */
    @GetMapping("/getListTableName")
    public List<Map<String,String>> getListTableName(@RequestParam(defaultValue = "[]") String[] dataBase) {

        List<Map<String,String>> listTableName = new ArrayList<>();

        Arrays.stream(dataBase).forEach(db -> {
            List<Map<String,String>> collect = codeService.getListTableName(db);
            listTableName.addAll(collect);
        });

        return listTableName;
    }

    /**
     * 获取配置的所有数据库
     * @return
     */
    @GetMapping("/getListDataBase")
    public Set<String> getListDataBase() {

        Set<String> disconfDataBase = DisconfBeanFactoryPostProcessor.getDisconfDataBase();

        return disconfDataBase;
    }

}
