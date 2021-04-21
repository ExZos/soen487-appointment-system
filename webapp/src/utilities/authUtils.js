import {server, api} from '../endpoints/server';

export const authCall = {
    admin: function(username, token) {
        return adminCall(api.adminAuth, username, token);
    },
    client: function(email, token) {
        return clientCall(api.userAuth, email, token);
    }
}

export const logoutCall = {
    admin: function(username, token) {
        return adminCall(api.adminLogout, username, token);
    },
    client: function(email, token) {
        return clientCall(api.userLogout, email, token);
    }
}

function adminCall(uri, username, token) {
    const config = buildReqConfig(token);
    config.headers['username'] = username;

    return server.post(uri, null, config);
}

function clientCall(uri, email, token) {
    const config = buildReqConfig(token);
    config.headers['email'] = email;

    return server.post(uri, null, config);
}

function buildReqConfig(token) {
    return {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'x-api-key': token
        }
    }
}
