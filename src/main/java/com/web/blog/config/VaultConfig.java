package com.web.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

import java.net.URI;

@Configuration
@VaultPropertySource("secrets/dbi")
public class VaultConfig extends AbstractVaultConfiguration {

    @Override
    public VaultEndpoint vaultEndpoint() {
        String uri = getEnvironment().getProperty("vault.uri");
        if (uri != null){
            return VaultEndpoint.from(URI.create(uri));
        }else throw new IllegalStateException();
    }

    @Override
    public ClientAuthentication clientAuthentication() {
        String token = getEnvironment().getProperty("spring.cloud.vault.token");
        if (token != null){
            return new TokenAuthentication(token);
        }else throw new IllegalStateException();
    }
}