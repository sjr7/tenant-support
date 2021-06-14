package com.suny.tenant.thread;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sunjianrong
 * @date 2021-06-07 14:22
 */
public final class PropertiesContext implements AutoCloseable {

    private static final ThreadLocal<Properties> PROPERTIES = new TransmittableThreadLocal<>();

    public static Properties getProperties() {
        final Properties properties = PROPERTIES.get();
        if (properties == null) {
            return new Properties();
        }
        return properties;
    }

    public static void setProperties(Properties properties) {
        PROPERTIES.set(properties);
    }

    public static void clear() {
        PROPERTIES.remove();
    }

    @Override
    public void close() throws Exception {
        clear();
    }


    public static void enableIgnoreTenantId() {
        final Properties properties = getProperties();
        properties.setIgnoreTenantId(true);
        setProperties(properties);
    }

    public static void disableIgnoreTenantId() {
        final Properties properties = getProperties();
        properties.setIgnoreTenantId(false);
        setProperties(properties);
    }


    public static class Properties {
        @Setter
        @Getter
        private boolean ignoreTenantId = false;

    }


}
