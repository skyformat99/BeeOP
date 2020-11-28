/*
 * Copyright Chris2018998
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.beeop;

import static cn.beeop.StaticCenter.ObjectClosedException;

/**
 * Object Proxy
 *
 * @author Chris.Liao
 * @version 1.0
 */
public class ProxyObject {
    private Object delegate;
    private PooledEntry pConn;
    private boolean isClosed;

    public ProxyObject(PooledEntry pConn) {
        this.pConn = pConn;
        pConn.proxyConn = this;
        this.delegate = pConn.object;
    }

    public Object getDelegate() throws ObjectException {
        checkClosed();
        return delegate;
    }

    public boolean isClosed() throws ObjectException {
        return isClosed;
    }

    protected void checkClosed() throws ObjectException {
        if (isClosed) throw ObjectClosedException;
    }

    public final void close() throws ObjectException {
        synchronized (this) {//safe close
            if (isClosed) return;
            isClosed = true;
            //delegate = CLOSED_CON;
        }
        pConn.recycleSelf();
    }

    final void trySetAsClosed() {//called from FastConnectionPool
        try {
            close();
        } catch (ObjectException e) {
        }
    }
}