import React from 'react';

import {getSession} from '../utilities/sessionUtils';

const sessionUser = getSession.user();

export const initialUser =  {
    identifier: sessionUser?.identifier,
    token: sessionUser?.token,
    isAdmin: sessionUser?.isAdmin
};

export const UserContext = React.createContext();
