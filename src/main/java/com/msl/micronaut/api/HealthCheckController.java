package com.msl.micronaut.api;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.validation.Validated;
import lombok.extern.slf4j.Slf4j;

@Validated 
@Slf4j
@Controller("/correlation")
public class HealthCheckController {

    @Get("/healthcheck") 
    public HttpResponse healthcheck() {
		log.info("healthcheck");
		return HttpResponse.ok();
    }
}
