package cn.testin.controller;

import cn.testin.mapper.secin.SecinMapper;
import cn.testin.entry.result.ResultEntry;
import cn.testin.entry.result.ResultError;
import cn.testin.entry.result.ResultSuccess;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author gaojindan
 * @date 2018/8/9 17:15
 * @des
 */

@RequestMapping("/admin/leak")
@RestController
public class LeakController {
    private static Logger logger = LogManager.getLogger(LeakController.class);

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private SecinMapper secinMapper;

    /**
     *
     * @author Gaojindan
     * @date 2018/8/9 17:11
     * @des 读取应用的所有测试列表
     * @param: appMd5:app唯一标识
     * @param: dimension: 1:漏洞总数  2:风险等级 3:检测分类
     * @return
     */
    @RequestMapping(value = "/leakinfo", method = RequestMethod.GET)
    private ResultEntry startSync(@RequestParam(required = true, value = "appMd5") String appMd5,
                                  @RequestParam(required = true, value = "dimension")Integer dimension) {
        if (dimension < 1 || dimension > 3){
            return new ResultError(401,"参数错误");
        }
        List<HashMap<String,Object>> listDoc = new ArrayList<>();
        try  {
            MongoCollection<Document> mongoCollection = mongoTemplate.getCollection("apps");

            //查询条件
            Bson matchBson = Aggregates.match(Filters.eq("appInfo.md5", appMd5));
            Bson groupBson = Aggregates.group("$appInfo.versionName", Accumulators.max("createTime", "$analyzers.analysisEnd"));
            MongoCursor<Document> groupResults = mongoCollection.aggregate(
                    Arrays.asList(
                            matchBson,
                            groupBson
                    )
            ).iterator();
            //要查询的字段
            Bson fields = Aggregates.project(
                    Projections.fields(
                            Projections.include("appInfo"),
                            Projections.include("analyzers"),
                            Projections.include("summary"),
                            Projections.include("risks")
                    )
            );
            while (groupResults.hasNext()) {
                //聚合后的数据只能聚合字段,别的字段要单独猎取
                Document groupDoc = groupResults.next();
                String versionName = groupDoc.getString("_id");
                Integer createTime = groupDoc.getInteger("createTime");
                //查询条件
                Bson matchBson1 = Aggregates.match(Filters.eq("appInfo.versionName", versionName));
                Bson matchBson2 = Aggregates.match(Filters.eq("analyzers.analysisEnd", createTime));
                MongoCursor<Document> results = mongoCollection.aggregate(
                        Arrays.asList(
                                matchBson1,
                                matchBson2,
                                fields
                        )
                ).iterator();

                while (results.hasNext()) {
                    HashMap<String,Object> resultMap = new HashMap<>();
                    Document doc = results.next();
                    if (1 == dimension){
                        //漏洞总数
                        Document summaryDoc = (Document)doc.get("summary");
                        resultMap.put("riskCount",summaryDoc.getInteger("riskCount"));
                    }
                    else if (2 == dimension){
                        //风险等级
                        Document summaryDoc = (Document)doc.get("summary");
                        resultMap.put("level",summaryDoc.get("level"));
                    }
                    else if (3 == dimension){
                        //检测分类
                        HashMap<String,Integer> riskMap = new HashMap<>();
                        Document risksDoc = (Document)doc.get("risks");
                        Collection<Object> values = risksDoc.values();
                        for (Object value : values){
                            Document riskDate = (Document)value;
                            String type = riskDate.getString("type");
                            Integer tmpNum = riskMap.get(type);
                            if (null == tmpNum){
                                //还没有添加过,可以添加了
                                tmpNum = 1;
                            }else{
                                tmpNum += 1;
                            }
                            riskMap.put(type,tmpNum);
                        }
                        resultMap.put("riskType",riskMap);
                    }
                    listDoc.add(resultMap);
                }
            }

        } catch (Exception ex) {
            logger.error("获得规则集信息时出错", ex);
        }
        ResultSuccess resultSuccess = new ResultSuccess(listDoc);
        return resultSuccess;
    }

    /*
     * @author: gaojindan
     * @date: 2018/5/22 下午2:33
     * @des: 获取APP列表
     * @param:
     * @return:
     */
    @RequestMapping(value = "/applist", method = RequestMethod.GET)
    private ResultEntry appList() {
        try {
            List<Map<String,Object>> results = secinMapper.getAppList();
            ResultSuccess resultSuccess = new ResultSuccess(results);
            return resultSuccess;
        } catch (Exception ex) {
            logger.error("写入OP数据库时出错", ex);
            return new ResultError(500,"读取数据库错误");
        }
    }

}