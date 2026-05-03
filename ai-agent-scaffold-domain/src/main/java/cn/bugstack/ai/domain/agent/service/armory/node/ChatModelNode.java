package cn.bugstack.ai.domain.agent.service.armory.node;

import cn.bugstack.ai.domain.agent.model.entity.ArmoryCommandEntity;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVo;
import cn.bugstack.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import cn.bugstack.ai.domain.agent.service.armory.AbstractArmorySupport;
import cn.bugstack.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.utils.StringUtils;
import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.oer.its.etsi102941.Url;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallback;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wxh
 * @date 2026年05月03日 11:59
 */
@Slf4j
@Service
public class ChatModelNode extends AbstractArmorySupport {

    @Resource
    private AgentNode agentNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        log.info("Ai Agent 装配操作 - ChatModelNode");

        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();

        AiAgentConfigTableVo aiAgentConfigTableVo = armoryCommandEntity.getAiAgentConfigTableVo();
        AiAgentConfigTableVo.Module.ChatModel chatModelConfig = aiAgentConfigTableVo.getModule().getChatModel();

        List<McpSyncClient> mcpSyncClients = new ArrayList<>();

        List<AiAgentConfigTableVo.Module.ChatModel.ToolMcp> toolMcpList = chatModelConfig.getToolMcpList();

        
        for (AiAgentConfigTableVo.Module.ChatModel.ToolMcp toolMcp : toolMcpList){
            mcpSyncClients.add(createMcpSyncClient(toolMcp));
        }

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatModelConfig.getModel())
                        .toolCallbacks(SyncMcpToolCallbackProvider.builder()
                                .mcpClients(mcpSyncClients).build()
                                .getToolCallbacks())
                        .build())
                .build();

        dynamicContext.setChatModel(chatModel);

        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return  agentNode;
    }


    private McpSyncClient createMcpSyncClient(AiAgentConfigTableVo.Module.ChatModel.ToolMcp toolMcp) throws Exception {

        AiAgentConfigTableVo.Module.ChatModel.ToolMcp.SSEServerParameters sseConfig = toolMcp.getSse();

        AiAgentConfigTableVo.Module.ChatModel.ToolMcp.StdioServerParameters stdioConfig = toolMcp.getStdio();

        if (sseConfig != null) {

            String originalBaseUri = sseConfig.getBaseUri();
            String baseUri = originalBaseUri;
            String sseEndpoint = sseConfig.getSseEndpoint();


            if (StringUtils.isBlank(sseEndpoint)) {
                URL url = new URL(originalBaseUri);

                String protocol = url.getProtocol();
                String host = url.getHost();
                int port = url.getPort();

                String baseUrl = port == -1 ? protocol + "://" + host : protocol + "://" + host + ":" + port;


                int index = originalBaseUri.indexOf(baseUrl);
                if (index != -1) {
                    sseEndpoint = originalBaseUri.substring(index + baseUrl.length());
                }

                baseUri = baseUrl;
            }

            sseEndpoint = StringUtils.isBlank(sseEndpoint) ? "/sse" : sseEndpoint;

            HttpClientSseClientTransport sseClientTransport = HttpClientSseClientTransport
                    .builder(baseUri)
                    .sseEndpoint(sseEndpoint)
                    .build();


            McpSyncClient mcpSyncClient = McpClient.sync(sseClientTransport).requestTimeout(Duration.ofMillis(sseConfig.getRequestTimeout())).build();

            McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

            log.info("tool sse mcp initialize 成功: {}", initialize);

            return mcpSyncClient;

        }

        if (stdioConfig != null){

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

            log.info("tool stdio mcp initialize 成功: {}", initialize);

            return mcpSyncClient;
        }

       throw new RuntimeException("tool mcp 配置异常");
    }


}
