package me.seung.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    // 스프링 MVC 테스트 핵심 클래스
    @Autowired
    MockMvc mockMvc;

    // 이미 bin에 들어가 있음
    @Autowired
    ObjectMapper objectMapper;
//    @MockBean
//    EventRepository eventRepository;
    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("api desc")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .endEventDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8) // Json 담아서 요청한다.
                    .accept(MediaTypes.HAL_JSON) // HAL_JSON을 응답을 원한다.
                    .content(objectMapper.writeValueAsString(event))) // objectMapper가 Eve nt 객체를 문자열로 바꿔준다.
                .andDo(print()) // 요청과 응답을, 응답을 출력해줌
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists()) // 응답 값에서 id 값이 존재하는지 확인함
        .andExpect(header().exists(HttpHeaders.LOCATION)) // LOCATION 헤더가 있는지 확인
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE)) // CONTENT_TYPE 값이 HAL_JSON_VALUE로 나오는지 확인
        .andExpect(jsonPath("id").value(Matchers.not(100))) // "id" 값이 100이면 안됨
        .andExpect(jsonPath("free").value(Matchers.not(true))); // "free" 값이 true이면 안됨


    }

    @Test
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("api desc")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .endEventDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8) // Json 담아서 요청한다.
                .accept(MediaTypes.HAL_JSON) // HAL_JSON을 응답을 원한다.
                .content(objectMapper.writeValueAsString(event))) // objectMapper가 Eve nt 객체를 문자열로 바꿔준다.
                .andDo(print()) // 요청과 응답을, 응답을 출력해줌
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();

        // 입력값이 없는 경우 Bad_Request 응답하도록 처리
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("api desc")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,24,10,10,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .beginEventDateTime(LocalDateTime.of(2018,11,24,10,10,10))
                .endEventDateTime(LocalDateTime.of(2018,11,22,10,10,10))
                .basePrice(10099)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        // 입력값이 없는 경우 Bad_Request 응답하도록 처리
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}
