import axios from 'axios';

export const server = axios.create({
    baseURL: 'http://localhost:8081',
    timeout: 0,
    withCredentials: false
});

export const api = {
    // Admin
    adminAuth: '/admin/auth',
    adminLogin: '/admin/login',
    adminLogout: '/admin/logout',

    // User
    userAuth: '/user/auth',
    userLogin: '/user/login',
    userToken: '/user/token',
    userLogout: '/user/logout',

    // Resource
    listResources: '/resource',
    addResource: '/resource/create',

    // Appointment
    listResourceAppointments: '/appointment/resourceAppointments',
    listOpenAppointments: '/appointment/resourceAppointments/open',
    getAppointmentDetails : '/appointment',
    getUserAppointments: '/appointment/user',
    bookAppointment: '/appointment/book',
    deleteAppointment: '/appointment',
    updateAppointment: '/appointment'
};
