package me.seung.demorestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.System.out;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(
        value = "/api/events", // 모든 요청의 BaseURL을 설정한다.
        produces = MediaTypes.HAL_JSON_VALUE // 모든 응답은 HAL_JSON 형식으로 내보낸다.
)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        // modelMapper의 map 함수를 통해 dto를 Event 클래스로 만들어줌
        Event event = modelMapper.map(eventDto, Event.class);
        // Headers에 Location 값을 설정한다.
        Event newEvent = eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(event);
    }
}
