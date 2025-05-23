package com.marin.UserService.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

/**
 * Enum for establishing the bandwidth (limit of requests and its refill period) of the different types of clients of the API
 */
public enum RateLimit {

    CLIENT {
        @Override
        public Bandwidth getLimit(){
            return Bandwidth.classic(3 , Refill.intervally(3 , Duration.ofMinutes(1)));
        }
    },

    USER {
        @Override
        public Bandwidth getLimit() {
            return Bandwidth.classic(10 , Refill.intervally(10 , Duration.ofMinutes(1)));
        }
    };

    public static RateLimit resolveClient(int type){
        if(type == 0){
            return CLIENT;
        }else{
            return USER;
        }
    }

    public abstract Bandwidth getLimit();
}
