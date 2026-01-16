<template>
  <div class="layout">
    <div class="question-area">
      <el-card class="card">
        <div v-if="!isSubmit">
          <div class="topic">Code Analysis System Teaching Effectiveness Evaluation Questionnaire</div>
          <div class="main">
            <div class="title">Part 1: Quality Assessment of System Technics</div>
            <question-list :questionList="techQuestions" ref="techAnswer"></question-list>
            <div class="title">Part 2: Assessment of personal knowledge outcome</div>
            <question-list :questionList="knowQuestions" ref="knowAnswer"></question-list>
            <div class="title">Part 3: Assessment of education value</div>
            <question-list :questionList="eduQuestions" ref="eduAnswer"></question-list>
          </div>
          <div class="tips">
            This questionnaire is designed to evaluate the effectiveness of the code review system in teaching, and all
            data will be used for academic research only. Please fill it out based on your genuine feelings.
          </div>
          <div class="post">
            <el-button type="primary" @click="submit">
              Submit your answers<el-icon size="20" class="button">
                <Upload />
              </el-icon>
            </el-button>
          </div>
        </div>
        <div style="margin-top:150px" v-else>
          <el-empty :image-size="250" description="Thanks for you finishing questions">
              <template #image>
            <el-icon style="color: skyblue; font-size: 150px;">
              <Check />
            </el-icon>
          </template>
        </el-empty>
        </div>
      </el-card>
    </div>
    <div class="chart-area">
      <el-card class="card">
        <div v-if="isSubmit">
          <div class="topic">Get and finish your Sustainable awareness diagram</div>
          <div class="echart" style="width: 100%;height: 100%;" ref="chartDom"></div>
          <div class="link">
            <div class="header">Add your chain of effect</div>
            <el-form :model="formData" class="form" ref="formRef" :rules="rules">
              <el-form-item label="position" prop="position">
                <el-select placeholder="please choose the position you want to put" class="select"
                  v-model="formData.position" clearable>
                  <el-option label="code" value="code"></el-option>
                  <el-option label="impact" value="impact"></el-option>
                  <el-option label="sustainable development goal" value="sustainable development goal"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="content" prop="content">
                <el-input v-model="formData.content" type="textarea" />
              </el-form-item>
              <div class="button">
                <el-button type="primary" @click="addChain">Create point</el-button>
              </div>
            </el-form>
            <el-table :data="chainList" class="table">
              <el-table-column prop="index" label="index" width="80"></el-table-column>
              <el-table-column prop="position" label="position"></el-table-column>
              <el-table-column prop="content" label="content"></el-table-column>
              <el-table-column label="operations" width="150">
                <template #default="scope">
                  <el-button size="small" @click="handleEdit(scope.$index)" v-if="!isEdit">
                    Edit
                  </el-button>
                  <el-button size="small" type="danger" @click="handleDelete(scope.$index)">
                    Delete
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div class="create">
            <el-button type="primary" @click="createChain(techLine, knowLine, eduLine)">create chain</el-button>
          </div>
        </div>
        <div class="empty" v-else>
          <el-empty :image-size="250" description="Please finish the questions left" />
        </div>
      </el-card>
    </div>
    <el-drawer v-model="drawer" title="Edit Node" direction="rtl" class="drawer">
      <div class="content">
        <el-form :model="formEdit" class="form" ref="editRef" :rules="rules2">
          <el-form-item label="index" prop="index">
            <el-input v-model="formEdit.index" type="number"></el-input>
          </el-form-item>
          <el-form-item label="position" prop="position">
            <el-select placeholder="please choose the position you want to put" class="select"
              v-model="formEdit.position" clearable>
              <el-option label="code" value="code"></el-option>
              <el-option label="impact" value="impact"></el-option>
              <el-option label="sustainable development goal" value="sustainable development goal"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="content" prop="content">
            <el-input v-model="formEdit.content" type="textarea" />
          </el-form-item>
        </el-form>
        <div class="demo-drawer__footer">
          <el-button @click="cancelForm">Cancel</el-button>
          <el-button type="primary" @click="submitEdit">
            submit
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>
<script setup>
import QuestionList from "@/components/questionList.vue";
import { ref, nextTick } from "vue";
import { Upload, Check } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import * as echarts from "echarts";
const isSubmit = ref(false);
const techAnswer = ref([]);
const knowAnswer = ref([]);
const eduAnswer = ref([]);
const techQuestions = ref([
  {
    title:
      "The system clearly identified sustainability-related problems in my code (e.g., infinite loops, unclosed resources).?",
    options: [
      { value: 1, label: "Not accurate" },
      { value: 2, label: "Somewhat accurate" },
      { value: 3, label: "Very accurate" }
    ]
  },
  {
    title:
      "The feedback provided by the system helped me understand why these problems matter.",
    options: [
      { value: 1, label: "Not clear" },
      { value: 2, label: "Somewhat clear" },
      { value: 3, label: "Very clear" }
    ]
  },
  {
    title:
      "The system’s feedback helped me decide how to improve my code.",
    options: [
      { value: 1, label: "Unhelpful" },
      { value: 2, label: "Somewhat helpful" },
      { value: 3, label: "Very helpful" }
    ]
  },
  {
    title:
      "The system clearly explained how my code issues are related to the SDGs.",
    options: [
      { value: 1, label: "Not clear" },
      { value: 2, label: "Somewhat clear" },
      { value: 3, label: "Very clear" }
    ]
  }
]);
const knowQuestions = ref([
  {
    title:
      "I am now aware that infinite loops waste energy and computing resources.",
    options: [
      { value: 1, label: "Not aware" },
      { value: 2, label: "Somewhat aware" },
      { value: 3, label: "Very aware" }
    ]
  },
  {
    title: "I understand that unclosed I/O resources lead to unnecessary resource consumption.",
    options: [
      { value: 1, label: "Not aware" },
      { value: 2, label: "Somewhat aware" },
      { value: 3, label: "Very aware" }
    ]
  },
  {
    title: "Now I realize that some alternative designs might affect the maintainability of the code (e.g., long main functions)",
    options: [
      { value: 1, label: "Not aware" },
      { value: 2, label: "Somewhat aware" },
      { value: 3, label: "Very aware" }
    ]
  },
  {
    title: "I am able to explain how a code issue can affect a specific SDG",
    options: [
      { value: 1, label: "Not able" },
      { value: 2, label: "Somewhat able" },
      { value: 3, label: "Very able" }
    ]
  },
  {
    title:
      "I consider sustainability when I write or revise my code.",
    options: [
      { value: 1, label: "Not aware" },
      { value: 2, label: "Somewhat aware" },
      { value: 3, label: "Very aware" }
    ]
  }
]);

const eduQuestions = ref([
  {
    title: "This system made the course more meaningful instead of introduction of awareness.",
    options: [
      { value: 1, label: "Not meaningful" },
      { value: 2, label: "Somewhat meaningful" },
      { value: 3, label: "Significant meaningful" }
    ]
  },
  {
    title:
      "I would like to use this system again in future programming assignments.",
    options: [
      { value: 1, label: "Not willing to" },
      { value: 2, label: "Somewhat willing to" },
      { value: 3, label: "Very willing to" }
    ]
  },
  {
    title:
      "I learned about sustainability through this programming assignment",
    options: [
      { value: 1, label: "Not promoted" },
      { value: 2, label: "Somewhat promoted" },
      { value: 3, label: "Very promoted" }
    ]
  },
  {
    title:
      "Has this tool increased the pressure and workload for me in my programming class.",
    options: [
      { value: 1, label: "Not increased" },
      { value: 2, label: "Somewhat increased "},
      { value: 3, label: "Very increased"}
    ]
  }
]);
const chartDom = ref(null);
const formData = ref({
  position: "",
  content: ""
});
const formEdit = ref({
  index: 1,
  position: "",
  content: ""
});

const rules = ref({
  position: [
    {
      required: true,
      trigger: "blur",
      message: "please fill..."
    }
  ],
  content: [
    {
      required: true,
      trigger: "blur",
      message: "please fill..."
    }
  ]
});
const rules2 = ref({
  position: [
    {
      required: true,
      trigger: "blur",
      message: "please fill..."
    }
  ],
  content: [
    {
      required: true,
      trigger: "blur",
      message: "please fill..."
    }
  ],
  index: [
    {
      required: true,
      trigger: "blur",
      message: "please fill..."
    }
  ]
});
const formRef = ref();
const editRef = ref();
const chainList = ref([]);
const drawer = ref(false);
const editRow = ref(0);
const techLine = ref(null);
const knowLine = ref(null);
const eduLine = ref(null);

function submit() {
  const allAnswer = [
    ...techAnswer.value.answer,
    ...knowAnswer.value.answer,
    ...eduAnswer.value.answer
  ];
  const emptySelect = allAnswer.findIndex(item => {
    return item === 0;
  });
  let title = "";
  let num = 0;
  if (emptySelect >= 0 && emptySelect < 4) {
    title = "Part 1";
    num = emptySelect;
  } else if (emptySelect < 8) {
    title = "Part 2";
    num = emptySelect - 4;
  } else {
    title = "Part 3";
    num = emptySelect - 8;
  }

  if (emptySelect !== -1) {
    ElMessage({
      message: `You haven't answer question the ${title} No.${num + 1} `,
      type: "warning",
      plain: true
    });
  }
  isSubmit.value = true;
  const techAve = calAverage(techAnswer.value.answer);
  const knowAve = calAverage(knowAnswer.value.answer);
  const eduAve = calAverage(eduAnswer.value.answer);
  createPic(techAve, knowAve, eduAve);
}

function calAverage(Arr) {
  let sum = Arr.reduce((sum, item) => {
    return (sum = sum + item);
  }, 0);

  return sum / Arr.length;
}

async function createPic(tech, know, edu) {
  await nextTick();
  let myChart = echarts.init(chartDom.value);
  myChart.resize({
    width: 600,
    height: 600
  });
  const offsetY = 0.75;
  const scaleT = 2.3 + tech / 3;
  const scaleK = 2.3 + know / 3;
  const scaleE = 2.3 + edu / 3;
  const vertices = [
    {
      name: "technique",
      value: [0 * scaleT, (Math.sqrt(3) - offsetY) * scaleT],
      score: tech
    },
    { name: "knowdege", value: [-1 * scaleK, -offsetY * scaleK], score: know },
    { name: "education", value: [1 * scaleE, -offsetY * scaleE], score: edu }
  ];
  const Gx = vertices.reduce((sum, item) => (sum += item.value[0]), 0) / 3;
  const Gy = vertices.reduce((sum, item) => (sum += item.value[1]), 0) / 3;
  const centerLines = [
    [Gx, Gy],
    vertices[0].value,
    [Gx, Gy],
    vertices[1].value,
    [Gx, Gy],
    vertices[2].value
  ];
  techLine.value = getLineFunction(vertices[0].value, [Gx, Gy]);
  knowLine.value = getLineFunction(vertices[1].value, [Gx, Gy]);
  eduLine.value = getLineFunction(vertices[2].value, [Gx, Gy]);

  const edges = vertices.map(v => v.value);
  edges.push(edges[0]);

  const option = {
    title: { text: "Sustainable awareness diagram", left: "center" },
    tooltip: {
      trigger: "item",
      formatter: ({ dataIndex }) => {
        const item = vertices[dataIndex];
        return `
    <div style="font-size: 16px;">
      <span style="color:blue;">·</span> ${item.name}<br/>
      <span style="color:blue;">·</span> Score: ${item.score.toFixed(2)}
    </div>
  `;
      }
    },
    grid: { left: 20, right: 20, top: 20, bottom: 20 },
    xAxis: { show: false, min: -4, max: 4 },
    yAxis: { show: false, min: -4, max: 4 },
    series: [
      {
        type: "line",
        data: edges,
        smooth: false,
        lineStyle: { color: "#67c23a", width: 3 },
        areaStyle: { color: "rgba(103,194,58,0.2)" },
        symbol: "none",
        z: 1
      },
      {
        type: "line",
        data: centerLines,
        lineStyle: { color: "#409eff", type: "dashed", width: 0.3 },
        z: 1,
        symbol: "none",
        smooth: false
      },
      {
        type: "scatter",
        data: vertices.map(v => v.value),
        symbolSize: 12,
        itemStyle: { color: "#409eff" },
        label: {
          show: true,
          position: "top",
          formatter: ({ dataIndex }) => vertices[dataIndex].name
        },
        z: 2
      },
      {
        type: "scatter",
        symbolSize: 6,
        itemStyle: { color: "#409eff" },
        data: [{ name: "center", value: [Gx, Gy] }],
        z: 2,
        tooltip: { show: false }
      }
    ]
  };
  myChart.setOption(option);
}

function handleDelete(index) {
  ElMessageBox.confirm("Are you sure to delete?", "delete confirm", {
    confirmButtonText: "Yes",
    cancelButtonText: "Cancel",
    type: "warning"
  })
    .then(() => {
      chainList.value.splice(index, 1);
      ElMessage({
        type: "success",
        message: "Delete completed"
      });
    })
    .catch(() => {});
}

function handleEdit(index) {
  drawer.value = true;
  editRow.value = index;
}

function cancelForm() {
  drawer.value = false;
  formEdit.value = {
    position: "",
    index: 0,
    content: ""
  };
  return;
}

async function submitEdit() {
  try {
    await editRef.value.validate();
    console.log(1);

    chainList.value[editRow.value].index = formEdit.value.index;
    chainList.value[editRow.value].position = formEdit.value.position;
    chainList.value[editRow.value].content = formEdit.value.content;
    drawer.value = false;
    formEdit.value = {
      index: 1,
      position: "",
      content: ""
    };
  } catch {
    console.log("failure");
  }
}
async function addChain() {
  try {
    await formRef.value.validate();
    chainList.value.push({
      position: formData.value.position,
      content: formData.value.content,
      index: chainList.value.length + 1
    });
    formData.value.position = "";
    formData.value.content = "";
  } catch {
    console.log("failure");
  }
}

function createChain() {
  renameVerticesToChain()
  const init = 0.6;
  let techNum = 0;
  let knowNum = 0;
  let eduNum = 0;
  chainList.value.sort((a, b) => {
    return a.index - b.index;
  });

  const NodeList = chainList.value.map(item => {
    if (item.position === "code") {
      let x;
      let y;
      if (techLine.value.k === "Infinity") {
        x = techLine.value.b;
        y = init - 0.3 + techNum * 0.5;
      } else {
        y = init - 0.3 + techNum * 0.5;
        x = (y - techLine.value.b) / techLine.value.k;
      }
      techNum++;
      return {
        index: item.index,
        position: item.position,
        content: item.content,
        color: "#f56c6c",
        coordinates: [x, y]
      };
    }
    if (item.position === "impact") {
      let x = -(init + knowNum * 0.5);
      let y = x * knowLine.value.k + knowLine.value.b;
      knowNum++;
      return {
        index: item.index,
        position: item.position,
        content: item.content,
        color: "#f56c6c",
        coordinates: [x, y]
      };
    }
    if (item.position === "sustainable development goal") {
      let x = init + eduNum * 0.5;
      let y = x * eduLine.value.k + eduLine.value.b;
      eduNum++;
      return {
        index: item.index,
        position: item.position,
        content: item.content,
        color: "#f56c6c",
        coordinates: [x, y]
      };
    }
  });
  console.log(NodeList);

  addChainToChart(NodeList);
}

function addChainToChart(NodeList) {
  let myChart = echarts.init(chartDom.value);
  const curOption = myChart.getOption();

  const baseSeriesCount = 4;
  if (curOption.series.length > baseSeriesCount) {
    curOption.series = curOption.series.slice(0, baseSeriesCount);
  }

  const nodeData = NodeList.map(item => {
    return {
      name: `${item.position}-${item.index}`,
      value: item.coordinates,
      index: item.index,
      position: item.position,
      content: item.content
    };
  });

  const lineData = [];
  for (let i = 0; i < NodeList.length - 1; i++) {
    let curNode = NodeList[i];
    let nextNode = NodeList[i + 1];
    lineData.push([
      {
        coord: curNode.coordinates,
        value: curNode.index,
        symbol: "none"
      },
      {
        coord: nextNode.coordinates,
        value: nextNode.index,
        symbol: "none"
      }
    ]);
  }
  console.log(lineData);

  const nodeSeries = {
    type: "scatter",
    data: nodeData,
    symbolSize: 8,
    itemStyle: { color: "blue" },
    tooltip: {
      trigger: "item",
      formatter: params => {
        const node = params.data;
        return `
         <div style="font-size: 14px; padding: 5px;">
          <div style="font-weight: bold;">
            ${node.position} - ${node.index}
          </div>
          <div style="margin-top: 5px;">
            <strong>content:</strong> ${node.content}
          </div>
        </div>
        `;
      }
    }
  };

  const lineSeries = {
    type: "lines",
    data: lineData,
    coordinateSystem: "cartesian2d",
    polyline:false,
    lineStyle: {
      width: 2,
      color: "black",
      type: "solid",
      opacity: 0.8,
      curveness: -0.4
    },
    tooltip: {
      show: false
    },
    effect: {
      show: true,
      period: 4,
      trailLength: 0,
      symbol: "arrow",
      symbolSize: 10,
      color: "black",


    }
  };
  curOption.series.push(lineSeries);
  curOption.series.push(nodeSeries);
  myChart.setOption(curOption, true);
}

function getLineFunction(p1, p2) {
  let k = (p2[1] - p1[1]) / (p2[0] - p1[0]);
  let b = p2[1] - k * p2[0];
  if (k === -Infinity) {
    return {
      k: "Infinity",
      b: p1[0]
    };
  }
  return {
    k: k,
    b: b
  };
}
function renameVerticesToChain() {
  const myChart = echarts.getInstanceByDom(chartDom.value);
  if (!myChart) return;

  const opt = myChart.getOption();

  if (!opt.series || !opt.series[2]) return;

  opt.series[2].label = opt.series[2].label || {};
  opt.series[2].label.show = true;
  opt.series[2].label.position = "top"; 
  opt.series[2].label.formatter = params => {
    const map = ["code", "impact", "sustainable development goal"];
    return map[params.dataIndex] || "";
  };

  myChart.setOption(opt, true);
}
</script>

<style lang="scss">
.card {
  width: 100%;
  height: 100%;
  padding: 10px;
}

.layout {
  padding: 20px;
  height: 100%;
  width: 100%;
  display: flex;
  gap: 20px;

  .question-area {
    width: 50%;
    height: 100%;
    background-color: pink;
    flex: 1;

    .card {
      overflow-y: auto;

      .topic {
        text-align: center;
        font-size: 28px;
        font-weight: 700;
        margin-bottom: 20px;
      }

      .main {
        .title {
          font-size: 23px;
          font-weight: 500;
          margin-bottom: 15px;
          text-align: left;
        }
      }

      .post {
        display: flex;
        align-items: center;
        justify-content: center;

        .button {
          margin-left: 3px;
        }
      }

      .tips {
        font-size: 12px;
        color: #909399;
        font-style: italic;
        line-height: 1.4;
        padding: 8px 12px;
        background: #f4f4f5;
        border-left: 3px solid #a8d8ea;
        border-radius: 4px;
        margin: 12px 0;
        cursor: help;
        transition: all 0.2s;

        &:hover {
          background: #e6f4ff;
          color: #5a9fd4;
          box-shadow: 0 2px 10px rgba(90, 159, 212, 0.25);
          transform: translateY(-1px);
        }
      }
    }
  }
}

.chart-area {
  width: 50%;
  height: 100%;
  flex: 1;

  .card {
    overflow-y: auto;

    .empty {
      margin-top: 200px;
    }

    .topic {
      text-align: center;
      font-size: 28px;
      font-weight: 700;
      margin-bottom: 20px;
    }

    .echart {
      display: flex;
      justify-content: center;
      align-items: bottom;
    }

    .link {
      margin-top: -60px;

      .table {
        margin-top: 30px;
      }

      .header {
        text-align: left;
        font-size: 20px;
      }

      .form {
        border: dashed 1px rgba(0, 0, 0, 0.3);
        padding: 10px;

        .select {
          width: 250px;
        }

        .button {
          display: flex;
          align-items: center;
          justify-content: center;
        }
      }
    }

    .create {
      margin-top: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>