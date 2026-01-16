<template>
  <router-view />
  <!-- <div class="layout">
    <div class="header"></div>
    <div class="middle">
      <div class="aside">
        <div class="title">File Upload</div>
        <div class="main">
          <el-upload class="upload-demo" accept=".zip" drag action="http://localhost:8081/upload"
          multiple="false" :on-success="handleSuccess" :on-error="handleError" v-if="!isUploadSuccess">
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            Drop file here or <em>click to upload</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              Please upload the file in zip format
            </div>
          </template>
        </el-upload>
        <div class="check" v-else>
          <el-icon color="grey" size="50"><Select /></el-icon>
          <div class="tip">You have uploaded the zip. </div>
          <el-button size="large" :icon="Document" round class="button">Get your report</el-button>
          <div class="tip2">upload your file and you will get report about SDG</div>
          <div class="back" @click="reupload"><el-icon size="26"><Back /></el-icon></div>
        </div>

        </div>
      
      </div>
      <div class="content"></div>
    </div>
    <div class="footer"></div>

  </div> -->
</template>
<script setup>
// import { UploadFilled, Select, Back, Document } from "@element-plus/icons-vue";
// import { ElMessage } from "element-plus";
// import { ref } from "vue";
// const isUploadSuccess = ref(false);
// function handleSuccess() {
//   isUploadSuccess.value = true;
//   ElMessage({
//     showClose: true,
//     message: "upload successfullly",
//     type: "success"
//   });
// }

// function handleError() {
//   ElMessage({
//     showClose: true,
//     message: "upload failure",
//     type: "error"
//   });
// }

// function reupload() {
//   isUploadSuccess.value = false;
// }
</script>

<style lang="scss">
// .layout {
//   height: 100%;
//   width: 100%;

//   .header {
//     background-color: pink;
//     width: 100%;
//     height: 25%;
//   }

//   .middle {
//     width: 100%;
//     display: flex;
//     height: 50%;

//     .aside {
//       width: 35%;
//       height: 100%;
//       .title{
//         font-size: 25px;
//         font-weight: 700;
//         height: 20%;
//         padding: 20px;
//         border-bottom: 1px solid black;
//       }
//       .main {
//         height: 80%;
//         display: flex;
//       justify-content: center;
//       align-items: center;
//         .upload-demo {
//           width: 70%;
//         }
//         .check {
//           display: flex;
//           justify-content: center;
//           align-items: center;
//           flex-direction: column;
//           position: relative;
//           width: 100%;
//           height: 100%;
//           .back {
//             position: absolute;
//             left: 15px;
//             top: 15px;
//           }
//           .button {
//             margin: 15px 0;
//           }
//           .tip2 {
//             font-size: 13px;
//             color: rgba(0, 0, 0, 0.6);
//           }
//         }
//       }
//     }

//     .content {
//       height: 100%;
//       width: 65%;
//       background-color: black;
//     }
//   }

//   .footer {
//     width: 100%;
//     height: 25%;
//     background-color: purple;
//   }
// }
</style>