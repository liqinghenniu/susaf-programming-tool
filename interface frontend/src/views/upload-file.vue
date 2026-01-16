<template>
  <div class="layout">
    <div class="left">
      <div class="upload-area">
        <el-card class="card">
          <div class="title">File Upload</div>
          <div class="main">
            <el-upload ref="upload" class="upload-demo" accept=".zip" drag action="http://localhost:8081/upload"
              :multiple="false" v-loading="isLoading" element-loading-text="Please loading..."
              :on-success="handleSuccess" :on-error="handleError" :before-upload="onProgress" v-if="!isUploadSuccess">
              <el-icon class="el-icon--upload"><upload-filled /></el-icon>
              <div class="el-upload__text">
                Drop file here or <em>click to upload</em>
              </div>
              <template #tip>
                <div class="el-upload__tip">Please upload the file in zip format</div>
              </template>
            </el-upload>


            <div class="check" v-else>
              <el-icon color="grey" size="50"><Select /></el-icon>
              <div class="tip">You have uploaded the zip.</div>
              <el-button size="large" :icon="Document" round class="button" @click="getReport" :disabled="isCheck">
                Get your report
              </el-button>
              <div class="tip2">upload your file and you will get report about SDG</div>
              <div class="back" @click="back">
                <el-icon size="26">
                  <Back />
                </el-icon>
              </div>
            </div>
            <el-dialog v-model="centerDialogVisible" title="Warning" width="500" align-center>
              <span>Do you confirm to reupload the file?</span>
              <template #footer>
                <div class="dialog-footer">
                  <el-button @click="centerDialogVisible = false">Cancel</el-button>
                  <el-button type="primary" @click="reupload">
                    Confirm
                  </el-button>
                </div>
              </template>
            </el-dialog>
          </div>
        </el-card>
      </div>

      <div class="tip-area">
        <el-card class="card">
          <div class="title">Instrction</div>
        </el-card>
      </div>
    </div>

    <div class="report-area">
      <el-card class="card">
        <div class="title">Report Content</div>
        <el-empty :image-size="200" v-if="!isCheck" class="empty"
          description="Please upload your file to get your report" />
        <div class="main" v-else>
          <div class="selectBox" v-if="isCheck">
            <div>Select:</div>
            <el-select @change="onChange" v-model="filterSDG" placeholder="Select certain SDG..." style="width: 240px"
              class="select">
              <el-option v-for="item in SDGOptionList" :key="item" :label="item" :value="item" />
            </el-select>
          </div>

          <ul class="ul">
            <el-skeleton :rows="12" animated v-if="isBoneShow" />
            <li v-for="(item) in (SDGDisplayList.length > 0 ? SDGDisplayList : resList)" :key="item" v-else>

              <div class="category"><el-icon color="skyblue" size="23" class="dot">
                  <Star />
                </el-icon>{{ item.sdg }}</div>
              <div class="content" v-if="item.type === 'loop'">
                <el-card class="card">
                  <div class="criteria">Infinite loop in your project</div>
                  <div v-if="item.information.length > 0" class="info">
                    <el-icon color="red" size="16">
                      <CloseBold />
                    </el-icon>
                    <div class="fault">There are <span class="label" v-for="item in item.information" :key="item">{{
                      item
                        }}</span> in your project.</div>
                  </div>
                  <div v-else class="info"><el-icon color="green" size="16"><Select /></el-icon> You have made a
                    contribution to the sustainability!</div>
                  <div class="tips">Your program contains an infinite-running loop. This means that the CPU will
                    continue to
                    run even after the main function has ended, wasting power and resources. Usually, it is necessary to
                    set
                    a limit on the loop conditions or add a termination statement block to promptly end the loop and
                    reduce
                    unnecessary resource consumption.</div>
                </el-card>
              </div>

              <div class="content" v-if="item.type === 'test'">
                <el-card class="card">
                  <div class="criteria">Test in your project</div>
                  <div v-if="item.information.length > 0" class="info">
                    <el-icon color="green" size="16">
                      <Select />
                    </el-icon>
                    <div class="fault">There are <span class="label" v-for="item in item.information" :key="item">{{
                      item
                        }}</span> contains the test parts</div>
                  </div>
                  <div v-else class="info"><el-icon color="red" size="16">
                      <CloseBold />
                    </el-icon> There is no file contains the test part in your project</div>
                  <div class="tips">No test cases were found in your code. Without testing, it is very difficult to
                    maintain
                    and improve the software. However, testing helps ensure that the software system has long-term
                    sustainability and reliability</div>
                </el-card>
              </div>
              <div class="content" v-if="item.type === 'main'">
                <el-card class="card">
                  <div class="criteria">main function length in your project</div>
                  <div v-if="item.information.length > 0" class="info">
                    <el-icon color="red" size="16">
                      <CloseBold />
                    </el-icon>
                    <div class="fault">Your main file <span class="label" v-for="item in item.information"
                        :key="item">{{
                          item }}</span>is too long in your project.</div>
                  </div>
                  <div v-else class="info"><el-icon color="green" size="16"><Select /></el-icon> You have made a
                    contribution to the sustainability!</div>
                  <div class="tips">Your main function is very long and is responsible for many functions. This makes
                    the
                    program difficult to maintain and expand. Sustainable programming decisions emphasize
                    modularization,
                    low coupling and good code structure.</div>
                </el-card>
              </div>
              <div class="content" v-if="item.type === 'size'">
                <el-card class="card">
                  <div class="criteria">fontsize in your project</div>
                  <div v-if="item.information.length > 0" class="info">
                    <el-icon color="red" size="16">
                      <CloseBold />
                    </el-icon>
                    <div class="fault">Your file <span class="label" v-for="item in item.information" :key="item">{{
                      item
                        }}</span>contain text too small.</div>
                  </div>
                  <div v-else class="info"><el-icon color="green" size="16"><Select /></el-icon> You have made a
                    contribution to the sustainability!</div>
                  <div class="tips">When your code is trapped in infinite loop, cpu will keep on 100% occupation</div>
                </el-card>
              </div>
              <div class="content" v-if="item.type === 'resource'">
                <el-card class="card">
                  <div class="criteria">close of stream in your project</div>
                  <div v-if="item.information.length > 0" class="info">
                    <el-icon color="red" size="16">
                      <CloseBold />
                    </el-icon>
                    <span class="fault">Your project includes <span class="label" v-for="item in item.information"
                        :key="item">
                        {{ item.resource }}
                      </span> did't close</span>
                  </div>
                  <div v-else class="info"><el-icon color="green" size="16"><Select /></el-icon> You have made a
                    contribution to the sustainability!</div>
                  <div class="tips">Some files or stream resources in the program have been opened but not closed. This
                    may
                    lead to waste of memory and system resources. Sustainable programming should promptly release
                    resources
                    and be responsible for using each piece of material</div>
                </el-card>
              </div>
              <div class="content" v-if="item.type === 'carbon'">
                <el-card class="card">
                  <div class="criteria">carbon emissiom in your project</div>
                  <div class="info">
                    Your project's carbon emission is {{ item.carbon }}g and the electicity consumption is {{
                    item.electricity }}kwh
                  </div>
                  <div class="tips">Based on how your code runs, it is estimated to consume a certain amount of
                    electricity
                    and produce CO₂ emissions. Optimizing your program can help reduce its environmental footprint and
                    support climate action</div>
                </el-card>
              </div>
            </li>
            <div class="goQuestion" v-if="!isBoneShow">
              <el-button type="primary" :icon="EditPen" @click="goQuestion"><span>Go to fill a question
                  sheet</span></el-button>
              <div class="hint">Help us to know your awareness change in SDG</div>
            </div>
          </ul>

        </div>

      </el-card>
    </div>

  </div>
</template>

<script setup>
import {
  UploadFilled,
  Select,
  Back,
  Document,
  CloseBold,
  Star,
  EditPen
} from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";
import { ref, computed } from "vue";
import { useRouter } from 'vue-router'
import axios from "axios";
import {
  // checkKeyword,
  checkLoop,
  checkTest,
  checkMain,
  checkSize,
  checkResource
} from "@/api/codeAnalysis";
const router = useRouter();
const isUploadSuccess = ref(false);
const isLoading = ref(false);
const isBoneShow = ref(false);
const isCheck = ref(false);
const resList = ref([]);
const centerDialogVisible = ref(false);
const filterSDG = ref("");
const SDGOptionList = ref([]);
const SDGDisplayList = computed(() => {
  return resList.value.filter(item => {
    return item.sdg === filterSDG.value;
  });
});
function handleSuccess() {
  isUploadSuccess.value = true;
  isLoading.value = false;
  ElMessage({
    showClose: true,
    message: "upload successfullly",
    type: "success"
  });
}

function handleError() {
  isLoading.value = false;
  ElMessage({
    showClose: true,
    message: "upload failure",
    type: "error"
  });
}

function onProgress() {
  isLoading.value = true;
}

function back() {
  centerDialogVisible.value = true;
}
function reupload() {
  isUploadSuccess.value = false;
  isCheck.value = false;
  centerDialogVisible.value = false;
}



function goQuestion() {
  router.push({ name: "question" })
}

async function getReport() {
  isBoneShow.value = true;
  isCheck.value = true;
  ElMessage({
    message: "Getting Report now.",
    type: "success"
  });
  // let keyword_res = await checkKeyword();
  // console.log(keyword_res);
  let loop_res = await checkLoop();
  let test_res = await checkTest();
  let main_res = await checkMain();
  let size_res = await checkSize();
  let resource_res = await checkResource();
  resList.value = []
  resList.value.push(
    // keyword_res,
    loop_res.data,
    test_res.data,
    main_res.data,
    size_res.data,
    resource_res.data
  );


  let response = await axios.post("http://localhost:8081/scan/carbon");
  let carbon_res = response.data.data;
  console.log(carbon_res);

  carbon_res.carbon = Number(carbon_res.carbon.toFixed(3))
  carbon_res.electricity = Number(carbon_res.electricity.toFixed(3))
  resList.value.push(carbon_res);

  SDGOptionList.value = [
    ...new Set(
      resList.value.map(item => {
        return item.sdg;
      })
    )
  ];
  SDGOptionList.value.unshift("All");
  isBoneShow.value = false;

  console.log(SDGOptionList.value);
}
</script>

<style lang="scss">
.layout {
  padding: 20px;
  height: 100%;
  width: 100%;
  display: flex;
  align-items: flex-start;
  gap: 20px;

  .left {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .upload-area {
      flex: 1;
      width: 600px;
      height: 400px;

      .card {
        width: 100%;
        height: 100%;

        .title {
          font-size: 25px;
          font-weight: 700;
          height: 40px;
          line-height: 30px;
          border-bottom: 1px solid black;
        }

        .main {
          margin-top: 20px;
          height: 320px;
          display: flex;
          justify-content: center;
          align-items: center;

          .upload-demo {
            width: 70%;
          }

          .check {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            position: relative;
            width: 100%;
            height: 100%;

            .back {
              position: absolute;
              left: 15px;
              top: 15px;
            }

            .button {
              margin: 15px 0;
            }

            .tip2 {
              font-size: 13px;
              color: rgba(0, 0, 0, 0.6);
            }
          }
        }
      }
    }

    .tip-area {
      width: 600px;
      margin-top: 10px;
      height: 370px;

      .card {
        width: 100%;
        height: 100%;

        .title {
          font-size: 25px;
          font-weight: 700;
          height: 40px;
          line-height: 30px;
          border-bottom: 1px solid black;
        }
      }
    }
  }

  .report-area {
    margin-left: 20px;
    flex: 1;
    height: 800px;
    width: 700px;

    .card {
      height: 100%;
      width: 100%;
      display: flex;
      flex-direction: column;

      .title {
        font-size: 25px;
        font-weight: 700;
        height: 40px;
        line-height: 30px;
        border-bottom: 1px solid black;
      }

      .empty {
        padding-top: 250px;
      }

      .main {
        height: 700px;

        .ul {
          padding: 5px;
          list-style: none;
          padding-right: 20px;
          height: 650px;
          overflow-y: auto;

          .goQuestion {
            text-align: center;
            padding: 20px 10px;

            .hint {
              font-size: 13px;
              margin-top: 5px;
              color: rgba(0, 0, 0, 0.5);

            }
          }
        }


        .selectBox {
          display: flex;
          justify-content: left;
          align-items: last baseline;
          gap: 8px;

          .select {
            margin-top: 15px;
          }
        }

        li {
          margin-top: 15px;

          .category {
            font-size: 23px;
            padding: 5px;
            align-items: flex-start;

            .dot {
              margin-right: 5px;
            }
          }

          .content {
            font-size: 18px;
            padding: 5px;
            color: rgba(0, 0, 0, 0.6);

            .card {
              .criteria {
                font-size: 20px;
                color: rgba(0, 0, 0, 0.7);
                margin-bottom: 10px;
              }

              .info {
                display: flex;
                align-items: center;
                line-height: 16px;
                gap: 3px;
                font-size: 16px;
                line-height: 20px;

                .label {
                  font-weight: 700;
                  border-bottom: 1px rgba(0, 0, 0, 0.6) solid;
                  margin-left: 4px;
                  font-size: 18px;
                  color: red;

                  &:last-child {
                    margin-right: 4px;
                  }
                }
              }

              .tips {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, sans-serif;
                line-height: 1.6;
                color: #2c3e50;
                max-width: 700px;
                background: white;
                border-radius: 8px;
                padding: 20px;
                border-left: 4px solid #3498db;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
                margin-top:10px;
              }
            }
          }
        }
      }
    }
  }
}
</style>