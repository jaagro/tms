package com.jaagro.tms.biz.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;
import java.util.List;

/**
 * @author tony
 */
@FeignClient("component")
public interface OssSignUrlClientService {

    /**
     * 获取oss图片地址
     * @param filePath
     * @return
     */
    @PostMapping("/listSignedUrl")
    List<URL> listSignedUrl(@RequestParam("filePath") String[] filePath);
}
