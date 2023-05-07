package com.dtech.kitecon.config;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import java.io.IOException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KiteConnectConfig {

  @Value("${kite.api.key}")
  private String apiKey;

  @Value("${kite.api.user}")
  private String userId;

  @Value("${kite.api.secret}")
  private String secret;

  private KiteConnect kiteConnect;

  public final void initialize(String requestToken) throws KiteException, IOException {
    this.kiteConnect = new KiteConnect(apiKey,true);

    //If you wish to enable debug logs send true in the constructor, this will log request and response.
    //KiteConnect kiteConnect = new KiteConnect("xxxxyyyyzzzz", true);

    // If you wish to set proxy then pass proxy as a second parameter in the constructor with api_key. syntax:- new KiteConnect("xxxxxxyyyyyzzz", proxy).
    //KiteConnect kiteConnect = new KiteConnect("xxxxyyyyzzzz", userProxy, false);

    // Set userId
    kiteConnect.setUserId(userId);

    // Get login url
    String url = kiteConnect.getLoginURL();

    User user = kiteConnect.generateSession(requestToken, secret);
    kiteConnect.setAccessToken(user.accessToken);
    kiteConnect.setPublicToken(user.publicToken);
  }

  @Bean
  public KiteConnect getKiteConnect() {
    return kiteConnect;
  }

}
