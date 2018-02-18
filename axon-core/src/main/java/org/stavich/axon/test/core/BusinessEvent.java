package org.stavich.axon.test.core;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class BusinessEvent implements Serializable {

    private String aggregateId;
    private long number;
    private String string;

}
