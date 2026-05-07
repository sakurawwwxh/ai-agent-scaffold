package cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.factory;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.ToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.LocalToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.SSEToolMcpCreateService;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl.StdioToolMcpCreateService;
import cn.bugstack.ai.types.enums.ResponseCode;
import cn.bugstack.ai.types.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Wxh
 * @date 2026年05月06日 16:41
 */

@Slf4j
@Service
public class DefaultMcpClientFactory {

    @Resource
    private LocalToolMcpCreateService localToolMcpCreateService;

    @Resource
    private SSEToolMcpCreateService sseToolMcpCreateService;

    @Resource
    private StdioToolMcpCreateService stdioToolMcpCreateService;

    public ToolMcpCreateService getToolMcpCreateService(AiAgentConfigTableVo.Module.ChatModel.ToolMcp toolMcp){
        if (null!= toolMcp.getLocal()) return localToolMcpCreateService;
        if (null!= toolMcp.getSse()) return sseToolMcpCreateService;
        if (null != toolMcp.getStdio()) return stdioToolMcpCreateService;

        throw new AppException(ResponseCode.NOT_FOUND_METHOD.getCode(),ResponseCode.NOT_FOUND_METHOD.getInfo());
    }
}
