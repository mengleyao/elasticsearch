import java.net.InetAddress;
import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

public class TestES {
    private TransportClient client;

    @Before
    public void testES() {
        try {
            //设置集群名称

            Settings settings = Settings.builder().put("cluster.name", "test").build();
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("116.196.95.212"), 9300));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看集群信息
     */
    @Test
    public void testInfo() {
        List<DiscoveryNode> nodes = client.connectedNodes();
        for (DiscoveryNode node : nodes) {
            System.out.println(node.getHostAddress());
        }
    }

}

