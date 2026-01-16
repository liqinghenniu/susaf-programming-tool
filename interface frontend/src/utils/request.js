import axios from 'axios'
const baseURL = "http://localhost:8081/scan"
const timeout = 10000
const instance = axios.create({baseURL,timeout})
instance.interceptors.response.use(
  result => {
    return result.data
  },
  err =>{
    return Promise.reject(err)
  }

)

export function get(url){
    return instance.get(url)
}