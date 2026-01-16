import { get } from '@/utils/request'

export function checkKeyword(){
  return get("/keyword")
}

export function checkLoop() {
  return get("/loop")
}

export function checkTest() {
  return get("/test")
}

export function checkMain(){
  return get("/main")
}

export function checkSize(){
  return get("/size")
}

export function checkResource(){
  return get("/resource")
}