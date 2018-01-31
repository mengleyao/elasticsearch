import com.zly.elasticsearch.entity.TaskInfo;
import com.zly.elasticsearch.service.ElasticsearchService;
import org.apache.log4j.Logger;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(locations={"classpath:application.xml"}) //加载配置文件
public class TestMain {

    private static final Logger logger = Logger.getLogger(TestMain.class);

    @Resource
    private ElasticsearchService elasticsearchService;
    /**
     * 插入
     */
    @Test
    public void insertNo() {
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        for (int i = 0; i < 20; i++) {
            taskInfoList.add(new TaskInfo(String.valueOf((i + 5)), i + 5, "高国藩"
                    + i, "taskArea", "taskTags", i + 5, "1996-02-03", "霍华德"));
        }
        elasticsearchService.insertOrUpdateTaskInfo(taskInfoList);
    }

    /**
     * 查询
     */
    @Test
    public void serchNo() {
        List<Map<String, Object>> al = elasticsearchService.queryForObject("task_info",
                new String[] { "taskContent", "taskArea" }, "高国藩", "taskArea", SortOrder.DESC,
                0, 2);
        for (int i = 0; i < al.size(); i++) {
            System.out.println(al.get(i));
        }

    }

    /**
     * filter查询
     */
    @Test
    public void serchFilter() {
        List<Map<String, Object>> al = elasticsearchService.queryForObjectForElasticSerch("task_info", "taskContent", "高",19,20);
        for (int i = 0; i < al.size(); i++) {
            System.out.println(al.get(i));
        }
    }
}
