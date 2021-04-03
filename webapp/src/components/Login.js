import {useEffect, useRef, useState} from 'react';
import {useHistory, useLocation} from 'react-router';
import {Button, Card, CardContent, CardHeader, CircularProgress, withStyles} from '@material-ui/core';

import {server, api} from '../endpoints/server';
import {setSession} from '../utilities/sessionUtils';

const windowHeight = window.innerHeight;

const LoginCard = withStyles({
    root: {
        display: 'inline-block',
        width: 'fit-content',
        marginTop: windowHeight/4 + 'px'
    }
})(Card);

function Login() {
    const history = useHistory();
    const location = useLocation();
    const code = useRef(new URLSearchParams(location.search).get('code'));

    useEffect(() => {
        console.log()
        if(code.current) {
            server.get(api.userToken + '?code=' + encodeURIComponent(code.current))
                .then(res => {
                    const user = res.data;
                    setSession.user({
                        email: user.email,
                        token: user.token
                    });

                    history.push('/home');
                })
                .catch(() => {
                    alert('Invalid code');
                    code.current = null;
                    history.push('/');
                });
        }
    }, [location, history]);

    const userLogin = () => {
        server.post(api.userLogin)
            .then(res => window.location.assign(res.data))
            .catch(err => console.log(err));
    };

    const adminLogin = () => {
        history.push('/admin/login');
    };

    // Means that its coming from
    if(code.current)
        return (
            <div className="text-center mt-5">
                <CircularProgress />
            </div>
        );

    return (
      <div id="login" className="text-center">
          <LoginCard raised>
              <CardHeader title="Login" />
              <CardContent>
                  <div>
                      <Button fullWidth variant="contained" color="primary" onClick={userLogin}>
                          Login as a Client
                      </Button>
                  </div>

                  <div>
                      <Button fullWidth variant="contained" onClick={adminLogin}>
                          Login as an Admin
                      </Button>
                  </div>
              </CardContent>
          </LoginCard>
      </div>
    );
}

export default Login;
