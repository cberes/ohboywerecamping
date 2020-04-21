import axios from 'axios'

export default {
  get (url) {
    return axios.get(url)
  },
  post (url, request, headers) {
    const options = headers && { headers }
    return axios.post(url, request, options)
  }
}
