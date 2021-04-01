import {useEffect, useRef} from 'react';
import {useHistory, useLocation} from 'react-router';
import {Button} from '@material-ui/core';

import {server, api} from '../endpoints/server';

function Login() {
    const history = useHistory();
    const location = useLocation();
    const code = useRef(new URLSearchParams(location.search).get('code'));

    useEffect(() => {
        if(code.current) {
            server.get(api.userToken + '?code=' + encodeURIComponent(code.current))
                .then(res => history.push({
                    pathname: '/home',
                    state: {
                        token: res.data
                    }
                }))
                .catch(err => console.log(err));
        }
    }, [location, history]);

    const userLogin = () => {
        server.post(api.userLogin)
            .then(res => window.location.replace(res.data))
            .catch(err => console.log(err));
    };

    const adminLogin = () => {
        console.log(encodeURIComponent(code.current));
    };

    return (
      <div id="login">
          <h3>
              Login
          </h3>

          <div>
            <Button variant="contained" color="primary" onClick={userLogin}>
                Login as a client
            </Button>
          </div>

          <div>
            <Button variant="contained" onClick={adminLogin}>
                Login as an Admin
            </Button>
          </div>
      </div>
    );
}

export default Login;
