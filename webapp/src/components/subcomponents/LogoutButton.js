import {Button} from '@material-ui/core';
import {useHistory} from 'react-router';

import {removeSession} from '../../utilities/sessionUtils';
import {logoutCall} from '../../utilities/authUtils';

function LogoutButton(props) {
    const history = useHistory();

    const logout = () => {
        if(props.username) // Admin
            generalLogoutCall(logoutCall.admin, props.username, props.token);
        else if(props.email) // Client
            generalLogoutCall(logoutCall.client, props.email, props.token);
    };

    const generalLogoutCall = (callback, identifier, token) => {
        callback(identifier, token)
            .then(() => {
                removeSession.user();
                history.push('/');
            });
    };

    return (
        <Button variant="contained" onClick={logout}>Logout</Button>
    );
}

export default LogoutButton;
