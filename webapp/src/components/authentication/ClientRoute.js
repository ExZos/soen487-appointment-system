import {useEffect, useState} from 'react';
import {Redirect} from 'react-router';
import {Route} from 'react-router-dom';
import {CircularProgress} from '@material-ui/core';

import {getSession} from '../../utilities/sessionUtils';
import {auth} from '../../utilities/authUtils';

function ClientRoute({component: Component, ...rest}) {
    const [user] = useState(getSession.user());
    const [isAuth, setIsAuth] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        auth.isClient(user?.email, user?.token)
            .then(res => setIsAuth(res.data))
            .catch(() => setIsAuth(false))
            .finally(() => setIsLoaded(true));
    }, [user, isLoaded]);

    return (
        <Route {...rest} render={props => (
            isLoaded ?
                isAuth ? <Component user={user} {...props} /> : <Redirect push to="/" /> :
                <CircularProgress />
        )} />
    );

}

export default ClientRoute;
