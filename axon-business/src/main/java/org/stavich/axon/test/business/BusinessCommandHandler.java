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
import org.axonframework.eventhandling.EventBus;
import org.springframework.stereotype.Component;
import org.stavich.axon.test.core.BusinessCommand;

@Component
@Slf4j
public class BusinessCommandHandler {

    private EventBus eventBus;

    public BusinessCommandHandler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @CommandHandler
    public void handle(BusinessCommand command) {
        log.info("Command received: {}", command);
    }

}
