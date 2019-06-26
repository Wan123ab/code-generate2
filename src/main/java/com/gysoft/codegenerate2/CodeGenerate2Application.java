package com.gysoft.codegenerate2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
//Mybatis映射路径
@MapperScan("com.gysoft.codegenerate2.dao")
public class CodeGenerate2Application {

    public static void main(String[] args) {
        SpringApplication.run(CodeGenerate2Application.class, args);
    }

}
