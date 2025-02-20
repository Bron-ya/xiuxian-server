package com.xx.xiuxianserver.Controller;

import com.xx.xiuxianserver.Common.Result.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author jiangzk
 */
@RestController
@RequestMapping("/login/gitee")
public class GiteeController {

  @Value("${login.gitee.clientId}")
  private String giteeClientId;

  @Value("${login.gitee.redirectUri}")
  private String giteeCallbackEndpoint;

  @GetMapping("/config")
  public ResponseResult<?> getA() {
    HashMap<String, Object> giteeConfig = new HashMap<>();
    giteeConfig.put("clientId", giteeClientId);
    giteeConfig.put("redirectUri", giteeCallbackEndpoint);
    return ResponseResult.okResult(giteeConfig, "传递 clientId 和 回调地址 成功");
  }
}