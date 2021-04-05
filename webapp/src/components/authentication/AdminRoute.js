import {useEffect, useState} from 'react';
import {Redirect} from 'react-router';
import {Route} from 'react-router-dom';
import {CircularProgress} from '@material-ui/core';

import {getSession} from '../../utilities/sessionUtils';
import {authCall} from '../../utilities/authUtils';

function AdminRoute({component: Component, ...rest}) {
    const [user] = useState(getSession.user());
    const [isAuth, setIsAuth] = useState(null);
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        authCall.admin(user?.username, user?.token)
            .then(res => setIsAuth(res.data))
            .catch(() => setIsAuth(false))
            .finally(() => setIsLoaded(true));
    }, [user, isLoaded]);

    const renderCircularProgress = () => {
        return (
            <div className="text-center mt-5">
                <CircularProgress />
            </div>
        );
    };

    return (
        <Route {...rest} render={props => (
            isLoaded ?
                isAuth ? <Component user={user} {...props} /> : <Redirect push to="/" /> :
                renderCircularProgress()
        )} />
    );

}

export default AdminRoute;
