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

package org.stavich.axon.test.report;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stavich.axon.test.core.BusinessEvent;

@Component
@Slf4j
public class BusinessEventListener {

    private ReportRepository repository;

    @Autowired
    public BusinessEventListener(ReportRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(BusinessEvent event) {
        log.info("Creating report: {}", event);
        repository.save(
                new ReportEntry(
                        event.getAggregateId(),
                        event.getNumber(),
                        event.getString(),
                        event.getTime()));
    }

}
