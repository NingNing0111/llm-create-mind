<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import {
  type CustomEventDataType,
  type CustomEventReadyStateChangeType,
  type CustomEventType,
  SSE,
  SSEOptionsMethod,
} from 'sse-ts';
import { Transformer } from 'markmap-lib';
import { Markmap } from 'markmap-view'
import { Toolbar } from 'markmap-toolbar'

const topic = ref('')
const markdownContent = ref('')
const generating = ref(false);
const svgRef = ref();
const mm = ref();
const transformer = new Transformer();
const bottomRef = ref();
/**
 * 发起 SSE 请求
 */
const handleChat = async () => {

  markdownContent.value = '';
  generating.value = true;
  const source = new SSE(`/api/llm/createMind?topic=${topic.value}`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: SSEOptionsMethod.POST,

  });

  source.addEventListener('message', (e: CustomEventType) => {
    const dataEvent = e as CustomEventDataType;
    const data = JSON.parse(dataEvent.data);
    if (data.output && data.output.text !== null) {
      const payload = JSON.parse(dataEvent.data);
      const text = payload.output.text;
      markdownContent.value += text;
    }
  });

  source.addEventListener('readystatechange', (e: CustomEventType) => {
    const dataEvent = e as CustomEventReadyStateChangeType;
    if (dataEvent.readyState >= 2) {
      source.close();
      generating.value = false;

    }
  });

  source.stream();

}

const update = async () => {
  const { root } = transformer.transform(markdownContent.value);
  await mm.value.setData(root);
  mm.value.fit();
};

onMounted(async () => {
  mm.value = Markmap.create(svgRef.value);
  update();

  const { el } = Toolbar.create(mm.value);

  // 移除 logo 图标（SVG 中的 <a> 元素）
  const logoImg = el.querySelector('a');
  if (logoImg) {
    logoImg.remove();
  }
  el.style.position = 'absolute';
  el.style.bottom = '0.5rem';
  el.style.right = '0.5rem';

  const container = svgRef.value.parentElement
  if (container) container.append(el)
})


watch(markdownContent, () => {
  if (mm.value) {
    update();
  }
});

</script>

<template>
  <div class="page">
    <!-- 上部分：输入主题 -->
    <div class="top">
      <input v-model="topic" placeholder="请输入主题" style="width: 400px;" />
      <button @click="handleChat" :disabled="generating">{{ generating ? '生成中...' : '点击生成' }}</button>
    </div>

    <!-- 中下部分：markdown 和思维导图左右布局 -->
    <div class="content">
      <!-- 左边：markdown 内容 -->
      <div class="middle">
        <pre>{{ markdownContent }}</pre>
      </div>

      <!-- 右边：Markmap 思维导图 -->
      <div class="bottom" :ref="bottomRef">
        <svg class="markmap" ref="svgRef"></svg>
      </div>
    </div>
  </div>
</template>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

/* 上部分：输入框区域 */
.top {
  padding: 10px;
  background-color: #f5f5f5;
  display: flex;
  gap: 10px;
}

/* 新增：中下部分容器，左右布局 */
.content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左边：markdown 内容 */
.middle {
  flex: 1;
  padding: 10px;
  overflow-y: auto;
  background-color: #ffffff;
  border-right: 1px solid #ddd;
  white-space: pre-wrap;
  font-family: monospace;
}

/* 右边：思维导图 */
.bottom {
  flex: 3;
  background-color: #fafafa;
  padding: 10px;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.markmap {
  width: 100%;
  height: 100%;
}
</style>
