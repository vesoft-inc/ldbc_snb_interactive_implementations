package org.ldbcouncil.snb.impls.workloads.nebula;

import org.ldbcouncil.snb.impls.workloads.BaseDbConnectionState;
import com.vesoft.nebula.client.graph.NebulaPoolConfig;
import com.vesoft.nebula.client.graph.data.HostAddress;
import com.vesoft.nebula.client.graph.exception.AuthFailedException;
import com.vesoft.nebula.client.graph.exception.ClientServerIncompatibleException;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.exception.NotValidConnectionException;
import com.vesoft.nebula.client.graph.net.NebulaPool;
import com.vesoft.nebula.client.graph.net.Session;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NebulaDbConnectionState  extends BaseDbConnectionState<NebulaQueryStore> {

    protected final NebulaPool pool;

    private final ThreadLocal<Session> session = new ThreadLocal<>();

    private final String user;

    private final String password;

    private final String spaceName;

    private final boolean printErrors;

    public NebulaDbConnectionState(Map<String, String> properties, NebulaQueryStore queryStore) throws UnknownHostException {
        super(properties, queryStore);

        user = properties.get("user");
        password = properties.get("password");
        String endPoint = properties.get("endpoint");
        spaceName = properties.get("spaceName");
        printErrors = Boolean.parseBoolean(properties.getOrDefault("printErrors", "false"));
        int maxConnSize = Integer.parseInt(properties.getOrDefault("max_conn_size", "500"));

        NebulaPoolConfig nebulaPoolConfig = new NebulaPoolConfig();
        nebulaPoolConfig.setMaxConnSize(maxConnSize);
        List<HostAddress> addresses = new ArrayList<>();
        for (String ipPort : endPoint.split(",")) {
            String[] ipPortPair = ipPort.split(":");
            addresses.add(new HostAddress(ipPortPair[0], Integer.parseInt(ipPortPair[1])));
        }

        pool = new NebulaPool();
        pool.init(addresses, nebulaPoolConfig);
    }

    public Session getSession() {
        if (session.get() == null) {
            try {
                Session newSession = pool.getSession(user, password, false);
                // Currently we hard code to use sf300
                newSession.execute("USE " + spaceName);
                session.set(newSession);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return session.get();
    }

    @Override
    public void close() throws IOException {
        pool.close();
    }

    public boolean isPrintErrors() {
        return printErrors;
    }
}
