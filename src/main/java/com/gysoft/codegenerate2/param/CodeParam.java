package com.gysoft.codegenerate2.param;

import com.gysoft.codegenerate2.valid.Valid;
import lombok.Data;

import java.util.List;

/**
 * @author 万强
 * @date 2019/5/31 16:47
 * @desc 代码生成参数
 */
@Data
public class CodeParam {

    @Valid(emptyable = false, message = "listParam不能为空")
    private List<TableParam> listParam;

}
