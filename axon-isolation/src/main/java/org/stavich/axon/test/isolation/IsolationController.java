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

package org.stavich.axon.test.isolation;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stavich.axon.test.core.BusinessCommand;

import java.time.OffsetDateTime;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@Slf4j
public class IsolationController {

    private final CommandGateway commandGateway;

    public IsolationController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @GetMapping("/command")
    public void command(){
        long number = ThreadLocalRandom.current().nextLong(19999999999l, 99999999999l + 1l);
        BusinessCommand command = BusinessCommand
                .builder()
                .aggregateId("stavich")
                .number(number)
                .string("Command string")
                .time(OffsetDateTime.now())
                .build();
        log.info("Dispatching: {}", command);
        commandGateway.send(command);
    }

}
