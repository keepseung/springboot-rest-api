package me.seung.demorestapi.index;

import lombok.val;
import me.seung.demorestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {
    @GetMapping("/api")
    public RepresentationModel index(){
        RepresentationModel representationModel = new RepresentationModel();
        representationModel.add(linkTo(EventController.class).withRel("events"));
        return representationModel;
    }
}
