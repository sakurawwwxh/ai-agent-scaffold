package cn.bugstack.ai.domain.agent.service.armory.mcp.client;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import org.springframework.ai.tool.ToolCallback;

/***
 * 工具 MCP 构建服务
 */
public interface ToolMcpCreateService {

    ToolCallback[] buildToolCallback(AiAgentConfigTableVo.Module.ChatModel.ToolMcp toolMcp) throws Exception;
}
