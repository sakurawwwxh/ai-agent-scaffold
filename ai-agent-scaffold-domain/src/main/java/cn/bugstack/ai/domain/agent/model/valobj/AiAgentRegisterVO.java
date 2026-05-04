package cn.bugstack.ai.domain.agent.model.valobj;

import com.google.adk.runner.InMemoryRunner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Wxh
 * @date 2026年05月02日 21:32
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentRegisterVO {

    /***
     * 智能体名称
     */
    private String appName;

    /***
     * 智能体ID
     */
    private String agentId;

    /***
     *  智能体名称
     */
    private String agentName;

    /***
     *  智能体描述信息
     */
    private String agentDesc;

    /***
     *  智能体执行对象
     */
    private InMemoryRunner runner;

}
