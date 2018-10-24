package onCommitDeployer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.*;

import java.io.InputStream;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 7000;
    public static void main(String[] args) throws Throwable {
        AppContainer container = new AppContainer();
        container.rebuild();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", (HttpExchange t) ->
        {
            if(t.getRequestHeaders().getFirst("X-GitHub-Event").equals("push")) {
                try (InputStream requestBodyStream = t.getRequestBody()) {
                    String requestBody = IOUtils.toString(requestBodyStream, "utf-8");
                    JSONObject reqJson = new JSONObject(requestBody);
                    System.out.println("Rebuild after push from " + reqJson.getJSONObject("pusher").getString("name"));
                    container.rebuild();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        server.setExecutor(null);
        server.start();
    }
}
