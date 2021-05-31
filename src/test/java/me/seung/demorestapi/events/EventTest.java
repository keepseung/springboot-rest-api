package me.seung.demorestapi.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("api name")
                .description("aaa desc")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        String name ="Event";
        String desc ="desc";

        Event event = new Event();
        event.setName(name);
        event.setDescription(desc);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(desc);
    }

}