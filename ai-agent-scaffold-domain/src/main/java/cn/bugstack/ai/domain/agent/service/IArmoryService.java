package cn.bugstack.ai.domain.agent.service;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;

import java.util.List;

public interface IArmoryService {

    void acceptArmoryAgents(List<AiAgentConfigTableVo> tables) throws Exception;
}
