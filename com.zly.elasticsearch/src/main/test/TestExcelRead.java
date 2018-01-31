import com.zly.elasticsearch.common.utils.ReadExcelDataUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * 测试excel表格数据读取工具类
 *
 * @author zhangmeng0725
 */
public class TestExcelRead {

    @Test
    public void testReadExcel() {
        ReadExcelDataUtil excelData = new ReadExcelDataUtil();
        String excelPath = "D:\\test.xls";
        List<UserDO> listUserDO = new ArrayList<>();
        try {
            listUserDO = excelData.readExcelData(excelPath, 6);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listUserDO != null) {
            for (UserDO userDO : listUserDO) {
                System.out.println(userDO.toString());
            }
        } else {
            System.out.println("无数据");
        }
    }
}
