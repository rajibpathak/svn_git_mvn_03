package com.omnet.cnt.Model;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Client client = new Client();  // Initialize Client to avoid null values
    private Omnet omnet = new Omnet();     // Initialize Omnet to avoid null values

    public static class Client {
        private String name;
        private String logo;

 
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }
        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public static class Omnet {
        private String logo;

        // Getters and Setters
        public String getLogo() {
            return logo;
        }
        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    // Getters and Setters for client and omnet
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public Omnet getOmnet() {
        return omnet;
    }
    public void setOmnet(Omnet omnet) {
        this.omnet = omnet;
    }
}
