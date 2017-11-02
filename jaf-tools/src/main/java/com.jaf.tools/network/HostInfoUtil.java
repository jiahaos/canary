package com.jaf.tools.network;

import java.lang.management.ManagementFactory;
import java.util.Set;

/**
 * @author jiahao
 * @Package com.jaf.tools.network
 * @Description: TODO
 * @date 2017/11/2 9:16
 */
public class HostInfoUtil {

    public static String getPID() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

    public static String getIP() {
        Set<String> ips = LocalIpAddressUtil.resolveLocalIps();
        if (ips == null || ips.size() == 0) {
            return "UNKNOWN HOST";
        }
        return ips.iterator().next();
    }
}
