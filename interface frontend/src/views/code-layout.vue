<template>
  <div class="lay">
    <div class="header">
      <el-menu :default-active="$route.path" class="menu" mode="horizontal" router>
        <el-menu-item index="upload" :route="{ name: 'upload' }"><el-icon>
            <DocumentAdd />
          </el-icon>Upload File</el-menu-item>
        <el-menu-item index="question" :route="{ name: 'question' }"><el-icon>
            <Edit />
          </el-icon>Fill the Question Sheety</el-menu-item>
      </el-menu>
    </div>

    <div class="middle">
      <router-view />
      <!-- <div class="aside">
        <div class="title">File Upload</div>
        <div class="main">
          <el-upload class="upload-demo" accept=".zip" drag action="http://localhost:8081/upload" multiple="false"
            :on-success="handleSuccess" :on-error="handleError" v-if="!isUploadSuccess">
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
  <div class="back" @click="reupload"><el-icon size="26">
      <Back />
    </el-icon></div>
</div>
</div>
</div>
<div class="content"></div> -->
    </div>


    <div class="footer"></div>

  </div>
</template>
<script setup>
import { DocumentAdd, Edit } from "@element-plus/icons-vue";
</script>

<style lang="scss">
.lay {
  width: 100%;
  height: 100%;

  .header {
    height: 60px;
    width: 100%;

    .menu {
      height: 100%;
    }
  }

  .middle {
    width: 100%;
    height: 800px;
  }
}
</style>