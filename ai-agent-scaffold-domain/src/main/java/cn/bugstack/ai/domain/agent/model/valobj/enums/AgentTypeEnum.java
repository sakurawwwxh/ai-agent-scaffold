package cn.bugstack.ai.domain.agent.model.valobj.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AgentTypeEnum {


    Loop("循环执行", "loop", "loopAgentNode"),
    Parallel("并行执行", "parallel", "parallelAgentNode"),
    Sequential("串行执行", "Sequential", "sequentialAgentNode"),

    ;

    private String name;
    private String type;
    private String node;

    public static AgentTypeEnum fromType(String type) {

        if (type == null) return null;

        for (AgentTypeEnum value : AgentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        return null;

    }

}




