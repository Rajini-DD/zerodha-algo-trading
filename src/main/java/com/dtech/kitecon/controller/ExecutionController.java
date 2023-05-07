package com.dtech.kitecon.controller;

import com.dtech.kitecon.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ExecutionController {

  private final ExecutionService executionService;


  @GetMapping("/start/{type}/{strategyName}/{instrument}/{direction}/{quantity}/{candle}")
  @ResponseBody
  public String startStrategy(@PathVariable String type,@PathVariable String strategyName,
                              @PathVariable String instrument, @PathVariable String direction,
                              @PathVariable String quantity,@PathVariable String candle) throws InterruptedException {
    return executionService.startStrategy(type,strategyName, instrument, direction,quantity,candle);
  }


}
