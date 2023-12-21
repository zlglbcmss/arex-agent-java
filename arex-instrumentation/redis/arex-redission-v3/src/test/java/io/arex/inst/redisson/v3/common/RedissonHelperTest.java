package io.arex.inst.redisson.v3.common;

import static org.junit.jupiter.api.Assertions.assertNull;
import io.arex.agent.bootstrap.util.ReflectUtil;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.Redisson;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.connection.ConnectionManager;

@ExtendWith(MockitoExtension.class)
public class RedissonHelperTest {

    @BeforeAll
    static void setUp() {
        Mockito.mockStatic(ReflectUtil.class);
        Mockito.when(ReflectUtil.getMethod(ConnectionManager.class, "getConfig")).thenReturn(null);
        try {
            Mockito.when(ReflectUtil.getMethod(Redisson.class, "getCommandExecutor"))
                .thenReturn(Redisson.class.getMethod("getCommandExecutor", null));
        } catch (NoSuchMethodException e) {
            Mockito.when(ReflectUtil.getMethod(Redisson.class, "getCommandExecutor")).thenReturn(null);
        }
    }
    @AfterAll
    static void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
    void testRedisUri() {
        ConnectionManager connectionManager = Mockito.mock(ConnectionManager.class);
        MasterSlaveServersConfig masterSlaveServersConfig = new MasterSlaveServersConfig();
        masterSlaveServersConfig.setMasterAddress("104.34.56:0000");
        Mockito.lenient().when(connectionManager.getConfig()).thenReturn(masterSlaveServersConfig);
        assertNull(RedissonHelper.getRedisUri(connectionManager));

        mockRedissionHelperSet();
        assertNull(RedissonHelper.getRedisUri(connectionManager));
    }

    @Test
    void testCacheRedisUri() {
        ConnectionManager connectionManager = Mockito.mock(ConnectionManager.class);
        assertNull(RedissonHelper.getRedisUri(connectionManager));

        mockRedissionHelperSet();
        assertNull(RedissonHelper.getRedisUri(connectionManager));
    }

    @Test
    void testGetMasterSlaveServersConfig() {
        ConnectionManager connectionManager = Mockito.mock(ConnectionManager.class);
        assertNull(RedissonHelper.getMasterSlaveServersConfig(connectionManager));
    }

    private void mockRedissionHelperSet() {
        // mock static final field
        try {
            Field field = RedissonHelper.class.getDeclaredField("masterSlaveServersConfigSet");
            field.setAccessible(true);
            Set<MasterSlaveServersConfig> set = new HashSet<>(1);
            set.add(new MasterSlaveServersConfig());
            field.set(null, set);
        } catch (Exception e) {
            // doNothing
        }
    }

    @Test
    void getCommandAsyncExecutor() throws NoSuchMethodException {
        assertNull(RedissonHelper.getCommandAsyncExecutor(null));

        Redisson redisson1 = Mockito.mock(Redisson.class);
        assertNull(RedissonHelper.getCommandAsyncExecutor(redisson1));
    }
}