package org.stavich.axon.test.core;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Value
@Builder
@JsonDeserialize(builder = BusinessEvent.BusinessEventBuilder.class)
public class BusinessEvent implements Serializable {

    private String aggregateId;
    private long number;
    private String string;
    private OffsetDateTime time;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class BusinessEventBuilder {}
}
