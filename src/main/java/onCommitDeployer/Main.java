package onCommitDeployer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.*;
import spark.Spark;

import java.io.InputStream;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 7000;
    public static void main(String[] args) throws Throwable {
        AppContainer container = new AppContainer();
        container.rebuild();

        Spark.port(7000);
        Spark.post("/api/events", (req, res) -> {
            if(req.headers("X-GitHub-Event").equals("push")){
                JSONObject reqJson = new JSONObject(req.body());
                System.out.println("Rebuild after push from " + reqJson.getJSONObject("pusher").getString("name"));
                container.rebuild();
                return "ok";
            }
            return "";
        });
    }
}
