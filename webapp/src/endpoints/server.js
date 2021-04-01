import axios from 'axios';

export const server = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 0,
    withCredentials: false
});

export const api = {
    userLogin: '/user/login',
    userToken: '/user/token'
};
