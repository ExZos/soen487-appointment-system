import {server, api} from '../endpoints/server';

export const auth = {
    isClient: function(email, token) {
        const params = new URLSearchParams();
        params.append('email', email);

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'x-api-key': token
            }
        };

        return server.post(api.userAuth, params, config);
    },
    isAdmin: function(username, token) {
        const params = new URLSearchParams();
        params.append('username', username);

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'x-api-key': token
            }
        };

        return server.post(api.adminAuth, params, config);
    }
}
