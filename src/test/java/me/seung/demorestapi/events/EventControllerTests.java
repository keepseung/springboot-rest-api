package me.seung.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.seung.demorestapi.common.RestDocsConfiguration;
import me.seung.demorestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test") // 공용으로 사용하는 application.properties도 사용하고 test 프로퍼티도 더한다.
public class EventControllerTests {

    // 스프링 MVC 테스트 핵심 클래스
    @Autowired
    MockMvc mockMvc;

    // 이미 bin에 들어가 있음
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;
    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
        .andExpect(jsonPath("id").exists()) // "id" 값이 100이면 안됨
//        .andExpect(jsonPath("free").value(Matchers.not(true))) // "free" 값이 true이면 안됨
        .andExpect(jsonPath("offline").value(Matchers.equalTo(true)))
        .andExpect(jsonPath("eventStatus").value(Matchers.equalTo(EventStatus.DRAFT.name())))
        .andDo(document("create-event",
                links(
                        linkWithRel("self").description("link to self"),
                        linkWithRel("query-events").description("link to query events"),
                        linkWithRel("update-event").description("link to update an existing events"),
                        linkWithRel("profile").description("link to profile")
                ),
                requestHeaders(
                        headerWithName(HttpHeaders.ACCEPT).description("request acccept header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("request content type header")
                ),
                requestFields(
                        fieldWithPath("name").description("Name fo new Event"),
                        fieldWithPath("description").description("description fo new Event"),
                        fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime fo new Event"),
                        fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime fo new Event"),
                        fieldWithPath("beginEventDateTime").description("beginEventDateTime fo new Event"),
                        fieldWithPath("endEventDateTime").description("endEventDateTime fo new Event"),
                        fieldWithPath("basePrice").description("basePrice fo new Event"),
                        fieldWithPath("maxPrice").description("maxPrice fo new Event"),
                        fieldWithPath("limitOfEnrollment").description("limitOfEnrollment fo new Event"),
                        fieldWithPath("location").description("location fo new Event")
                ),
                responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("response Location header"),
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("response content type header")
                ),
                responseFields(
                        fieldWithPath("id").description("Id fo new Event"),
                        fieldWithPath("name").description("Name fo new Event"),
                        fieldWithPath("description").description("description fo new Event"),
                        fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime fo new Event"),
                        fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime fo new Event"),
                        fieldWithPath("beginEventDateTime").description("beginEventDateTime fo new Event"),
                        fieldWithPath("endEventDateTime").description("endEventDateTime fo new Event"),
                        fieldWithPath("basePrice").description("basePrice fo new Event"),
                        fieldWithPath("maxPrice").description("maxPrice fo new Event"),
                        fieldWithPath("limitOfEnrollment").description("limitOfEnrollment fo new Event"),
                        fieldWithPath("location").description("location fo new Event"),
                        fieldWithPath("free").description("free fo new Event"),
                        fieldWithPath("offline").description("offline fo new Event"),
                        fieldWithPath("eventStatus").description("eventStatus fo new Event"),
                        fieldWithPath("_links.self.href").description("link to self"),
                        fieldWithPath("_links.query-events.href").description("link to query-events"),
                        fieldWithPath("_links.update-event.href").description("link to update-event"),
                        fieldWithPath("_links.profile.href").description("link to profile")
                )
        )) // 문서 생성 및 이름 명시
        ;

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 발생하는 이벤트를 생성하는 테스트")
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
    @TestDescription("입력 값이 비어있는 경우에 에라가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build();

        // 입력값이 없는 경우 Bad_Request 응답하도록 처리
        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못 된 경우에 에라가 발생하는 테스트")
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
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
    public void queryEvent() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(i -> {
            this.generateEvent(i);
        });

        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))

        ;

    }

    private void generateEvent(int i) {
        Event event = Event.builder()
                .name("event " + i)
                .description("test event")
                .build();
        this.eventRepository.save(event);
    }
}
