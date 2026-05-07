package cn.bugstack.ai.test.app;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import com.alibaba.fastjson.JSON;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import cn.bugstack.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Wxh
 * @date 2026年05月05日 11:05
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AiAgentAutoConfigTest {

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void testAgent(){
        // debug: check config loaded
        AiAgentAutoConfigProperties props = applicationContext.getBean(AiAgentAutoConfigProperties.class);
        log.info("Config enabled: {}, tables: {}", props.isEnabled(), props.getTables());

        // debug: list all bean names containing "1000"
        for (String name : applicationContext.getBeanDefinitionNames()) {
            if (name.contains("1000")) {
                log.info("Found bean: {}", name);
            }
        }

        AiAgentRegisterVO aiAgentRegisterVo = applicationContext.getBean("100001", AiAgentRegisterVO.class);

        String appName = aiAgentRegisterVo.getAppName();
        InMemoryRunner runner = aiAgentRegisterVo.getRunner();

        Session session = runner.sessionService()
                .createSession(appName, "tomato")
                .blockingGet();

        Content userMessage = Content.fromParts(Part.fromText("编写冒泡排序"));
        Flowable<Event> events = runner.runAsync("tomato", session.id(), userMessage);

        List<String> outPuts = new ArrayList<>();
        events.blockingForEach(event -> outPuts.add(event.stringifyContent()));

        log.info("测试结果: {}", JSON.toJSONString(outPuts));

    }

    @Test
    public void test_handleMessage_02(){
        AiAgentRegisterVO aiAgentRegisterVo = applicationContext.getBean("100002", AiAgentRegisterVO.class);

        String appName = aiAgentRegisterVo.getAppName();
        InMemoryRunner runner = aiAgentRegisterVo.getRunner();

        Session session = runner.sessionService()
                .createSession(appName, "tomato")
                .blockingGet();

        Content userMessage = Content.fromParts(Part.fromText("把tomato转换为大写"));
        Flowable<Event> events = runner.runAsync("tomato", session.id(), userMessage);

        List<String> outPuts = new ArrayList<>();
        events.blockingForEach(event -> outPuts.add(event.stringifyContent()));

        

        log.info("测试结果: {}", JSON.toJSONString(outPuts));
    }

    @Test
    public void test_handleMessage_03(){
        AiAgentRegisterVO aiAgentRegisterVo = applicationContext.getBean("100003", AiAgentRegisterVO.class);

        String appName = aiAgentRegisterVo.getAppName();
        InMemoryRunner runner = aiAgentRegisterVo.getRunner();

        Session session = runner.sessionService()
                .createSession(appName, "tomato")
                .blockingGet();

        Content userMessage = Content.fromParts(Part.fromText("你具备哪些能力"));
        Flowable<Event> events = runner.runAsync("tomato", session.id(), userMessage);

        List<String> outPuts = new ArrayList<>();
        events.blockingForEach(event -> outPuts.add(event.stringifyContent()));

        log.info("测试结果: {}", JSON.toJSONString(outPuts));
    }


}
