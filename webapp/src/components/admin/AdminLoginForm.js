import {useState} from 'react';
import {useHistory} from 'react-router';
import {Button, Card, CardContent, CardHeader, FormControl, TextField, withStyles} from '@material-ui/core';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';

import '../../styles/form.css';
import {api, server} from '../../endpoints/server';
import {setSession} from '../../utilities/sessionUtils';

const windowHeight = window.innerHeight;

const LoginCard = withStyles({
    root: {
        display: 'inline-block',
        width: 'fit-content',
        marginTop: windowHeight/5 + 'px'
    }
})(Card);

function AdminLoginForm() {
    const history = useHistory();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [usernameError, setUsernameError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const submit = (callback) => {
        let valid = true;

        if(!username || !username.trim()) {
            setUsernameError('Required');
            valid = false;
        }

        if(!password || !password.trim()) {
            setPasswordError('Required');
            valid = false;
        }

        if(valid)
            callback();
    };

    const login = () => {
        const params = new URLSearchParams();
        params.append('username', username);
        params.append('password', password);

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        };

        server.post(api.adminLogin, params, config)
            .then(res => {
                const user = res.data;
                setSession.user({
                    username: user.username,
                    token: user.token
                });

                history.push('/admin/home');
            })
            .catch(() => alert('Invalid username or password'));
    };

    return (
        <div id="adminLoginForm" className="text-center">
            <LoginCard raised>
                <CardHeader title="Admin Login" className="pb-0" />
                <CardContent>
                    <div className="mb-3">
                        <FormControl>
                            <TextField size="small" label="Username" value={username} error={usernameError !== ''} helperText={usernameError}
                                       onChange={(e) => setUsername(e.currentTarget.value)} />
                        </FormControl>
                    </div>

                    <div className="mb-4">
                        <FormControl>
                            <TextField size="small" type="password" label="Password" value={password} error={passwordError !== ''} helperText={passwordError}
                                       onChange={(e) => setPassword(e.currentTarget.value)} />
                        </FormControl>
                    </div>

                    <Button variant="contained" color="primary" startIcon={<ExitToAppIcon />} onClick={() => submit(login)}>Login</Button>
                </CardContent>
            </LoginCard>
        </div>
    );
}

export default AdminLoginForm;
