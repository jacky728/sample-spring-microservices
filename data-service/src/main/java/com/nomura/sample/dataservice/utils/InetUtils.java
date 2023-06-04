package com.nomura.sample.dataservice.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@UtilityClass
public class InetUtils {
    public static String hostIp;
    public static String hostName;

    static {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            hostIp = localHost.getHostAddress();
            hostName = localHost.getHostName();
        } catch (UnknownHostException e) {
            log.warn("Failed to retrieve hostIp and hostName thru InetAddress");
        }

        String envHostname = System.getenv("HOSTNAME");
        if (StringUtils.isNotBlank(envHostname)) {
            log.info("Got and override hostName={} by environment variable HOSTNAME={}", hostName, envHostname);
            hostName = envHostname;     // Allow overridden by environment
        }
    }

}
