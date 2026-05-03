package cn.bugstack.ai.domain.agent.service.armory.node;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.springai.SpringAI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Wxh
 * @date 2026年05月03日 21:38
 */
@Slf4j
@Service
public class AgentNode extends AbstractArmorySupport {
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        log.info("Ai Agent 装配操作 - AgentNode");

        ChatModel chatModel = dynamicContext.getChatModel();

        AiAgentConfigTableVo aiAgentConfigTableVo = armoryCommandEntity.getAiAgentConfigTableVo();
        List<AiAgentConfigTableVo.Module.Agent> agents = aiAgentConfigTableVo.getModule().getAgents();

        for (AiAgentConfigTableVo.Module.Agent agent : agents){
            LlmAgent llmAgent = LlmAgent.builder()
                    .name(agent.getName())
                    .description(agent.getDescription())
                    .model(new SpringAI(chatModel))
                    .instruction(agent.getInstruction())
                    .outputKey(agent.getOutputKey())
                    .build();

            dynamicContext.getAgentGroup().put(agent.getName(),llmAgent);
        }


        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return null;
    }
}
