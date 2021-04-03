import React from 'react';
import ReactDOM from 'react-dom';
import {Router, Switch} from 'react-router';
import {Route} from 'react-router-dom';
import {createBrowserHistory} from 'history';
import 'bootstrap/dist/css/bootstrap.min.css';

import './styles/index.css';
import AdminRoute from './components/authentication/AdminRoute';
import ClientRoute from './components/authentication/ClientRoute';
import Login from './components/Login';
import AdminLoginForm from './components/admin/AdminLoginForm';
import Home from './components/Home';

const history = createBrowserHistory();

const routing = (
    <React.StrictMode>
        <Router history={history}>
            <Switch>
                <AdminRoute path="/admin/home" component={Home} />
                <Route path="/admin/login" component={AdminLoginForm} />
                <ClientRoute path="/home" component={Home} />
                <Route path="/" component={Login} />
            </Switch>
        </Router>
    </React.StrictMode>
);

ReactDOM.render(routing, document.getElementById('root'));
