<div align="center">

<img src="docs/assets/mark.svg" alt="ai-agent-scaffold" width="104" height="104">

# ai-agent-scaffold

基于 xfg-frame-archetype 的 DDD 脚手架，用来起一个 AI Agent 应用。

Java · Spring Boot · Maven · 多模块

</div>

## 它是什么

不是又一个 Agent 框架，是**一个起手的骨架**：依赖、分层、包结构和构建脚本都已经铺好，你要做的就是往 `domain/agent` 里填自己的 Agent。

对外只有一个入口方法：

```java
Response<ChatResponseDTO> chat(ChatRequestDTO requestDTO);
```

## 三种执行方式

Agent 的执行方式是一个枚举，每个枚举值对应 Spring 容器里的一个节点 Bean：

| `type` | 名称 | 节点 Bean |
| :--- | :--- | :--- |
| `loop` | 循环执行 | `loopAgentNode` |
| `parallel` | 并行执行 | `parallelAgentNode` |
| `sequential` | 串行执行 | `sequentialAgentNode` |

三种都有对应的用例测试（`LoopAgentTest` / `ParallelAgentTest` / `SequentialAgentTest`），可以直接看它们各自怎么被调用。

## 模块结构

| 模块 | 职责 |
| :--- | :--- |
| `ai-agent-scaffold-api` | 对外接口与 DTO：`IAgentService`、会话与配置的出入参 |
| `ai-agent-scaffold-app` | 启动入口、`AiAgentAutoConfig` 自动装配 |
| `ai-agent-scaffold-domain` | Agent 节点、配置对象、执行策略 |
| `ai-agent-scaffold-trigger` | HTTP 入口 |
| `ai-agent-scaffold-infrastructure` | 数据访问与外部依赖 |
| `ai-agent-scaffold-types` | 通用枚举、异常、常量 |

自动装配的开关和属性在 `AiAgentAutoConfigProperties` 里。

## 常用命令

```bash
mvn clean install          # 构建全部模块
mvn clean package          # 打包
./ai-agent-scaffold-app/build.sh
```

`app` 模块自带 `Dockerfile`，可以直接打镜像。

## 随仓库带的东西

- `docs/prompt/` —— `chat-prompt.md`、`login-prompt.md`
- `docs/dev-ops/` —— nginx 配置、部署用页面
- `docs/dev-ops/agent/skills/pdf/` —— 给 Agent 用的 PDF 处理 skill

## 开发提示

代理设置、包路径与分层约定沿用 xfg-frame-archetype 的那一套，改代码前先看一眼同类模块是怎么分的。