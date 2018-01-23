import com.zly.elasticsearch.common.utils.APP;
import com.zly.elasticsearch.entity.TaskInfo;
import com.zly.elasticsearch.service.ElasticSearchService;
import org.apache.log4j.Logger;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestMain {
    private static final Logger logger = Logger.getLogger(TestMain.class);

    public void start() {
        ClassPathXmlApplicationContext context = null;
        try {
            context = new ClassPathXmlApplicationContext("classpath:app.xml");
        } catch (Exception e) {
            logger.error("An error occurred, applicationContext will close.", e);
            if (context != null) {
                context.close();
            }
            context = null;
            logger.error(APP.CLOSED_MSG);
        }
    }

    /**
     * 插入
     */
    @Test
    public void insertNo() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:app.xml");
        ElasticSearchService service = context
                .getBean(ElasticSearchService.class);
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        for (int i = 0; i < 20; i++) {
            taskInfoList.add(new TaskInfo(String.valueOf((i + 5)), i + 5, "高国藩"
                    + i, "taskArea", "taskTags", i + 5, "1996-02-03", "霍华德"));
        }
        service.insertOrUpdateTaskInfo(taskInfoList);
    }

    /**
     * 查询
     */
    @Test
    public void serchNo() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:app.xml");
        ElasticSearchService service = context
                .getBean(ElasticSearchService.class);
        List<Map<String, Object>> al = service.queryForObject("task_info",
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
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:app.xml");
        ElasticSearchService service = context
                .getBean(ElasticSearchService.class);
        List<Map<String, Object>> al = service.queryForObjectForElasticSerch("task_info", "taskContent", "高",19,20);

        for (int i = 0; i < al.size(); i++) {
            System.out.println(al.get(i));
        }

    }
}
