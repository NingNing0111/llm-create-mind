# 大模型生成思维导图


&emsp;大多数情况下，大模型或者是Agent在执行任务时，会列举一个计划列表或者行动大纲，我们可以将这一中间产物转换为可理解的内容，例如思维导图。

&emsp;让大模型生成思维导图并不难，百行不到的代码便能实现，主要的核心点就是：提示词。

## 个解决方案

- 使用提示词要求大模型输出`markdown`格式的标题列表。
- 使用`markmap`将输出内容转换为思维导图。

## 实现效果

![](https://pgthinker.me/wp-content/uploads/2025/06/Screenity-video-Jun-11-2025.gif)

## 提示词

> 可以根据实际业务场景进行修改，可以结合RAG让大模型生成的思维导图更加专业。

```text
You are an expert in using Markmap to create clear and structured brainstorming mind maps.

Given the topic "{topic}", generate a well-organized markdown outline that can be directly parsed by Markmap.
Use concise headings and subheadings to represent hierarchical ideas effectively.
Avoid unnecessary formatting or inline content — focus on clarity, logic, and depth.

**Please answer directly using the markdown content.You also don't need to wrap it with '```' **.
```

## 其它说明

- 若要使用XMind，可以将生成的markdown文件导入到XMind软件中生成思维导图。
- XMind文件：`.xmind`本质是一个zip压缩包，内部文件结构及文件内容较复杂，官网也没有明确的说明，因此直接生成`.xmind`文件理论上可以实现，但研究成本较高。
- Github上有一些开源项目支持直接将markdown转为`.xmind`文件，但大部分生成的文件仅支持XMind 8版本以上，最新版本的XMind基本不支持。

## 源码

- [GitHub](https://github.com/NingNing0111/llm-create-mind)
