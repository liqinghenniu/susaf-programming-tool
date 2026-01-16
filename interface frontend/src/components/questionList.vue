<template>
  <div class="list_area">
    <div class="quesiton" v-for="(item,index) in questionList" :key="item">
      <div class="q_title">{{ index + 1 }}.&nbsp;{{ item.title }}</div>
      <el-radio-group v-model="answer[index]" @change="onchange">
        <el-radio class="radio" v-for="opt in item.options" :key="opt.value" :value="opt.value" >
          {{ opt.label }}
        </el-radio>
      </el-radio-group>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref,defineExpose, } from "vue";
const props = defineProps({
  questionList: {
    type: Array,
    default: () => {
      return [];
    }
  },
})
const answer = ref(new Array(props.questionList.length).fill(0));
defineExpose({'answer':answer},{'category':props.category})

</script>
<style lang="scss">
.list_area {
  
  .radio {
    margin-bottom: 15px;
  }
}
</style> 