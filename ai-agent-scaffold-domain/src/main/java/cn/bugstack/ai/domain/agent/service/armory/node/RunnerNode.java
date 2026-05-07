package cn.bugstack.ai.domain.agent.service.armory.node;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.ai.types.enums.ResponseCode;
import cn.bugstack.ai.types.exception.AppException;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.plugins.BasePlugin;
import com.google.adk.runner.InMemoryRunner;
import com.google.common.collect.ImmutableList;
import com.networknt.schema.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wxh
 * @date 2026年05月04日 18:18
 */
@Slf4j
@Service
public class RunnerNode extends AbstractArmorySupport {
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        log.info("Ai Agent 装配操作 - RunnerNode");

        AiAgentConfigTableVo aiAgentConfigTableVo = armoryCommandEntity.getAiAgentConfigTableVo();
        String appName = aiAgentConfigTableVo.getAppName();
        AiAgentConfigTableVo.Agent agent = aiAgentConfigTableVo.getAgent();

        String agentId = agent.getAgentId();
        String agentName = agent.getAgentName();
        String agentDesc = agent.getAgentDesc();

        // 获取上下文对象
//        SequentialAgent sequentialAgent = dynamicContext.getSequentialAgent();

        InMemoryRunner runner = getInMemoryRunner(dynamicContext, aiAgentConfigTableVo, appName);

        AiAgentRegisterVO aiAgentRegisterVO = AiAgentRegisterVO.builder()
                .appName(appName)
                .agentId(agentId)
                .agentName(agentName)
                .agentDesc(agentDesc)
                .runner(runner)
                .build();

        //注册到spring容器
        registerBean(agentId,AiAgentRegisterVO.class,aiAgentRegisterVO);

        return aiAgentRegisterVO;
    }


    private  InMemoryRunner getInMemoryRunner(DefaultArmoryFactory.DynamicContext dynamicContext, AiAgentConfigTableVo aiAgentConfigTableVo, String appName) {
        AiAgentConfigTableVo.Module.Runner runnerConfig = aiAgentConfigTableVo.getModule().getRunner();


        String agentName = runnerConfig.getAgentName();
        if (StringUtils.isBlank(agentName)){
            log.error("runner agentName is null");
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(),ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }


        BaseAgent baseAgent = dynamicContext.getAgentGroup().get(runnerConfig.getAgentName());

        List<BasePlugin> plugins;
        List<String> pluginNameList = runnerConfig.getPluginNameList();

        if (null != pluginNameList && !pluginNameList.isEmpty()) {
            plugins = new ArrayList<>();
            for (String pluginName : pluginNameList) {
                BasePlugin plugin = getBean(pluginName);
                plugins.add(plugin);
            }
        } else {
            plugins = ImmutableList.of();
        }

        InMemoryRunner runner = new InMemoryRunner(baseAgent, appName,plugins);
        return runner;
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }
}
