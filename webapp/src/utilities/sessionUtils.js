const userSessionKey = 'user';

interface IUser {
    username: string,
    email: string,
    token: string
}

export const setSession = {
    user: function(user: IUser) {
        sessionStorage.setItem(userSessionKey, JSON.stringify(user));
    },
    userUsername: function(username: string) {
        updateUserSessionKey('username', username);
    },
    userEmail: function(email: string) {
        updateUserSessionKey('email', email);
    },
    userToken: function(token: string) {
        updateUserSessionKey('token', token);
    }
};

export const getSession = {
    user: function() {
        return JSON.parse(sessionStorage.getItem(userSessionKey));
    }
}

function updateUserSessionKey(key, value) {
    let user = getSession.user();

    if(!user)
        user = {};

    user[key] = value;
    setSession.user(user);
}
