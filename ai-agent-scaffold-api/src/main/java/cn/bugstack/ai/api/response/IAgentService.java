package cn.bugstack.ai.api.response;


import cn.bugstack.ai.api.dto.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

/***
 * 智能体服务接口
 */
public interface IAgentService {

    Response<List<AiAgentConfigResponseDTO>> queryAiAgentConfigList();

    Response<CreateSessionResponseDTO> createSession(CreateSessionRequestDTO requestDTO);

    Response<ChatResponseDTO> chat(ChatRequestDTO requestDTO);

    ResponseBodyEmitter chatStream(ChatRequestDTO requestDTO);
}
