/*
 * Copyright (c) 2010-2011. Axon Framework
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

package org.axonframework.test;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.domain.DomainEventMessage;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

import java.util.UUID;

/**
 * @author Allard Buijze
 */
class AnnotatedAggregate extends AbstractAnnotatedAggregateRoot {

    @SuppressWarnings("UnusedDeclaration")
    private transient int counter;
    private Integer lastNumber;
    @AggregateIdentifier
    private Object identifier;
    private MyEntity entity;

    public AnnotatedAggregate(Object identifier) {
        this.identifier = identifier;
    }

    public AnnotatedAggregate() {
    }

    @CommandHandler
    public AnnotatedAggregate(CreateAggregateCommand command) {
        this.identifier = command.getAggregateIdentifier() == null ?
                UUID.randomUUID() : command.getAggregateIdentifier();
        apply(new MyEvent(0));
    }

    @CommandHandler
    public void delete(DeleteCommand command) {
        apply(new MyAggregateDeletedEvent());
    }

    @CommandHandler
    public void doSomethingIllegal(IllegalStateChangeCommand command) {
        apply(new MyEvent(lastNumber + 1));
        lastNumber = command.getNewIllegalValue();
    }

    @EventHandler
    public void handleMyEvent(MyEvent event) {
        lastNumber = event.getSomeValue();
        if (entity == null) {
            entity = new MyEntity();
        }
    }

    @EventHandler
    public void deleted(MyAggregateDeletedEvent event) {
        markDeleted();
    }

    @EventHandler
    public void handleAll(DomainEventMessage event) {
        // we don't care about events
    }

    @CommandHandler
    public void doSomething(TestCommand command) {
        // this state change should be accepted, since it happens on a transient value
        counter++;
        apply(new MyEvent(lastNumber + 1));
    }

    @Override
    public Object getIdentifier() {
        return identifier;
    }
}
