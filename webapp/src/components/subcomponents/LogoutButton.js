import {Button, withStyles} from '@material-ui/core';
import {useHistory} from 'react-router';

import {removeSession} from '../../utilities/sessionUtils';
import {logoutCall} from '../../utilities/authUtils';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';

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
        if(props.user?.username) // Admin
            generalLogoutCall(logoutCall.admin, props.user.username, props.user.token);
        else if(props.user?.email) // Client
            generalLogoutCall(logoutCall.client, props.user.email, props.user.token);
    };

    const generalLogoutCall = (callback, identifier, token) => {
        callback(identifier, token)
            .then(() => {
                removeSession.user();
                history.push('/');
            });
    };

    if(props.iconOnly)
        return (
            <CustomButton variant="contained" size="small" onClick={logout}>
                <ExitToAppIcon />
            </CustomButton>
        );

    return (
        <CustomButton variant="contained" size="small" startIcon={<ExitToAppIcon />} onClick={logout}>Logout</CustomButton>
    );
}

export default LogoutButton;
