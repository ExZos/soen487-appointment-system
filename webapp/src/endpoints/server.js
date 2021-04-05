import axios from 'axios';

export const server = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 0,
    withCredentials: false
});

export const api = {
    adminAuth: '/admin/auth',
    adminLogin: '/admin/login',
    adminLogout: '/admin/logout',

    userAuth: '/user/auth',
    userLogin: '/user/login',
    userToken: '/user/token',
    userLogout: '/user/logout',

    listResourceAppointments: '',
    listResources: '/resource',
    addResource: '/resource/create'
};
