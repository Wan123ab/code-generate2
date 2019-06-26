package com.gysoft.codegenerate2.handler;

import com.gysoft.codegenerate2.param.TableParam;
import com.gysoft.codegenerate2.util.IDUtils;
import com.gysoft.codegenerate2.util.ThreadLocalUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成服务类
 * @author 万强
 * @date 2019/6/2 10:33
 */
@Service
public class GenerateTableSqlHandler {
	@Resource
	private CreatePoJoHandler createPoJoHandler;
	@Resource
	private CreateDaoHandler createDaoHandler;
	@Resource
	private CreateDaoImplHandler createDaoImplHandler;
	@Resource
	private CreateServiceHandler createServiceHandler;
	@Resource
	private CreateServiceImplHandler createServiceImplHandler;

	/**
	 * 注入操作系统属性
	 */
	@Value("#{systemProperties['user.dir']}")
	private String userDir;

	private static Map<String,String> mapProject = new HashMap<>();

	static{
		mapProject.put("gy_project",".project");
		mapProject.put("gy_account",".account");
		mapProject.put("gy_core",".core");
		mapProject.put("gy_model",".model");

	}

	public void handler2(TableParam param) {
		String baseFile = userDir + File.separator
				+ "table" + File.separator;

		//基本包路径，默认为"com.gysoft.impl"
		String basePackage = param.getBasePackage();
		String table = param.getTableName();
		String dataBase = param.getDataName();
		String dataSource = dataBase;
		//不要转换小写，去数据库查询需要区分大小写，转换小写将导致查不出数据
//		table = table.toLowerCase();
		//如果basePackage为空，默认"com.gysoft.impl"
		if(StringUtils.isEmpty(basePackage)){
			basePackage = "com.gysoft.impl";
		}
		//拼接项目名
		basePackage = basePackage + mapProject.get(dataBase);

		String pojoPath = createPoJoHandler.createPojoDynamic(basePackage, dataBase, dataSource, table, baseFile);
		System.out.println("pojo生成完毕【"+table+"】");

		String daoPath = createDaoHandler.createDaoDynamic(basePackage,pojoPath,dataBase,dataSource,table, baseFile);
		System.out.println("dao生成完毕【"+table+"】");

		createDaoImplHandler.createDaoImpl(basePackage,pojoPath, daoPath,dataBase,dataSource,table, baseFile);
		System.out.println("DaoImpl生成完毕【"+table+"】");

		String servicePath = createServiceHandler.createService(basePackage,dataBase,dataSource, table, baseFile);
		System.out.println("service生成完毕【"+table+"】");

		createServiceImplHandler.createServiceImpl(basePackage,servicePath,daoPath,dataBase,dataSource, table, baseFile);
		System.out.println("serviceImpl生成完毕【"+table+"】");

	}

}