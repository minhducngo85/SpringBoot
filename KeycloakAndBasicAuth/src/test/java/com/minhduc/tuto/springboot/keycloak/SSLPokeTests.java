package com.minhduc.tuto.springboot.keycloak;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class SSLPokeTests {
    /**
     * to get test job with CLI
     * 
     * @throws ParseException
     *             {@link ParseException}
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @Test
    public void SSLPoke_Test() {
        List<String> argList = new ArrayList<>();
        argList.add("google.com");
        argList.add("443");

        String[] stringArray = argList.stream().map(String::new).toArray(String[]::new);
        SSLPoke.main(stringArray);
    }
}
