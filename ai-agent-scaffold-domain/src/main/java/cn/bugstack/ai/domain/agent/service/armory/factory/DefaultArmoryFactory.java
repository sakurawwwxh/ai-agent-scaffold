package cn.bugstack.ai.domain.agent.service.armory.factory;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.node.RootNode;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Wxh
 * @date 2026年05月02日 21:26
 */
@Service
public class DefaultArmoryFactory {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RootNode rootNode;

    public StrategyHandler<ArmoryCommandEntity, DynamicContext, AiAgentRegisterVO> armoryStrategyHandler() {
        return rootNode;
    }

    public AiAgentRegisterVO getAiAgentRegisterVO(String agentId){
        return applicationContext.getBean(agentId,AiAgentRegisterVO.class);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private OpenAiApi openAiApi;

        private ChatModel chatModel;
        


        /***
         * 智能体配置组
         */

        private Map<String, BaseAgent> agentGroup = new HashMap<>();

        private Map<String,Object> dataObjects = new HashMap<>();


        private AtomicInteger currentStepIndex = new AtomicInteger(0);

        private AiAgentConfigTableVO.Module.AgentWorkflow currentAgentWorkflow;

        public <T> void setValue(String key, T value) {
            dataObjects.put(key,value);
        }

        public <T> T getValue(String key) {
            return (T) dataObjects.get(key);
        }

        public List<BaseAgent> queryAgentList(List<String> agentName){
            if (null == agentName || agentName.isEmpty() || agentGroup==null){
                return Collections.emptyList();
            }

            List<BaseAgent> agentList = new ArrayList<>();
            for (String name : agentName){

                BaseAgent agent = agentGroup.get(name);
                if (null != agent){
                    agentList.add(agent);
                }
            }

            return agentList;
        }


        public void addCurrentStepIndex(){
            currentStepIndex.incrementAndGet();
        }

        public int getCurrentStepIndex(){
            return currentStepIndex.get();
        }

    }
}
