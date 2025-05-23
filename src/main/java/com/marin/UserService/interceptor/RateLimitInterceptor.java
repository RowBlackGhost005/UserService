package com.marin.UserService.interceptor;

import com.marin.UserService.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Component that sits between a received request to the API and the business logic and determines if the client
 * has enough requests left to grant him access to this API.
 *
 *  Currently, this rate limit only applies to auth requests.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService limiterService;

    @Autowired
    public RateLimitInterceptor(RateLimiterService limiter){
        this.limiterService = limiter;
    }

    /**
     * Determines if the client has enough requests left to perform the requested operation in this API.
     *
     * If the client has enough requests tokens the request goes without further problems.
     * If the client doesn't have enough requests tokens it returns to the client a 429 (Too many Requests) code.
     */
    @Override
    public boolean preHandle(HttpServletRequest request , HttpServletResponse response , Object handler) throws Exception {

        String ipAddress = request.getRemoteAddr();

        Bucket tokenBucket = limiterService.resolveBucket(ipAddress);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()){
            return true;
        }else{
            System.err.println("Interceptor");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("You've tried too many times, wait one minute and try again");
            return false;
        }
    }
}
