/*
 * Copyright (c) 2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.stavich.axon.test.business;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateLifecycle;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.stereotype.Component;
import org.stavich.axon.test.core.BusinessCommand;
import org.stavich.axon.test.core.BusinessEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@Slf4j
public class BusinessAggregate {

    @AggregateIdentifier
    private String aggregateId;

    @CommandHandler
    public BusinessAggregate(BusinessCommand command){
        handle(command);
    }

    @CommandHandler
    public void handle(BusinessCommand command) {
        log.info("Command received: {}", command);
        BusinessEvent event = BusinessEvent
                .builder()
                .aggregateId(command.getAggregateId())
                .number(command.getNumber())
                .string(command.getString())
                .time(command.getTime())
                .build();
        apply(event);
    }

    @EventSourcingHandler
    public void on(BusinessEvent event) {
        log.info("Handling event: {}", event);
        this.aggregateId = event.getAggregateId();
    }

}
