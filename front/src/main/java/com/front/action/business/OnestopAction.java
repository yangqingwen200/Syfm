package com.front.action.business;

import com.commons.bean.drivingschool.DsDrivingSchool;
import com.commons.constant.CommonConstant;
import com.commons.redis.RedisUtil;
import com.front.constant.SQLConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("system.pc.action.business.onestopAction")
@Scope("prototype")
public class OnestopAction extends BusinessAction {

    private static final long serialVersionUID = 1081681641584848916L;
    private static final Logger LOG = LoggerFactory.getLogger(OnestopAction.class);
    private String menuIndex = "onestop";

    public String list() {
        try {
           String findSchool = "SELECT id, name FROM `ds_driving_school` WHERE disabled=? AND STATUS =? limit 100";
           List<Object> sqlListMap = RedisUtil.getListByKey("schoolList", findSchool, new Object[]{1, 3}, 3 * 3600);
           this.request.setAttribute("schoolList", sqlListMap);
           page = this.publicService.pagedQuerySqlFreemarker(page, SQLConstant.PC_FIND_ONETOP_DRIVINGSCHOOL, dto);
           return SUCCESS;
        } catch (Exception e) {
            this.checkActionError(e);
            return ERROR;
        }
    }

    /**
     * 跳转至新增页面 和 新增数据同一个方法 <br>
     * 通过请求方法(get or post)来判断是跳转至新增 还是 新增数据
     *
     * @return 一般如果返回JSON的话, return NONE 即可
     */
   public String add() {
       try {
           if(isGetRequest) {
               return SUCCESS;
           }
       } catch (Exception e) {
           this.checkException(e);
       }
       this.printJson();
       return NONE;
   }

    /**
     * 跳转至编辑页面 和 编辑数据同一个方法 <br>
     * 通过请求方法(get or post)来判断是跳转至编辑页面 还是 编辑数据提交
     *
     * @return 一般如果返回JSON的话, return NONE 即可
     */
    public String edit() {
        try {
            if (isGetRequest) {
                DsDrivingSchool dds = this.publicService.load(DsDrivingSchool.class, dto.getAsLong("id"));
                this.request.setAttribute("dds", dds);
                return SUCCESS;
            }
            Integer id = dto.getAsInteger("id");
            String name = dto.getAsStringTrim("name");
            String link_tel = dto.getAsStringTrim("linkTel");
            this.publicService.executeUpdateSql("update ds_driving_school set name=?, link_tel=? where id=?", name, link_tel, id);

            //清空下redis中缓存
            this.cacheRedis.del(CommonConstant.REDIS_UTIL_MAP_PREFIX + dto.getAsInteger("id"));
        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
        return NONE;
    }

    /**
     * 禁用驾校
     *
     * @return 一般如果返回JSON的话, return NONE 即可
     */
    public String forbidden() {
        try {
            Integer status = dto.getAsInteger("status");
            String paramId = dto.getAsStringTrim("id");
            String[] split = paramId.split(",");
            for (String s : split) {
                int id = Integer.parseInt(s);
                this.publicService.executeUpdateSql("update ds_driving_school set status=? where id=?", status, id);
                //清空下redis中缓存
                this.cacheRedis.del(CommonConstant.REDIS_UTIL_MAP_PREFIX + id);
            }

        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
        return NONE;
    }

    /**
     * 查看详情
     *
     * @return NONE
     */
    public String detail() {
        try {
            //使用对象方式, 一般很少使用直接load, 会把全部字段加载出来, 建议使用什么字段就查询什么字段
            //DsDrivingSchool dds = this.publicService.load(DsDrivingSchool.class, dto.getAsLong("id"));

            //使用Map形式, 存在redis中
            String sql = "select ds.name, ds.link_tel as linkTel, ds.address from ds_driving_school ds where ds.id=?";
            Map<String, Object> schoolDetail = RedisUtil.getMapByKey(dto.getAsLong("id"), sql,
                    new Object[]{dto.getAsLong("id")}, 600);
            this.request.setAttribute("dds", schoolDetail);
            return SUCCESS;
        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
        return NONE;
    }

    /**
     * 记得清除下redis缓存
     */
    public void delete() {
        try {
            String sql = "update ds_driving_school set disabled = ? where id = ?";
            List<Object[]> param = new ArrayList<>();
            String paramId = dto.getAsStringTrim("id");
            String[] ids = paramId.split(",");
            List<Object> list = new ArrayList<>();
            for (String id : ids) {
                list.add(0);
                list.add(Integer.parseInt(id));
                param.add(list.toArray());
                list.clear();
            }
            this.publicService.executeBatchUpdateSql(sql, param);
        } catch (Exception e) {
            this.checkException(e);
        }
        this.printJson();
    }

    public String getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(String menuIndex) {
        this.menuIndex = menuIndex;
    }
}
