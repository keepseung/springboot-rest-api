package me.seung.demorestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.seung.demorestapi.events.EventRepository;
import org.junit.Ignore;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test") // 공용으로 사용하는 application.properties도 사용하고 test 프로퍼티도 더한다.
@Ignore // 테스트를 가지고 있는 클래스로 간주되지 않도록 함 = 테스트 실해 안됨
public class BaseControllerTest {
    // 스프링 MVC 테스트 핵심 클래스
    @Autowired
    protected MockMvc mockMvc;

    // 이미 bin에 들어가 있음
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;
}
