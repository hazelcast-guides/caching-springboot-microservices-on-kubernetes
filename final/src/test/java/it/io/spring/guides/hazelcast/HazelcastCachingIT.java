
import guides.hazelcast.spring.CommandResponse;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class HazelcastCachingIT {

    private static String clusterUrl;

    @BeforeClass
    public static void oneTimeSetup() {
        String clusterIp = "localhost"; 
        String nodePort = "31000";
        clusterUrl = "http://" + clusterIp + ":" + nodePort ;
    }


    @Test(timeout = 60000)
    public void testHazelcastCache() throws Exception {

        String key = "key1";
        String value = "hazelcast-spring-guide";

        String put_url = String.format(clusterUrl +"/put?key=%s&value=%s", key, value);
        RestTemplate rest = new RestTemplate();

        ResponseEntity<CommandResponse> putResponse = rest.getForEntity(put_url, CommandResponse.class);
        Assert.assertTrue(putResponse.getStatusCode() == HttpStatus.OK);

        String firstPod = putResponse.getBody().getPodName();

        //GET call to see data is coming from another pod
        String get_url = String.format(clusterUrl+"/get?key=%s", key);

        while (true) { // it will try every second until it timeouts in 60 seconds
            ResponseEntity<CommandResponse> getResponse = rest.getForEntity(get_url, CommandResponse.class);
            String secondPod = getResponse.getBody().getPodName();
            if (!secondPod.equals(firstPod)) break; // we get the response from different pod so SUCCESS!!
            Thread.sleep(1000);
        }
    }

}

