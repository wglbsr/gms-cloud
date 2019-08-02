package ${classPath};


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPath}.${entityName};
import ${servicePath}.${entityName}Service;
import com.dyny.common.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
*
* @author wanggl
* @since 2019-03-12
*/
@RestController
@RequestMapping(value = "/${entityName}", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class ${entityName}Controller extends BaseController {
@Autowired
${entityName}Service ${entityNameLower}Service;

@RequestMapping("/select/{id}")
public String selectById(@PathVariable("id") int ${entityNameLower}Id) {
return getSuccessResult(${entityNameLower}Service.getById(${entityNameLower}Id));
}


@RequestMapping("/select")
public String select(@RequestParam(KEY_KEY_WORD) String keyWord,
@RequestParam(KEY_PAGE_NUM) int pageNum,
@RequestParam(KEY_PAGE_SIZE) int pageSize) {
QueryWrapper<${entityName}> ${entityNameLower}QueryWrapper = new QueryWrapper<>();
${entityNameLower}QueryWrapper.like("name", keyWord).or()
.like("address", keyWord);
return getSuccessResult(${entityNameLower}Service.page(new Page<>(pageNum, pageSize), ${entityNameLower}QueryWrapper));
}

@RequestMapping("/delete/{id}")
public String delete(@PathVariable("id") int ${entityNameLower}Id) {
return getSuccessResult(${entityNameLower}Service.removeById(${entityNameLower}Id));
}


@RequestMapping("/modify")
public String modify(@RequestBody ${entityName} ${entityNameLower}) {
return getSuccessResult(${entityNameLower}Service.updateById(${entityNameLower}));
}

@RequestMapping("/create")
public String create(@RequestBody ${entityName} ${entityNameLower}) {
return getSuccessResult(${entityNameLower}Service.save(${entityNameLower}) ? ${entityNameLower}.getId() : 0);
}
}

