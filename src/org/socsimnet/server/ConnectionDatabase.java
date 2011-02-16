package org.socsimnet.server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Set;

/**
 * Organization: Sociotechnical Systems Engineering Institute
 * www: http://socsimnet.com/
 * User: artis
 * Date: 2/14/11
 * Time: 12:29 AM
 */
public class ConnectionDatabase {
    private HashMap<Integer, Socket> connections;

    public ConnectionDatabase(HashMap<Integer, Socket> connections) {
        this.connections = connections;
    }

    public ConnectionDatabase() {
        this.connections = new HashMap<Integer, Socket>();
    }

    public void put(Integer key, Socket value) {
        this.connections.put(key, value);
    }

    public Object get(Integer key) {
        return this.connections.get(key);
    }

    public Set<Integer> getKeySet() {
        return this.connections.keySet();
    }
}
