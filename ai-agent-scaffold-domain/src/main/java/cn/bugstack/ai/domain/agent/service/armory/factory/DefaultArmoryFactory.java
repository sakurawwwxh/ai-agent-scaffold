package cn.bugstack.ai.domain.agent.service.armory.factory;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Wxh
 * @date 2026年05月02日 21:26
 */
@Service
public class DefaultArmoryFactory {


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext {

        private OpenAiApi openAiApi;

        private ChatModel chatModel;

        private SequentialAgent sequentialAgent;


        /***
         * 智能体配置组
         */

        private Map<String, BaseAgent> agentGroup = new HashMap<>();

        private Map<String,Object> dataObjects = new HashMap<>();

        private List<AiAgentConfigTableVo.Module.AgentWorkflow> agentWorkflows = new ArrayList<>();

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
    }
}
