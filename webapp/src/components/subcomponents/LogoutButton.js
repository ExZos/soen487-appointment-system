import {Button, withStyles} from '@material-ui/core';
import {useHistory} from 'react-router';

import {removeSession} from '../../utilities/sessionUtils';
import {logoutCall} from '../../utilities/authUtils';

const CustomButton = withStyles({
    root: {
        position: 'absolute',
        right: 0,
        top: 0,
        borderTopLeftRadius: 0,
        borderTopRightRadius: 0,
        borderBottomRightRadius: 0
    }
})(Button);

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
        <CustomButton variant="contained" size="small" onClick={logout}>Logout</CustomButton>
    );
}

export default LogoutButton;
