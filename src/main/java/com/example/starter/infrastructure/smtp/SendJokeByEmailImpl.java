package com.example.starter.infrastructure.smtp;

import com.example.starter.domain.SendJokeByEmail;
import com.example.starter.domain.XkcdDomainException;
import com.example.starter.domain.XkcdJoke;
import com.example.starter.infrastructure.smtp.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class SendJokeByEmailImpl implements SendJokeByEmail {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendJokeByEmailImpl.class.getName());

    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    public SendJokeByEmailImpl(final EmailService emailService) {
        this.emailService = emailService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void send(final XkcdJoke joke,
                     final String to) {
        LOGGER.info("Send joke by email: " + joke.getId());
        emailService.send(toJson(joke), to,
                res -> {
                    if (res.succeeded()) {
                        LOGGER.info("Send joke " + joke.getId() + " by email: Success!!!!");
                    } else {
                        LOGGER.info("Send joke " + joke.getId() + " by email: Error: " + res.cause());
                    }
                });
    }

    private String toJson(final XkcdJoke xkcdJoke) {
        try {
            return objectMapper.writeValueAsString(xkcdJoke);
        } catch (Exception e) {
            LOGGER.info("Could not parse xkcd joke!!!");
            throw new XkcdDomainException("Impossible to parse xkcd joke!", e);
        }
    }
}
