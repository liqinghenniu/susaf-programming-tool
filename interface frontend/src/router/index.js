import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',                 
    component: () => import('@/views/code-layout.vue'), 
    redirect: '/upload',  
    children: [
      { path: 'upload',  name: 'upload',  component: () => import('@/views/upload-file.vue')  },
      { path: 'quesiton', name: 'question',  component: () => import('@/views/fill-question.vue')  }
    ]
  }
]

export default createRouter({
  history: createWebHistory(),
  routes
})