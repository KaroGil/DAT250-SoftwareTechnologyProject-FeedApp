package dat250.group22.FeedApp;

import dat250.group22.FeedApp.service.AnalyticsService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import dat250.group22.FeedApp.document.PollDocument;
import dat250.group22.FeedApp.document.VoteOptionDocument;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class MongoDBIntegrationTest {

    @Mock
    private RabbitTemplate rabbitTemplate;  // Mock RabbitTemplate

    @InjectMocks
    private AnalyticsService analyticsService;  // Inject the mocked RabbitTemplate into your service

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testSavePollDocument(){
        PollDocument pollDocument = new PollDocument();
        pollDocument.setId("1");
        pollDocument.setQuestion("Is it working?");
        pollDocument.setCreatorUserID("1234");
        pollDocument.setPublic(true);

        // Sample VoteOptionDocument instances
        List<VoteOptionDocument> options = new ArrayList<>();

        VoteOptionDocument option1 = new VoteOptionDocument();
        option1.setId("01");
        option1.setCaption("Yes");

        VoteOptionDocument option2 = new VoteOptionDocument();
        option2.setId("02");
        option2.setCaption("No");

        options.add(option1);
        options.add(option2);

        pollDocument.setOptions(options);

        // Save to MongoDB
        mongoTemplate.save(pollDocument);

        // Retrieve and verify
        PollDocument retrieved = mongoTemplate.findById("1", PollDocument.class);
        assertNotNull(retrieved);
        assertEquals(2, retrieved.getOptions().size());
        assertEquals("Yes", retrieved.getOptions().get(0).getCaption());
        assertEquals("No", retrieved.getOptions().get(1).getCaption());
    }

    @Configuration
    @Import(FeedAppApplication.class)
    static class TestConfig {

        @Bean
        public RabbitTemplate rabbitTemplate() {
            return mock(RabbitTemplate.class); // Mock the RabbitTemplate
        }
    }
}

