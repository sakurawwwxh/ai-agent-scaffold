package cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.impl;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.service.armory.matter.mcp.client.ToolMcpCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Wxh
 * @date 2026年05月06日 15:54
 */
@Slf4j
@Service
public class StdioToolMcpCreateService implements ToolMcpCreateService {
    @Override
    public ToolCallback[] buildToolCallback(AiAgentConfigTableVo.Module.ChatModel.ToolMcp toolMcp) {

        AiAgentConfigTableVo.Module.ChatModel.ToolMcp.StdioServerParameters stdioConfig = toolMcp.getStdio();


        AiAgentConfigTableVo.Module.ChatModel.ToolMcp.StdioServerParameters.ServerParameters serverParameters = stdioConfig.getServerParameters();

        ServerParameters stdioParams = ServerParameters
                .builder(serverParameters.getCommand())
                .args(serverParameters.getArgs())
                .env(serverParameters.getEnv())
                .build();

        McpSyncClient mcpSyncClient = McpClient
                .sync(new StdioClientTransport(stdioParams, new JacksonMcpJsonMapper(new ObjectMapper())))
                .requestTimeout(Duration.ofMillis(stdioConfig.getRequestTimeout()))
                .build();

        McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

        return SyncMcpToolCallbackProvider.builder()
                .mcpClients(mcpSyncClient).build()
                .getToolCallbacks();
    }
}
