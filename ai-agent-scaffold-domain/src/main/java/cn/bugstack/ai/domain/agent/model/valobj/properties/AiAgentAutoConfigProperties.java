package cn.bugstack.ai.domain.agent.model.valobj.properties;

import cn.bugstack.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author Wxh
 * @date 2026年05月02日 10:57
 */

@Data
@ConfigurationProperties(prefix = "ai.agent.config", ignoreInvalidFields = true)
public class AiAgentAutoConfigProperties {

    private boolean enabled = false;

    private Map<String, AiAgentConfigTableVO> tables;
}
