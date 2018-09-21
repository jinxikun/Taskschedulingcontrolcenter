package com.telecom.ccs.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class PropertiesConfig {

    @Value("${system.ftp.server}")
    private String system_ftp_server;

    @Value("${system.ftp.port}")
    private String system_ftp_port;

    @Value("${system.ftp.relativePath}")
    private String system_ftp_relativePath;

    @Value("${system.ftp.username}")
    private String system_ftp_username;

    @Value("${system.ftp.password}")
    private String system_ftp_password;

    @Value("${system.dispatch.shandong}")
    private boolean system_dispatch_shandong;

    @Value("${system.dispatch.beijing}")
    private boolean system_dispatch_beijing;

    @Value("${system.dispatch.hebei}")
    private boolean system_dispatch_hebei;






}
